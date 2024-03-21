from pydantic import BaseModel


class Category(BaseModel):
    name: str


class Activity(BaseModel):
    name: str


class ActivityLogs(BaseModel):
    date: str
    time_spent: float


class ActivError(BaseModel):
    num: int
    description: str


class ActivLogError(BaseModel):
    num: int
    description: str
