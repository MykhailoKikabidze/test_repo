from sqlalchemy.ext.asyncio import AsyncSession
from backend.sql_app.profile import profile_models, profile_schemas
from backend.sql_app.logging_api import logging_models, logging_crud
from sqlalchemy import select
from datetime import date


async def change_login(db: AsyncSession, email: str, new_login: str):
    user = await logging_crud.get_user_by_email(db=db, email=email)
    if user is not None:
        async with db as session:
            user.login = new_login
            session.add(user)
            await session.commit()

            return {"status": "success", "message": "User login updated successfully."}
    return {"status": "error", "message": "User is not found."}


async def change_email(db: AsyncSession, email: str, new_email: str):
    user = await logging_crud.get_user_by_email(db=db, email=email)
    if user is not None:
        async with db as session:
            user.email = new_email
            session.add(user)
            await session.commit()

            return {"status": "success", "message": "User email updated successfully."}
    return {"status": "error", "message": "User is not found."}


async def change_password(db: AsyncSession, email: str, new_password: str):
    user = await logging_crud.get_user_by_email(db=db, email=email)
    if user is not None:
        async with db as session:
            user.password = logging_crud.hash_password(new_password)
            session.add(user)
            await session.commit()

            return {"status": "success", "message": "User password updated successfully."}
    return {"status": "error", "message": "User is not found."}


async def get_profile(db: AsyncSession, email: str):
    async with db as session:
        user = await logging_crud.get_user_by_email(db=db, email=email)

        if user is not None:
            profile = await session.execute(
                select(profile_models.Profile).filter(
                profile_models.Profile.id_user == user.id
            ))

            return profile.scalars().first()

        return None


async def add_default_profile(db: AsyncSession, email: str, last_log: str):
    user = await logging_crud.get_user_by_email(db=db, email=email)
    if user is not None:

        check_profile = await get_profile(db=db, email=email)

        if check_profile is None:

            date_last_log = list(map(int, last_log.split("/")))
            profile = profile_models.Profile(id_user=user.id, bonus=0, points=0, last_log=date(date_last_log[0], date_last_log[1], date_last_log[2]))

            async with db as session:
                session.add(profile)
                await session.commit()
                await session.refresh(profile)

                return {"status": "success", "message": "Profile added successfully."}
        return {"status": "error", "message": "Profile already exists."}
    return {"status": "error", "message": "User is not found."}


async def update_profile_last_log(db: AsyncSession, email: str, new_date_log: str):
    pass


async def update_profile_points(db: AsyncSession, email: str, new_points: int):
    pass


async def get_points(db: AsyncSession, email: str):
    pass
