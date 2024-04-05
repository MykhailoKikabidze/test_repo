from sqlalchemy.ext.asyncio import AsyncSession
from backend.sql_app.logging_api import logging_models
from sqlalchemy import select
from passlib.context import CryptContext


pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")


def verify_password(plain_password: str, hashed_password: str) -> bool:
    return pwd_context.verify(plain_password, hashed_password)


def hash_password(password: str) -> str:
    return pwd_context.hash(password)


async def get_user_by_email(db: AsyncSession, email: str):
    async with db as session:
        result = await session.execute(
            select(logging_models.User).filter(logging_models.User.email == email)
        )
        return result.scalars().first()


async def create_user_db(db: AsyncSession, user: logging_models.User):
    password = hash_password(user.password)
    db_user = logging_models.User(login=user.login, email=user.email, password=password)
    async with db as session:
        session.add(db_user)
        await session.commit()
        await session.refresh(db_user)
    return db_user
