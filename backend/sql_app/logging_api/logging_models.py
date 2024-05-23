from sqlalchemy import Column, Integer, String, ForeignKey
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
    friends1 = relationship("Friends", foreign_keys='Friends.id_user1', back_populates="user1",
                            cascade="all, delete-orphan")
    friends2 = relationship("Friends", foreign_keys='Friends.id_user2', back_populates="user2",
                            cascade="all, delete-orphan")


class Friends(Base):
    __tablename__ = "friends"

    id_user1 = Column(Integer, ForeignKey("users.id", ondelete="CASCADE"), primary_key=True)
    id_user2 = Column(Integer, ForeignKey("users.id", ondelete="CASCADE"), primary_key=True)

    user1 = relationship("User", foreign_keys=[id_user1], back_populates="friends1")
    user2 = relationship("User", foreign_keys=[id_user2], back_populates="friends2")
