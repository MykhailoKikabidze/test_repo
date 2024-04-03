from sqlalchemy.ext.asyncio import AsyncSession
from backend.sql_app.categories import category_models
from backend.sql_app.logging_api import logging_models
from sqlalchemy import select


async def get_categories(db: AsyncSession):
    async with db as session:
        result = await session.execute(
            select(category_models.Category)
        )

        return result.scalars().all()


async def get_activity(db: AsyncSession, cat_name: str, user_email: str, activity_name: str):
    async with db as session:
        cat_query = await session.execute(
            select(category_models.Category.id).where(category_models.Category.name == cat_name)
        )
        cat_id = cat_query.scalar_one_or_none()

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
        cat_query = await session.execute(
            select(category_models.Category.id).where(category_models.Category.name == cat_name)
        )
        cat_id = cat_query.scalars().first()

        user_query = await session.execute(
            select(logging_models.User.id).where(logging_models.User.email == user_email)
        )
        user_id = user_query.scalars().first()

        if cat_id is not None and user_id is not None:
            new_activity = category_models.Activity(id_user=user_id, id_category=cat_id, name=activity_name)
            session.add(new_activity)
            await session.commit()
            await session.refresh(new_activity)
            return new_activity

        return None

