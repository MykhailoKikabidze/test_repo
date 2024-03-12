from sqlalchemy.orm import Session
from sqlalchemy.ext.asyncio import AsyncSession
from . import models, schemas
import asyncio
from sqlalchemy import select
from sqlalchemy.orm import selectinload


async def get_user(db: AsyncSession, email: str, password: str):
    async with db as session:
        result = await session.execute(
            select(models.User).filter(models.User.email == email, models.User.password == password)
        )
        return result.scalars().first()


async def create_user_db(db: AsyncSession, user: schemas.User):
    password = user.password
    db_user = models.User(login=user.login, email=user.email, password=password)
    async with db as session:
        session.add(db_user)
        await session.commit()
        await session.refresh(db_user)
    return db_user
