package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao;

import org.springframework.stereotype.Repository;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.LessonEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.AbstractJpaDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ILessonDAO;

@Repository
public class LessonDAO
        extends AbstractJpaDAO<LessonEntity> implements ILessonDAO {

}
