from pydantic import BaseModel, Field


class UserSchema(BaseModel):
    username: str = Field(...)
    password: str = Field(...)


class UserLoginSchema(BaseModel):
    username: str = Field(...)
    password: str = Field(...)

#
# class ImageSchema(BaseModel):
#     image: bytearray
