package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts;

import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.StudyDirectionEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;

public interface IStudyDirectionDAO extends GenericDAO<StudyDirectionEntity> {
    void deleteByTagId(long tagId) throws CantDeleteObjectExeption;
}