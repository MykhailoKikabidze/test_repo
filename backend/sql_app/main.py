from sqlalchemy.ext.asyncio import AsyncSession

from fastapi import Depends, FastAPI

from .logging_api import logging_crud, logging_schemas
from .categories import category_crud, category_schemas
from .profile import profile_crud, profile_schemas
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
async def delete_activity(activity_name: str, cat_name: str, user_email: str, db: AsyncSession = Depends(get_db_session)):
    result = await category_crud.delete_activity(db=db, cat_name=cat_name, user_email=user_email, activity_name=activity_name)
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


@app.get("/activities/", response_model=Union[List[category_schemas.Activity], category_schemas.Status])
async def show_activities(cat_name: str, user_email: str, db: AsyncSession = Depends(get_db_session)):
    result = await category_crud.get_activities(db=db, cat_name=cat_name, user_email=user_email)
    if result is not None:
        return result
    return category_schemas.Status(status="error", message="Category or user is not found.")


@app.put("/profile/update/login/", response_model=category_schemas.Status)
async def update_user_login(new_login: str, user_email: str, db: AsyncSession = Depends(get_db_session)):
    result = await profile_crud.change_login(db=db, email=user_email, new_login=new_login)
    res_status = category_schemas.Status(status=result["status"], message=result["message"])
    return res_status


@app.put("/profile/update/email/", response_model=category_schemas.Status)
async def update_user_email(new_email: str, user_email: str, db: AsyncSession = Depends(get_db_session)):
    result = await profile_crud.change_email(db=db, email=user_email, new_email=new_email)
    res_status = category_schemas.Status(status=result["status"], message=result["message"])
    return res_status


@app.put("/profile/update/password/", response_model=category_schemas.Status)
async def update_user_password(new_password: str, user_email: str, db: AsyncSession = Depends(get_db_session)):
    result = await profile_crud.change_password(db=db, email=user_email, new_password=new_password)
    res_status = category_schemas.Status(status=result["status"], message=result["message"])
    return res_status


@app.post("/profile/", response_model=category_schemas.Status)
async def add_profile(user_email: str, last_log: str, db: AsyncSession = Depends(get_db_session)):
    result = await profile_crud.add_default_profile(db=db, email=user_email, last_log=last_log)
    res_status = category_schemas.Status(status=result["status"], message=result["message"])
    return res_status


@app.get("/profile/points/", response_model=Union[int, category_schemas.Status])
async def get_profile_points(user_email: str, db: AsyncSession = Depends(get_db_session)):
    result = await profile_crud.get_points(db=db, email=user_email)
    if result is not None:
        return result
    else:
        return category_schemas.Status(status="error", message="User profile is not found.")


@app.put("/profile/points/", response_model=category_schemas.Status)
async def update_profile_points(user_email: str, points: int, action: str, db: AsyncSession = Depends(get_db_session)):
    result = await profile_crud.update_profile_points(db=db, email=user_email, points=points, action=action)
    res_status = category_schemas.Status(status=result["status"], message=result["message"])
    return res_status


@app.put("/profile/last_log/", response_model=category_schemas.Status)
async def update_profile_last_log(user_email: str, new_date_log: str, db: AsyncSession = Depends(get_db_session)):
    result = await profile_crud.update_profile_last_log(db=db, email=user_email, new_date_log=new_date_log)
    res_status = category_schemas.Status(status=result["status"], message=result["message"])
    return res_status
