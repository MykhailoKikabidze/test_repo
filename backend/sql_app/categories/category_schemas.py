from pydantic import BaseModel


class Category(BaseModel):
    name: str


class Activity(BaseModel):
    name: str


class ActivityLogs(BaseModel):
    date: str
    time_spent: float


class Status(BaseModel):
    status: str
    message: str
