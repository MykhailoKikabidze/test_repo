from pydantic import BaseModel


class Category(BaseModel):
    name: str


class Activity(BaseModel):
    name: str


class ActivityLogs(BaseModel):
    date: str
    time_spent: int


class Status(BaseModel):
    status: str
    message: str
