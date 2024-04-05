from pydantic import BaseModel


class User(BaseModel):
    login: str
    email: str
    password: str


class LogError(BaseModel):
    num: int
    description: str
