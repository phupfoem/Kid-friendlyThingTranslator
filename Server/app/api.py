from yolov3.yolo import ImageLabeler
from yolov3.image_converter import string_to_image, to_rgb

from word_definition.memory_dictionary import MemoryDictionary
from word_definition.database_dictionary import DatabaseDictionary

from app.model import *
from app.auth.auth_bearer import JWTBearer
from app.auth.auth_handler import sign_jwt

from fastapi import Body, Depends, FastAPI
from fastapi import HTTPException

from google.cloud import vision

import mysql.connector

import cv2

import decouple
import logging
import time

from typing import Any, AnyStr, Dict, List, Union


JSONObject = Dict[AnyStr, Any]
JSONArray = List[Any]
JSONStructure = Union[JSONArray, JSONObject]


logging.basicConfig(level=logging.INFO)


# REST-ful API server
app = FastAPI()


# DB connection
db_user = mysql.connector.connect(
    host=decouple.config('host'),
    user=decouple.config('user'),
    password=decouple.config('password'),
    database=decouple.config('database_user')
)


# Instantiates a GG Cloud client
try:
    logging.info("Instantiating a Google Cloud Vision Client...")
    client = vision.ImageAnnotatorClient()
    logging.info("Google Cloud Vision Client instantiated")
except Exception as e:
    logging.warning("Failed to instantiate a Google Cloud Vision Client: %s", str(e))
    client = None


# Word dictionary
try:
    logging.info("Loading a Memory Dictionary...")
    memory_word_dict = MemoryDictionary(decouple.config("common_word_definition"))
    logging.info("Loaded a Memory Dictionary")
except MemoryError:
    logging.warning("Fail to load a Memory Dictionary")
    memory_word_dict = None

try:
    logging.info("Loading a Database Dictionary...")
    database_word_dict = DatabaseDictionary(
        mysql.connector.connect(
            host=decouple.config('host'),
            user=decouple.config('user'),
            password=decouple.config('password'),
            database=decouple.config('database_dictionary')
        )
    )
    logging.info("Loaded a Database Dictionary")
except Exception as e:
    logging.error("Fail to load a Database Dictionary: %s", str(e))
    memory_word_dict = None


# Yolov3 Image Labeler
logging.info("Loading YOLO from disk...")
yolov3_image_labeler = ImageLabeler(
    labels_path=decouple.config("yolov3_names"),
    weights_path=decouple.config("yolov3_weights"),
    config_path=decouple.config("yolov3_config")
)
logging.info("Loaded YOLO")


# helpers
def check_user(data: UserLoginSchema):
    my_cursor = db_user.cursor()

    sql = "SELECT username FROM Accounts WHERE username = %s AND password = %s"
    param = (data.email, data.password)

    my_cursor.execute(sql, param)
    my_result = my_cursor.fetchone()

    return my_result is not None


def get_user_name(data: UserLoginSchema):
    my_cursor = db_user.cursor()

    sql = "SELECT name FROM Accounts WHERE username = %s AND password = %s"
    param = (data.email, data.password)

    my_cursor.execute(sql, param)
    my_result = my_cursor.fetchone()[0]

    return my_result


# route handlers
@app.get("/")
async def read_root() -> dict:
    return {"message": "Welcome!."}


@app.post("/", dependencies=[Depends(JWTBearer())])
async def add_sth() -> dict:
    return {
        "data": "Nothing"
    }


@app.post("/user/signup")
async def create_user(user: UserSignupSchema = Body(...)) -> dict:
    try:
        my_cursor = db_user.cursor()

        sql = "INSERT INTO accounts(username, password, name) VALUES (%s, %s, %s)"
        param = (user.email, user.password, user.name)

        my_cursor.execute(sql, param)
        db_user.commit()

        return {
            "message": "Ok"
        }
    except mysql.connector.errors.IntegrityError:
        raise HTTPException(status_code=401, detail="Email has already been used.")


@app.post("/user/login")
async def user_login_body(user: UserLoginSchema = Body(...)) -> dict:
    if check_user(user):
        return {**sign_jwt(user.email), **{'name': get_user_name(user)}}
    raise HTTPException(status_code=401, detail="Wrong email/password.")


@app.post("/label-image")
async def recognize_image(image_base64: str = Body(...)) -> dict:
    image = to_rgb(string_to_image(image_base64))

    # Try using GG Cloud service
    try:
        _, encoded_image = cv2.imencode('.jpg', image)
        image = vision.Image(content=encoded_image.tobytes())

        # Performs label detection on the image file
        response = client.label_detection(image=image)
        labels = response.label_annotations

        if response.error.message:
            return {
                "message": response.error.message
            }

        label = labels[0].description if labels else ""
    except Exception as e:
        logging.warning("Fail to use Google Cloud Vision: %s", str(e))

        # Fall back to local yolov3
        start = time.time()
        label = yolov3_image_labeler.label_image(image)
        end = time.time()

        # show timing information on YOLO
        logging.info("YOLO took {:.6f} seconds".format(end - start))

    return {
        "message": "Ok",
        "word": label,
        "description": memory_word_dict.define(label) or database_word_dict.define(label)
    }
