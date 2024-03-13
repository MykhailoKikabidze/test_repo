from sqlalchemy.ext.asyncio import create_async_engine, AsyncSession
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
import json

with open(r'C:\Users\HP\Desktop\Progs\Test_repo\backend\sql_app\settings.json', 'r') as file:
    data = json.load(file)

SQLALCHEMY_DATABASE_URL = data["credentials"]

engine = create_async_engine(SQLALCHEMY_DATABASE_URL, echo=True)
AsyncSessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine, class_=AsyncSession)

Base = declarative_base()
