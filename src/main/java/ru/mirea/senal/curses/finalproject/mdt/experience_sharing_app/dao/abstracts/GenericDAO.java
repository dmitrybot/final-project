package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts;

import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;

import java.io.Serializable;
import java.util.List;

public interface GenericDAO<T extends Serializable> {
    T findOne(long id);

    List<T> findAll();

    T create(T entity);

    T update(T entity);

    void delete(T entity);

    void deleteById(long entityId) throws CantDeleteObjectExeption;
}
