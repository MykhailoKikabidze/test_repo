from sqlalchemy.ext.asyncio import AsyncSession

from fastapi import Depends, FastAPI

from .logging_api import logging_crud, logging_schemas
from .categories import category_crud, category_schemas
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


@app.post("/users/email/", response_model=Union[logging_schemas.User, logging_schemas.LogError])
async def authorization_user(user: logging_schemas.User, db: AsyncSession = Depends(get_db_session)):
    db_user = await logging_crud.get_user_by_email(db, email=user.email)
    if db_user is None or not logging_crud.verify_password(user.password, db_user.password):
        return logging_schemas.LogError(num=404, description="User not found. Invalid email or password")

    return db_user


@app.post("/users/", response_model=Union[logging_schemas.User, logging_schemas.LogError])
async def create_user(user: logging_schemas.User, db: AsyncSession = Depends(get_db_session)):
    db_user = await logging_crud.get_user_by_email(db, email=user.email)
    if db_user:
        return logging_schemas.LogError(num=400, description="Email already registered")
    return await logging_crud.create_user_db(db=db, user=user)


@app.post("/activities/name/", response_model=Union[category_schemas.Activity, category_schemas.ActivError])
async def create_activity(activity: category_schemas.Activity, cat_name: str, user_email: str, db: AsyncSession = Depends(get_db_session)):
    act = await category_crud.get_activity(db=db, cat_name=cat_name, user_email=user_email, activity_name=activity.name)
    if act:
        return category_schemas.ActivError(num=400, description="The activity already exists")
    result = await category_crud.add_activity_by_category(db=db, cat_name=cat_name, user_email=user_email, activity_name=activity.name)
    if result is None:
        return category_schemas.ActivError(num=404, description="Error adding activity into database")
    return result

