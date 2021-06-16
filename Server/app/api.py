from .api_helper import ApiHelper
from .model import *

from .auth.auth_bearer import JWTBearer
from .auth.auth_handler import sign_jwt

from fastapi import Body, Depends, FastAPI
from fastapi import HTTPException

import logging


logging.basicConfig(level=logging.INFO)


# REST-ful API server
app = FastAPI()

# API Helper
api_helper = ApiHelper()


# route handlers
@app.post("/user/signup")
async def create_user(user: UserSignupSchema = Body(...)) -> dict:
    if api_helper.create_user(user):
        return {
            "message": "Ok"
        }

    raise HTTPException(status_code=401, detail="Email has already been used.")


@app.post("/user/login")
async def user_login_body(user: UserLoginSchema = Body(...)) -> dict:
    if api_helper.is_user_exist(user):
        return {
            **sign_jwt(user.email),
            **{
                'name': api_helper.get_user_name(user)
            }
        }

    raise HTTPException(status_code=401, detail="Wrong email/password.")


@app.post("/label-image", dependencies=[Depends(JWTBearer())])
async def recognize_image(image_base64: str = Body(...)) -> dict:
    # Try using GG Cloud service, with fall back to yolov3
    label = (
        api_helper.label_image_by_gg_cloud_vision(image_base64)
        or api_helper.label_image_by_yolov3(image_base64)
    )

    description = (
            api_helper.define_word_by_memory_dictionary(label)
            or api_helper.define_word_by_database_dictionary(label)
    )

    return {
        "message": "Ok",
        "word": label,
        "description": description
    }
