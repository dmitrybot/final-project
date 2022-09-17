package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao;

import org.springframework.stereotype.Repository;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.UserEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.AbstractJpaDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IUserDAO;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class UserDAO
        extends AbstractJpaDAO<UserEntity> implements IUserDAO {

    public UserEntity findByEmail(final String email) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UserEntity> criteria = builder.createQuery(UserEntity.class);
        Root<UserEntity> root = criteria.from(UserEntity.class);
        criteria.select(root).where(builder.equal(root.get("email"), email));
        return getEntityManager().createQuery(criteria).getSingleResult();
    }
}
