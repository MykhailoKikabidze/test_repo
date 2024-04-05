from sqlalchemy.ext.asyncio import AsyncSession

from fastapi import Depends, FastAPI

from .logging_api import logging_crud, logging_schemas
from .categories import category_crud, category_schemas
from .database import AsyncSessionLocal

from pydantic.types import Union, List


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
        return logging_schemas.LogError(num=404, description="User not found. Invalid email or password.")

    return db_user


@app.post("/users/", response_model=Union[logging_schemas.User, logging_schemas.LogError])
async def create_user(user: logging_schemas.User, db: AsyncSession = Depends(get_db_session)):
    db_user = await logging_crud.get_user_by_email(db, email=user.email)
    if db_user:
        return logging_schemas.LogError(num=400, description="Email already registered.")
    return await logging_crud.create_user_db(db=db, user=user)


@app.get("/categories/", response_model=List[category_schemas.Category])
async def show_categories(db: AsyncSession = Depends(get_db_session)):
    return await category_crud.get_categories(db=db)


@app.post("/activities/name/", response_model=category_schemas.Status)
async def create_activity(activity: category_schemas.Activity, cat_name: str, user_email: str, db: AsyncSession = Depends(get_db_session)):
    act = await category_crud.get_activity(db=db, cat_name=cat_name, user_email=user_email, activity_name=activity.name)
    if act:
        return category_schemas.Status(status="error", message="The activity already exists.")
    result = await category_crud.add_activity_by_category(db=db, cat_name=cat_name, user_email=user_email, activity_name=activity.name)
    res_status = category_schemas.Status(status=result["status"], message=result["message"])
    return res_status


@app.delete("/activities/name/", response_model=category_schemas.Status)
async def delete_activity(activity: category_schemas.Activity, cat_name: str, user_email: str, db: AsyncSession = Depends(get_db_session)):
    result = await category_crud.delete_activity(db=db, cat_name=cat_name, user_email=user_email, activity_name=activity.name)
    res_status = category_schemas.Status(status=result["status"], message=result["message"])
    return res_status


@app.put("/activities/name/", response_model=category_schemas.Status)
async def update_activity(activity: category_schemas.Activity, cat_name: str, user_email: str, new_activity_name: str, db: AsyncSession = Depends(get_db_session)):
    result = await category_crud.update_activity(db=db, cat_name=cat_name, user_email=user_email, activity_name=activity.name, new_activity_name=new_activity_name)
    res_status = category_schemas.Status(status=result["status"], message=result["message"])
    return res_status


@app.post("/activity_log/",  response_model=category_schemas.Status)
async def add_activity_log(activity_log: category_schemas.ActivityLogs, cat_name: str, user_email: str, activity_name: str, db: AsyncSession = Depends(get_db_session)):
    result = await category_crud.add_activity_log(db=db, cat_name=cat_name, user_email=user_email, activity_name=activity_name, activity_log=activity_log)
    res_status = category_schemas.Status(status=result["status"], message=result["message"])
    return res_status
