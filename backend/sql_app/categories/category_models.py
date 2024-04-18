from sqlalchemy import Column, Integer, String, Date, ForeignKey
from sqlalchemy.orm import relationship
from sqlalchemy.dialects.postgresql import INTERVAL
from backend.sql_app.database import Base


class Category(Base):
    __tablename__ = "categories"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, unique=True, index=True)


class Activity(Base):
    __tablename__ = "activities"

    id = Column(Integer, primary_key=True, index=True)
    id_user = Column(Integer, index=True)
    id_category = Column(Integer, index=True)
    name = Column(String, index=True)

    activity_logs = relationship("ActivityLogs", back_populates="activity", cascade="all, delete-orphan")


class ActivityLogs(Base):
    __tablename__ = "activity_logs"

    id = Column(Integer, primary_key=True, index=True)
    id_activity = Column(Integer, ForeignKey("activities.id", ondelete="CASCADE"))
    date = Column(Date, index=True)
    time_spent = Column(INTERVAL, index=True)

    activity = relationship("Activity", back_populates="activity_logs")
