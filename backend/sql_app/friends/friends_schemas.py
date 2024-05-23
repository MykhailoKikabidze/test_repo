from pydantic import BaseModel


class Friendship(BaseModel):
    id_user1: int
    id_user2: int
