from yolov3.yolo import label_image, stringToImage, toRGB

from app.model import *
from app.auth.auth_bearer import JWTBearer
from app.auth.auth_handler import sign_jwt

from fastapi import Body, Depends, FastAPI
from fastapi import HTTPException

# Imports the Google Cloud client library
# from google.cloud import vision

import mysql.connector

import decouple
import base64

from typing import Any, AnyStr, Dict, List, Union


JSONObject = Dict[AnyStr, Any]
JSONArray = List[Any]
JSONStructure = Union[JSONArray, JSONObject]


app = FastAPI()

my_db = mysql.connector.connect(
    host=decouple.config('host'),
    user=decouple.config('user'),
    password=decouple.config('password'),
    database=decouple.config('database_user')
)

# Instantiates a GG Cloud client
# client = vision.ImageAnnotatorClient()


# helpers
def check_user(data: UserLoginSchema):
    my_cursor = my_db.cursor()

    sql = "SELECT username FROM Accounts WHERE username = %s AND password = %s"
    param = (data.email, data.password)

    my_cursor.execute(sql, param)
    my_result = my_cursor.fetchone()

    return my_result is not None


def get_user_name(data: UserLoginSchema):
    my_cursor = my_db.cursor()

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
    my_cursor = my_db.cursor()

    sql = "INSERT INTO accounts(username, password, name) VALUES (%s, %s, %s)"
    param = (user.email, user.password, user.name)

    my_cursor.execute(sql, param)
    my_db.commit()

    return {
        "message": "Ok"
    }


@app.post("/user/login")
async def user_login_body(user: UserLoginSchema = Body(...)) -> dict:
    if check_user(user):
        return sign_jwt(user.email) | {'name': get_user_name(user)}
    raise HTTPException(status_code=401, detail="Wrong email/password.")


@app.post("/label-image")
async def recognize_image(image_base64: str = Body(...)) -> dict:
    image = base64.b64decode(image_base64)

    # # Performs label detection on the image file
    # response = client.label_detection(image=image)
    # labels = response.label_annotations
    #
    # if response.error.message:
    #     return {
    #         "message": response.error.message
    #     }
    #
    # print('Labels:')
    # for label in labels:
    #     print(label.description)
    #
    # return {
    #     "message": labels[0] if labels else "Spooky~~"
    # }

    label = label_image(toRGB(stringToImage(image_base64))) or "Spooky~~"
    return {
        "message": label
    }
