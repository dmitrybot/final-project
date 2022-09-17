package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao;

import org.springframework.stereotype.Repository;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.AbstractJpaDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ILessonInfoDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.LessonInfoEntity;

@Repository
public class LessonInfoDAO
        extends AbstractJpaDAO<LessonInfoEntity> implements ILessonInfoDAO {
}
