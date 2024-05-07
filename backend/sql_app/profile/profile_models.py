from sqlalchemy import Column, Integer, Date, REAL, ForeignKey
from sqlalchemy.orm import relationship
from backend.sql_app.database import Base

class Profile(Base):
    __tablename__ = "profile"

    id_user = Column(Integer, ForeignKey("users.id", ondelete="CASCADE"), primary_key=True)
    last_log = Column(Date, index=True)
    bonus = Column(REAL, index=True)
    points = Column(Integer, index=True)

    user = relationship("User", back_populates="profile")
