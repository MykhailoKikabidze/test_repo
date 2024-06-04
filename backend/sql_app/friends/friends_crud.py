from sqlalchemy.ext.asyncio import AsyncSession
from backend.sql_app.categories import category_crud
from backend.sql_app.logging_api import logging_models
from sqlalchemy import select, or_, and_


async def search_friendship(db: AsyncSession, user1_id: int, user2_id: int) -> logging_models.Friendship:
    async with db as session:
        result = await session.execute(
            select(logging_models.Friendship).filter(
                or_(
                    and_(
                        logging_models.Friendship.id_user1 == user1_id,
                        logging_models.Friendship.id_user2 == user2_id
                    ),
                    and_(
                        logging_models.Friendship.id_user1 == user2_id,
                        logging_models.Friendship.id_user2 == user1_id
                    )
                )
            )
        )

        res = result.scalars().first()
        return res


async def add_friend(db: AsyncSession, user_email: str, friend_email: str) -> dict:
    user_id = await category_crud.get_user_id(db=db, user_email=user_email)
    friend_id = await category_crud.get_user_id(db=db, user_email=friend_email)

    if user_id is None:
        return {"status": "error", "message": "User is not found."}
    elif friend_id is None:
        return {"status": "error", "message": "Friend is not found."}

    is_friendship = await search_friendship(db=db, user1_id=user_id, user2_id=friend_id)

    if is_friendship is not None:
        return {"status": "error", "message": "Friendship already exists."}

    friendship = logging_models.Friendship(id_user1=user_id, id_user2=friend_id)

    async with db as session:
        session.add(friendship)
        await session.commit()
        await session.refresh(friendship)

        return {"status": "success", "message": "Friendship added successfully."}


async def delete_friend(db: AsyncSession, user_email: str, friend_email: str) -> dict:
    user_id = await category_crud.get_user_id(db=db, user_email=user_email)
    friend_id = await category_crud.get_user_id(db=db, user_email=friend_email)

    if user_id is None:
        return {"status": "error", "message": "User is not found."}
    elif friend_id is None:
        return {"status": "error", "message": "Friend is not found."}

    friendship = await search_friendship(db=db, user1_id=user_id, user2_id=friend_id)

    if friendship is not None:
        async with db as session:
            await session.delete(friendship)
            await session.commit()

            return {"status": "success", "message": "Friendship deleted successfully."}
    return {"status": "error", "message": "Friendship is not found."}


async def show_friends(db: AsyncSession, user_email: str) -> list:
    user_id = await category_crud.get_user_id(db=db, user_email=user_email)

    async with db as session:
        result = await session.execute(
            select(logging_models.Friendship).filter(
                or_(
                    logging_models.Friendship.id_user1 == user_id,
                    logging_models.Friendship.id_user2 == user_id
                )
            )
        )

        friendships = result.scalars().all()
        lst_id_friends = []

        for friendship in friendships:
            if friendship.id_user1 == user_id:
                lst_id_friends.append(friendship.id_user2)
            else:
                lst_id_friends.append(friendship.id_user1)

        lst_usernames_friends = []

        for id in lst_id_friends:
            res = await session.execute(
                select(logging_models.User.login).filter(
                    logging_models.User.id == id
                )
            )

            lst_usernames_friends.append(res.scalars().first())

        return lst_usernames_friends
