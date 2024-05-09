from sqlalchemy.ext.asyncio import AsyncSession
from backend.sql_app.categories import category_models, category_schemas
from backend.sql_app.logging_api import logging_models
from sqlalchemy import select
from datetime import date, timedelta
from typing import Sequence, Optional


async def get_categories(db: AsyncSession) -> Sequence[category_models.Category]:
    async with db as session:
        result = await session.execute(
            select(category_models.Category)
        )

        return result.scalars().all()


async def get_user_id(db: AsyncSession, user_email: str) -> int:
    async with db as session:
        user_query = await session.execute(
            select(logging_models.User.id).where(logging_models.User.email == user_email)
        )
        user_id = user_query.scalar_one_or_none()
        return user_id


async def get_category_id(db: AsyncSession, cat_name: str) -> int:
    async with db as session:
        cat_query = await session.execute(
            select(category_models.Category.id).where(category_models.Category.name == cat_name)
        )
        cat_id = cat_query.scalar_one_or_none()
        return cat_id


async def get_activity_id(db: AsyncSession, cat_name: str, user_email: str, activity_name: str) -> Optional[int]:
    async with db as session:
        cat_id = await get_category_id(db=db, cat_name=cat_name)
        user_id = await get_user_id(db=db, user_email=user_email)

        if cat_id is not None and user_id is not None:
            result = await session.execute(
                select(category_models.Activity.id).filter(
                    category_models.Activity.id_category == cat_id,
                    category_models.Activity.id_user == user_id,
                    category_models.Activity.name == activity_name)
            )

            return result.scalars().first()

        return None


async def get_activities(db: AsyncSession, cat_name: str, user_email: str) -> Optional[Sequence[category_models.Activity]]:
    async with db as session:
        cat_id = await get_category_id(db=db, cat_name=cat_name)
        user_id = await get_user_id(db=db, user_email=user_email)

        if cat_id is not None and user_id is not None:
            result = await session.execute(
                select(category_models.Activity).filter(
                    category_models.Activity.id_category == cat_id,
                    category_models.Activity.id_user == user_id)
            )

            return result.scalars().all()

        return None


async def add_activity_by_category(db: AsyncSession, cat_name: str, user_email: str, activity_name: str) -> dict:
    async with db as session:
        cat_id = await get_category_id(db=db, cat_name=cat_name)
        user_id = await get_user_id(db=db, user_email=user_email)

        if cat_id is None or user_id is None:
            return {"status": "error", "message": "Category or user is not found."}

        new_activity = category_models.Activity(id_user=user_id, id_category=cat_id, name=activity_name)
        session.add(new_activity)
        await session.commit()
        await session.refresh(new_activity)
        return {"status": "success", "message": "Activity added successfully."}


async def delete_activity(db: AsyncSession, cat_name: str, user_email: str, activity_name: str) -> dict:
    async with db as session:
        cat_id = await get_category_id(db=db, cat_name=cat_name)
        user_id = await get_user_id(db=db, user_email=user_email)

        if cat_id is None or user_id is None:
            return {"status": "error", "message": "Category or user is not found."}

        activity = await session.execute(
            select(category_models.Activity).filter(
                category_models.Activity.id_category == cat_id,
                category_models.Activity.id_user == user_id,
                category_models.Activity.name == activity_name)
        )

        activity_to_delete = activity.scalars().first()

        if activity_to_delete is None:
            return {"status": "error", "message": "Activity is not found."}

        await session.delete(activity_to_delete)
        await session.commit()

        return {"status": "success", "message": "Activity deleted successfully."}


async def update_activity(db: AsyncSession, cat_name: str, user_email: str, activity_name: str,
                          new_activity_name: str) -> dict:
    async with db as session:
        cat_id = await get_category_id(db=db, cat_name=cat_name)
        user_id = await get_user_id(db=db, user_email=user_email)

        if cat_id is None or user_id is None:
            return {"status": "error", "message": "Category or user is not found."}

        activity = await session.execute(
            select(category_models.Activity).filter(
                category_models.Activity.id_category == cat_id,
                category_models.Activity.id_user == user_id,
                category_models.Activity.name == activity_name)
        )

        activity_to_update = activity.scalars().first()

        if activity_to_update is None:
            return {"status": "error", "message": "Activity is not found."}

        activity_check = await session.execute(
            select(category_models.Activity).filter(
                category_models.Activity.id_category == cat_id,
                category_models.Activity.id_user == user_id,
                category_models.Activity.name == new_activity_name)
        )

        activity_to_check = activity_check.scalars().first()

        if activity_to_check is not None:
            return {"status": "error", "message": "Activity with new name already exists."}

        activity_to_update.name = new_activity_name
        session.add(activity_to_update)
        await session.commit()

        return {"status": "success", "message": "Activity name updated successfully."}


async def add_activity_log(db: AsyncSession, cat_name: str, user_email: str, activity_name: str,
                           activity_log: category_schemas.ActivityLogs) -> dict:
    async with db as session:
        cat_id = await get_category_id(db=db, cat_name=cat_name)
        user_id = await get_user_id(db=db, user_email=user_email)

        if cat_id is None or user_id is None:
            return {"status": "error", "message": "Category or user is not found."}

        activity_id = await session.execute(
            select(category_models.Activity.id).filter(
                category_models.Activity.id_category == cat_id,
                category_models.Activity.id_user == user_id,
                category_models.Activity.name == activity_name)
        )

        activity_id = activity_id.scalars().first()

        if activity_id is None:
            return {"status": "error", "message": "Activity is not found."}

        try:
            date_log = list(map(int, activity_log.date.split("/")))
        except:
            return {"status": "error", "message": "Incorrect parameters in activity log."}

        new_activity_log = category_models.ActivityLogs(id_activity=activity_id,
                                                        date=date(date_log[0], date_log[1], date_log[2]),
                                                        time_spent=timedelta(seconds=activity_log.time_spent))
        session.add(new_activity_log)
        await session.commit()
        await session.refresh(new_activity_log)
        return {"status": "success", "message": "Activity log added successfully."}
