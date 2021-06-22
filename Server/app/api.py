from .api_helper import ApiHelper
from .model import *

from .auth.auth_bearer import JWTBearer
from .auth.auth_handler import sign_jwt, decode_jwt, token_response

from fastapi import Body, Depends, Header, FastAPI
from fastapi import HTTPException

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
    if api_helper.is_user_login_info_exist(user):
        return {
            **sign_jwt(user.email),
            **{
                'name': api_helper.get_user_name(user.email)
            }
        }

    raise HTTPException(status_code=401, detail="Email has already been used")


@app.post("/check-token", dependencies=[Depends(JWTBearer())])
async def check_token(authorization: str = Header(None)) -> dict:
    token = authorization.split(' ')[1]
    email = decode_jwt(token)['data']

    return {
        **token_response(token),
        **{
              'name': api_helper.get_user_name(email)
        }
    }


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
