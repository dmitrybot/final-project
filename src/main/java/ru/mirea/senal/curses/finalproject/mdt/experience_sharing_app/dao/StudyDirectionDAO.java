package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao;

import org.springframework.stereotype.Repository;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.StudyDirectionEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.AbstractJpaDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IStudyDirectionDAO;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class StudyDirectionDAO
        extends AbstractJpaDAO<StudyDirectionEntity> implements IStudyDirectionDAO {

    public void deleteByTagId(final long tagId) throws CantDeleteObjectExeption {
        try {
            CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<StudyDirectionEntity> criteria = builder.createQuery(StudyDirectionEntity.class);
            Root<StudyDirectionEntity> root = criteria.from(StudyDirectionEntity.class);
            criteria.select(root).where(builder.equal(root.get("tag"), tagId));
            final StudyDirectionEntity entity = getEntityManager().createQuery(criteria).getSingleResult();
            delete(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
