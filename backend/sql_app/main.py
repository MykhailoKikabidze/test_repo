from sqlalchemy.ext.asyncio import AsyncSession

from fastapi import Depends, FastAPI
import asyncio

from . import crud, schemas
from .database import AsyncSessionLocal

from pydantic.types import Union


app = FastAPI()


@app.get("/")
async def root():
    return {"message": "Success connection!"}


# Dependency
async def get_db_session() -> AsyncSession:
    async with AsyncSessionLocal() as session:
        yield session


@app.post("/users/email/", response_model=Union[schemas.User, schemas.Error])
async def authorization_user(user: schemas.User, db: AsyncSession = Depends(get_db_session)):
    db_user = await crud.get_user_by_email(db, email=user.email)
    if db_user is None or not crud.verify_password(user.password, db_user.password):
        return schemas.Error(num=404, description="User not found. Invalid email or password")

    return db_user


@app.post("/users/", response_model=Union[schemas.User, schemas.Error])
async def create_user(user: schemas.User, db: AsyncSession = Depends(get_db_session)):
    db_user = await crud.get_user_by_email(db, email=user.email)
    if db_user:
        return schemas.Error(num=400, description="Email already registered")
    return await crud.create_user_db(db=db, user=user)
