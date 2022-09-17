package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces;

import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.LessonInfoDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.LessonInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.ProductInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;

import java.util.List;

public interface ILessonInfoService {
    LessonInfoDTO createLessonForCourse(long id, LessonInfoEntity lessonInfoEntity) throws DBExeption;
    Boolean createLessonsForCourse(long id, List<LessonInfoEntity> lessons) throws DBExeption;
    LessonInfoDTO createLesson(LessonInfoEntity lesson, Long userId) throws DBExeption;
    LessonInfoDTO updateLesson(ProductInfoEntity productInfoEntity, long lessonId) throws DBExeption;
    Boolean deleteLesson(long id, long userId) throws DBExeption, CantDeleteObjectExeption;
}
