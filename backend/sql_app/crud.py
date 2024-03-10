from sqlalchemy.orm import Session
from . import models, schemas
import asyncio


async def get_user_by_email(db: Session, email: str, password: str):
    return db.query(models.User).filter(models.User.email == email and models.User.hashed_password == password).first()


async def create_user(db: Session, user: schemas.User):
    password = user.password
    db_user = models.User(login=user.login, email=user.email, hashed_password=password)
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return db_user
