from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select, func, and_
from datetime import datetime, timedelta
from backend.sql_app.categories import category_models, category_crud
from typing import Optional


def formatting_interval(inter: timedelta) -> str:
    total_seconds = int(inter.total_seconds())
    hours, remainder = divmod(total_seconds, 3600)
    minutes, seconds = divmod(remainder, 60)
    formatted_time = f"{hours:02}:{minutes:02}:{seconds:02}"

    return formatted_time


async def statictics_activity_day(db: AsyncSession, user_email: str, cat_name: str, activity_name: str) -> Optional[timedelta]:
    act_id = await category_crud.get_activity_id(db=db, cat_name=cat_name,
                                                 user_email=user_email,
                                                 activity_name=activity_name)

    if act_id is None:
        return None

    today = datetime.utcnow().date()
    yesterday = today - timedelta(days=1)

    async with db as session:
        result = await session.execute(
            select(func.sum(category_models.ActivityLogs.time_spent)).filter(
                and_(
                    category_models.ActivityLogs.id_activity == act_id,
                    category_models.ActivityLogs.date >= yesterday
                )
            )
        )

        time_spent = result.scalar()

        return time_spent or timedelta(seconds=0)


async def statictics_activity_weak(db: AsyncSession, user_email: str, cat_name: str, activity_name: str) -> Optional[timedelta]:
    act_id = await category_crud.get_activity_id(db=db, cat_name=cat_name,
                                                 user_email=user_email,
                                                 activity_name=activity_name)

    if act_id is None:
        return None

    today = datetime.utcnow().date()
    yesterday = today - timedelta(days=7)

    async with db as session:
        result = await session.execute(
            select(func.sum(category_models.ActivityLogs.time_spent)).filter(
                and_(
                    category_models.ActivityLogs.id_activity == act_id,
                    category_models.ActivityLogs.date >= yesterday
                )
            )
        )

        time_spent = result.scalar()

        return time_spent or timedelta(seconds=0)


async def statictics_activity_month(db: AsyncSession, user_email: str, cat_name: str, activity_name: str) -> Optional[timedelta]:
    act_id = await category_crud.get_activity_id(db=db, cat_name=cat_name,
                                                 user_email=user_email,
                                                 activity_name=activity_name)

    if act_id is None:
        return None

    today = datetime.utcnow().date()
    start_date = today - timedelta(days=30)

    async with db as session:
        result = await session.execute(
            select(func.sum(category_models.ActivityLogs.time_spent)).filter(
                and_(
                    category_models.ActivityLogs.id_activity == act_id,
                    category_models.ActivityLogs.date >= start_date,
                    category_models.ActivityLogs.date <= today
                )
            )
        )

        time_spent = result.scalar()

        return time_spent or timedelta(seconds=0)


async def statictics_activity_year(db: AsyncSession, user_email: str, cat_name: str, activity_name: str) -> Optional[timedelta]:
    act_id = await category_crud.get_activity_id(db=db, cat_name=cat_name,
                                                 user_email=user_email,
                                                 activity_name=activity_name)

    if act_id is None:
        return None

    today = datetime.utcnow().date()
    start_date = today - timedelta(days=365)

    async with db as session:
        result = await session.execute(
            select(func.sum(category_models.ActivityLogs.time_spent)).filter(
                and_(
                    category_models.ActivityLogs.id_activity == act_id,
                    category_models.ActivityLogs.date >= start_date,
                    category_models.ActivityLogs.date <= today
                )
            )
        )

        time_spent = result.scalar()

        return time_spent or timedelta(seconds=0)


async def statictics_activity_all_time(db: AsyncSession, user_email: str, cat_name: str, activity_name: str) -> Optional[timedelta]:
    act_id = await category_crud.get_activity_id(db=db, cat_name=cat_name,
                                                 user_email=user_email,
                                                 activity_name=activity_name)

    if act_id is None:
        return None

    async with db as session:
        result = await session.execute(
            select(func.sum(category_models.ActivityLogs.time_spent)).filter(
                category_models.ActivityLogs.id_activity == act_id
            )
        )

        time_spent = result.scalar()

        return time_spent or timedelta(seconds=0)


