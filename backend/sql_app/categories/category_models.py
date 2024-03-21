from sqlalchemy import Column, Integer, String, Date, Interval
from backend.sql_app.database import Base


class Category(Base):
    __tablename__ = "categories"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, unique=True, index=True)


class Activity(Base):
    __tablename__ = "activities"

    id = Column(Integer, primary_key=True, index=True)
    id_user = Column(Integer, primary_key=True, index=True)
    id_category = Column(Integer, primary_key=True, index=True)
    name = Column(String, index=True)


class ActivityLogs(Base):
    __tablename__ = "activity_logs"

    id = Column(Integer, primary_key=True, index=True)
    id_activity = Column(Integer, primary_key=True, index=True)
    date = Column(Date, index=True)
    time_spent = Column(Interval, index=True)
