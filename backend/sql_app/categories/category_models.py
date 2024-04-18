from sqlalchemy import Column, Integer, String, Date, ForeignKey
from sqlalchemy.orm import relationship
from sqlalchemy.dialects.postgresql import INTERVAL
from backend.sql_app.database import Base


class Category(Base):
    __tablename__ = "categories"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, unique=True, index=True)

    activity = relationship("Activity", back_populates="category", cascade="all, delete-orphan")


class Activity(Base):
    __tablename__ = "activities"

    id = Column(Integer, primary_key=True, index=True)
    id_user = Column(Integer, ForeignKey("users.id", ondelete="CASCADE"))
    id_category = Column(Integer, ForeignKey("categories.id", ondelete="CASCADE"))
    name = Column(String, index=True)

    category = relationship("Category", back_populates="activity")
    user = relationship("User", back_populates="activity")
    activity_logs = relationship("ActivityLogs", back_populates="activity", cascade="all, delete-orphan")


class ActivityLogs(Base):
    __tablename__ = "activity_logs"

    id = Column(Integer, primary_key=True, index=True)
    id_activity = Column(Integer, ForeignKey("activities.id", ondelete="CASCADE"))
    date = Column(Date, index=True)
    time_spent = Column(INTERVAL, index=True)

    activity = relationship("Activity", back_populates="activity_logs")
