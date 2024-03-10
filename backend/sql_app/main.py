from sqlalchemy.ext.asyncio import AsyncSession

from fastapi import Depends, FastAPI, HTTPException
import asyncio

from . import crud, models, schemas
from .database import SessionLocal, engine

models.Base.metadata.create_all(bind=engine)


app = FastAPI()


# Dependency
async def get_db_session() -> AsyncSession:
    async with SessionLocal() as session:
        yield session


@app.get("/users/{user_email}", response_model=schemas.User)
async def read_user(user_email: str, db: AsyncSession = Depends(get_db_session)):
    db_user = crud.get_user_by_email(db, email=user_email)
    if db_user is None:
        raise HTTPException(status_code=404, detail="User not found")
    return db_user


@app.get("/users/", response_model=schemas.User)
async def authorization_user(user: schemas.User, db: AsyncSession = Depends(get_db_session)):
    db_user = crud.get_user_by_email(db, email=user.email, password=user.password)
    if db_user is None:
        raise HTTPException(status_code=404, detail="User not found. Invalid email or password")

    return db_user


@app.post("/users/", response_model=schemas.User)
async def create_user(user: schemas.User, db: AsyncSession = Depends(get_db_session)):
    db_user = await crud.get_user_by_email(db, email=user.email)
    if db_user:
        raise HTTPException(status_code=400, detail="Email already registered")
    return await crud.create_user(db=db, user=user)
