from pydantic import BaseModel


class Statistics(BaseModel):
    data: str
    period: str
    time: str
