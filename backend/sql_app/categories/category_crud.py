from sqlalchemy.ext.asyncio import AsyncSession
from backend.sql_app.categories import category_models, category_schemas
from backend.sql_app.logging_api import logging_models
from sqlalchemy import select
from datetime import date, timedelta


async def get_categories(db: AsyncSession):
    async with db as session:
        result = await session.execute(
            select(category_models.Category)
        )

        return result.scalars().all()


async def get_user(db: AsyncSession, user_email: str):
    pass


async def get_cat(db: AsyncSession, cat_name: str):
    pass


async def get_activity(db: AsyncSession, cat_name: str, user_email: str, activity_name: str):
    async with db as session:
        # Separate function
        cat_query = await session.execute(
            select(category_models.Category.id).where(category_models.Category.name == cat_name)
        )
        cat_id = cat_query.scalar_one_or_none()

        # Separate function
        user_query = await session.execute(
            select(logging_models.User.id).where(logging_models.User.email == user_email)
        )
        user_id = user_query.scalar_one_or_none()

        if cat_id is not None and user_id is not None:

            result = await session.execute(
                select(category_models.Activity).filter(
                    category_models.Activity.id_category == cat_id,
                    category_models.Activity.id_user == user_id,
                    category_models.Activity.name == activity_name)
            )

            if result.scalars().first() is not None:
                return True

        return False


async def add_activity_by_category(db: AsyncSession, cat_name: str, user_email: str, activity_name: str):
    async with db as session:
        # Separate function
        cat_query = await session.execute(
            select(category_models.Category.id).where(category_models.Category.name == cat_name)
        )
        cat_id = cat_query.scalars().first()

        # Separate function
        user_query = await session.execute(
            select(logging_models.User.id).where(logging_models.User.email == user_email)
        )
        user_id = user_query.scalars().first()

        if cat_id is None or user_id is None:
            return {"status": "error", "message": "Category or user is not found."}

        new_activity = category_models.Activity(id_user=user_id, id_category=cat_id, name=activity_name)
        session.add(new_activity)
        await session.commit()
        await session.refresh(new_activity)
        return {"status": "success", "message": "Activity added successfully."}



async def delete_activity(db: AsyncSession, cat_name: str, user_email: str, activity_name: str):
    async with db as session:
        # Separate function
        cat_query = await session.execute(
            select(category_models.Category.id).where(category_models.Category.name == cat_name)
        )
        cat_id = cat_query.scalars().first()

        # Separate function
        user_query = await session.execute(
            select(logging_models.User.id).where(logging_models.User.email == user_email)
        )
        user_id = user_query.scalars().first()

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


async def update_activity(db: AsyncSession, cat_name: str, user_email: str, activity_name: str, new_activity_name: str):
    async with db as session:
        # Separate function
        cat_query = await session.execute(
            select(category_models.Category.id).where(category_models.Category.name == cat_name)
        )
        cat_id = cat_query.scalars().first()

        # Separate function
        user_query = await session.execute(
            select(logging_models.User.id).where(logging_models.User.email == user_email)
        )
        user_id = user_query.scalars().first()

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


async def add_activity_log(db: AsyncSession, cat_name: str, user_email: str, activity_name: str, activity_log: category_schemas.ActivityLogs):
    async with db as session:
        # Separate function
        cat_query = await session.execute(
            select(category_models.Category.id).where(category_models.Category.name == cat_name)
        )
        cat_id = cat_query.scalars().first()

        # Separate function
        user_query = await session.execute(
            select(logging_models.User.id).where(logging_models.User.email == user_email)
        )
        user_id = user_query.scalars().first()

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

        new_activity_log = category_models.ActivityLogs(id_activity=activity_id, date=date(date_log[0], date_log[1], date_log[2]), time_spent=timedelta(seconds=activity_log.time_spent))
        session.add(new_activity_log)
        await session.commit()
        await session.refresh(new_activity_log)
        return {"status": "success", "message": "Activity log added successfully."}



