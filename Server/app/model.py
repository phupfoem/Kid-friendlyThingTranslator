from pydantic import BaseModel, Field


class UserSignupSchema(BaseModel):
    email: str = Field(...)
    password: str = Field(...)
    name: str = Field(...)


class UserLoginSchema(BaseModel):
    email: str = Field(...)
    password: str = Field(...)
