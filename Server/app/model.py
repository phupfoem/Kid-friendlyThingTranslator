from pydantic import BaseModel, Field


class UserSchema(BaseModel):
    email: str = Field(...)
    password: str = Field(...)


class UserLoginSchema(BaseModel):
    email: str = Field(...)
    password: str = Field(...)

#
# class ImageSchema(BaseModel):
#     image: bytearray
