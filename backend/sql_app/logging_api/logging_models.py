from sqlalchemy import Column, Integer, String
from sqlalchemy.orm import relationship
from backend.sql_app.database import Base


class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    login = Column(String, index=True)
    email = Column(String, unique=True, index=True)
    password = Column(String)

    activity = relationship("Activity", back_populates="user", cascade="all, delete-orphan")
    profile = relationship("Profile", back_populates="user", cascade="all, delete-orphan")
