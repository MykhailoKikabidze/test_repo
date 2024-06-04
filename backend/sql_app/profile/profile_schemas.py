from pydantic import BaseModel


class Profile(BaseModel):
    last_log: str
    bonus: float
    points: int
    image: str
