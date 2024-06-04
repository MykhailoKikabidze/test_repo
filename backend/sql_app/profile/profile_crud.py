from sqlalchemy.ext.asyncio import AsyncSession
from backend.sql_app.profile import profile_models, profile_schemas
from backend.sql_app.logging_api import logging_models, logging_crud
from sqlalchemy import select
from datetime import date
from datetime import datetime
import math


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
    check_user = await logging_crud.get_user_by_email(db=db, email=new_email)

    if check_user is not None:
        return {"status": "error", "message": "User with new email already exists."}

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
            profile = profile_models.Profile(id_user=user.id,
                                             bonus=1,
                                             points=0,
                                             image="",
                                             last_log=date(date_last_log[0],
                                                           date_last_log[1],
                                                           date_last_log[2]))

            async with db as session:
                session.add(profile)
                await session.commit()
                await session.refresh(profile)

                return {"status": "success", "message": "Profile added successfully."}
        return {"status": "error", "message": "Profile already exists."}
    return {"status": "error", "message": "User is not found."}


async def update_profile_last_log(db: AsyncSession, email: str, new_date_log: str):
    profile = await get_profile(db=db, email=email)
    if profile is not None:

        new_date = datetime.strptime(new_date_log, "%Y/%m/%d")

        if isinstance(profile.last_log, datetime):
            last_log_date = profile.last_log
        else:
            last_log_date = datetime.combine(profile.last_log, datetime.min.time())

        difference = (new_date - last_log_date).days

        bonus = profile.bonus
        points = profile.points

        if difference > 1:
            bonus = 1
            points += 2
        elif difference == 1:
            bonus += 0.3
            points += math.floor(2 * bonus)

        date_last_log = list(map(int, new_date_log.split("/")))
        new_profile = profile_models.Profile(id_user=profile.id_user, bonus=bonus,
                                             points=points,
                                             last_log=date(date_last_log[0],
                                                           date_last_log[1],
                                                           date_last_log[2]))

        async with db as session:
            await session.delete(profile)
            session.add(new_profile)
            await session.commit()
            await session.refresh(new_profile)

        return {"status": "success", "message": "Profile last date is changed successfully."}

    return {"status": "error", "message": "User profile is not found."}


async def update_profile_points(db: AsyncSession, email: str, points: int, action: str):
    profile = await get_profile(db=db, email=email)
    if profile is not None:
        if action == "add":
            new_profile = profile_models.Profile(id_user=profile.id_user, bonus=profile.bonus,
                                                 points=profile.points + math.floor(points * profile.bonus),
                                                 last_log=profile.last_log)
        elif action == "divide":
            new_profile = profile_models.Profile(id_user=profile.id_user, bonus=profile.bonus,
                                                 points=profile.points - points,
                                                 last_log=profile.last_log)
        else:
            return {"status": "error", "message": f"Action [{action}] is undefined."}

        async with db as session:
            await session.delete(profile)
            session.add(new_profile)
            await session.commit()
            await session.refresh(new_profile)

        return {"status": "success", "message": "Profile points are changed successfully."}

    return {"status": "error", "message": "User profile is not found."}


async def update_profile_image(db: AsyncSession, email: str, new_image: str):
    profile = await get_profile(db=db, email=email)

    if profile is not None:
        new_profile = profile_models.Profile(id_user=profile.id_user,
                                             last_log=profile.last_log,
                                             bonus=profile.bonus,
                                             points=profile.points,
                                             image=new_image)

        async with db as session:
            await session.delete(profile)
            session.add(new_profile)
            await session.commit()
            await session.refresh(new_profile)

        return {"status": "success", "message": "Profile image is changed successfully."}

    return {"status": "error", "message": "User profile is not found."}
