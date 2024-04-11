from sqlalchemy import Column, Integer, Date, Numeric
from backend.sql_app.database import Base

class Profile(Base):
    __tablename__ = "profile"

    id_user = Column(Integer, primary_key=True, index=True)
    last_log = Column(Date, index=True)
    bonus = Column(Numeric, index=True)
    points = Column(Integer, index=True)
