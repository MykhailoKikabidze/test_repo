from pydantic import BaseModel


class User(BaseModel):
    login: str
    email: str
    password: str


class Error(BaseModel):
    num: int
    description: str