async def statictics_category_day(db: AsyncSession, user_email: str, cat_name: str) -> Optional[timedelta]:
    activities = await category_crud.get_activities(db=db,
                                                    cat_name=cat_name,
                                                    user_email=user_email)

    if activities is None:
        return None

    activities_name = [act.name for act in activities]

    time_spent = timedelta(seconds=0)

    for name in activities_name:
        time_spent += await statictics_activity_day(db=db,
                                                    user_email=user_email,
                                                    cat_name=cat_name,
                                                    activity_name=name)

    return time_spent


async def statictics_category_weak(db: AsyncSession, user_email: str, cat_name: str) -> Optional[timedelta]:
    activities = await category_crud.get_activities(db=db,
                                                    cat_name=cat_name,
                                                    user_email=user_email)

    if activities is None:
        return None

    activities_name = [act.name for act in activities]

    time_spent = timedelta(seconds=0)

    for name in activities_name:
        time_spent += await statictics_activity_weak(db=db,
                                                     user_email=user_email,
                                                     cat_name=cat_name,
                                                     activity_name=name)

    return time_spent


async def statictics_category_month(db: AsyncSession, user_email: str, cat_name: str) -> Optional[timedelta]:
    activities = await category_crud.get_activities(db=db,
                                                    cat_name=cat_name,
                                                    user_email=user_email)

    if activities is None:
        return None

    activities_name = [act.name for act in activities]

    time_spent = timedelta(seconds=0)

    for name in activities_name:
        time_spent += await statictics_activity_month(db=db,
                                                      user_email=user_email,
                                                      cat_name=cat_name,
                                                      activity_name=name)

    return time_spent


async def statictics_category_year(db: AsyncSession, user_email: str, cat_name: str) -> Optional[timedelta]:
    activities = await category_crud.get_activities(db=db,
                                                    cat_name=cat_name,
                                                    user_email=user_email)

    if activities is None:
        return None

    activities_name = [act.name for act in activities]

    time_spent = timedelta(seconds=0)

    for name in activities_name:
        time_spent += await statictics_activity_year(db=db,
                                                     user_email=user_email,
                                                     cat_name=cat_name,
                                                     activity_name=name)

    return time_spent


async def statictics_category_all_time(db: AsyncSession, user_email: str, cat_name: str) -> Optional[timedelta]:
    activities = await category_crud.get_activities(db=db,
                                                    cat_name=cat_name,
                                                    user_email=user_email)

    if activities is None:
        return None

    activities_name = [act.name for act in activities]

    time_spent = timedelta(seconds=0)

    for name in activities_name:
        time_spent += await statictics_activity_all_time(db=db,
                                                         user_email=user_email,
                                                         cat_name=cat_name,
                                                         activity_name=name)

    return time_spent


async def statictics_total_day(db: AsyncSession, user_email: str) -> timedelta:
    categories = await category_crud.get_categories(db=db)

    categories_name = [cat.name for cat in categories]

    time_spent = timedelta(seconds=0)

    for name in categories_name:
        time_spent += await statictics_category_day(db=db,
                                                    user_email=user_email,
                                                    cat_name=name)

    return time_spent


async def statictics_total_weak(db: AsyncSession, user_email: str) -> timedelta:
    categories = await category_crud.get_categories(db=db)

    categories_name = [cat.name for cat in categories]

    time_spent = timedelta(seconds=0)

    for name in categories_name:
        time_spent += await statictics_category_weak(db=db,
                                                     user_email=user_email,
                                                     cat_name=name)

    return time_spent


async def statictics_total_month(db: AsyncSession, user_email: str) -> timedelta:
    categories = await category_crud.get_categories(db=db)

    categories_name = [cat.name for cat in categories]

    time_spent = timedelta(seconds=0)

    for name in categories_name:
        time_spent += await statictics_category_month(db=db,
                                                      user_email=user_email,
                                                      cat_name=name)

    return time_spent


async def statictics_total_year(db: AsyncSession, user_email: str) -> timedelta:
    categories = await category_crud.get_categories(db=db)

    categories_name = [cat.name for cat in categories]

    time_spent = timedelta(seconds=0)

    for name in categories_name:
        time_spent += await statictics_category_year(db=db,
                                                     user_email=user_email,
                                                     cat_name=name)

    return time_spent


async def statictics_total_all_time(db: AsyncSession, user_email: str) -> timedelta:
    categories = await category_crud.get_categories(db=db)

    categories_name = [cat.name for cat in categories]

    time_spent = timedelta(seconds=0)

    for name in categories_name:
        time_spent += await statictics_category_all_time(db=db,
                                                         user_email=user_email,
                                                         cat_name=name)

    return time_spent
