package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts;

import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

public abstract class AbstractJpaDAO<T extends Serializable> {
    private Class<T> tClass;

    @PersistenceContext
    private EntityManager entityManager;

    public void settClass(final Class<T> clazzToSet) {
        tClass = clazzToSet;
    }

    public T findOne(final long id) {
        return entityManager.find(tClass, id);
    }

    public List<T> findAll() {
        return entityManager.createQuery("from " + tClass.getName()).getResultList();
    }

    public T create(final T entity) {
        entityManager.persist(entity);
        return entity;
    }

    public T update(final T entity) {
        return entityManager.merge(entity);
    }

    public void delete(final T entity) {
        entityManager.remove(entity);
    }

    public void deleteById(final long entityId) throws CantDeleteObjectExeption {
        final T entity = findOne(entityId);
        delete(entity);
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }
}
