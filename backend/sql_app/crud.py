from sqlalchemy.ext.asyncio import AsyncSession
from . import models, schemas
import asyncio
from sqlalchemy import select
from sqlalchemy.orm import selectinload
from passlib.context import CryptContext


pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")


def verify_password(plain_password: str, hashed_password: str) -> bool:
    return pwd_context.verify(plain_password, hashed_password)


def hash_password(password: str) -> str:
    return pwd_context.hash(password)


async def get_user(db: AsyncSession, email: str, password: str):
    async with db as session:
        result = await session.execute(
            select(models.User).filter(models.User.email == email, models.User.password == password)
        )
        return result.scalars().first()


async def get_user_by_email(db: AsyncSession, email: str):
    async with db as session:
        result = await session.execute(
            select(models.User).filter(models.User.email == email)
        )
        return result.scalars().first()


async def create_user_db(db: AsyncSession, user: schemas.User):
    password = hash_password(user.password)
    db_user = models.User(login=user.login, email=user.email, password=password)
    async with db as session:
        session.add(db_user)
        await session.commit()
        await session.refresh(db_user)
    return db_user
