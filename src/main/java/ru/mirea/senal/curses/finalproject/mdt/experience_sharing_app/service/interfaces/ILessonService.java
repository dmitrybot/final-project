package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces;

import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.Status;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.LessonDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.LessonEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.IsAlreadyStartedExeption;

public interface ILessonService {
    LessonDTO startLessonForCourse(long courseId, LessonEntity lessonEntity, long lessonInfoId) throws DBExeption;
    LessonDTO startLesson(LessonEntity lesson, long lessonInfoId) throws DBExeption;
    LessonDTO updateLesson(LessonEntity lesson) throws DBExeption, IsAlreadyStartedExeption;
    Boolean updateLessonStatus(long id, Status status) throws DBExeption;
    Boolean deleteLesson(long id) throws DBExeption, CantDeleteObjectExeption;
}
