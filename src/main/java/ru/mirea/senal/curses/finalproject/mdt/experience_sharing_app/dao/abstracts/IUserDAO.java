package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts;

import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.UserEntity;

public interface IUserDAO extends GenericDAO<UserEntity> {
    UserEntity findByEmail(String email);
}