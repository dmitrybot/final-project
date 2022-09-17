package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces;

import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.CourseInfoDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.CourseInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.ProductInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;

public interface ICourseInfoService {
    CourseInfoDTO createCourse(CourseInfoEntity course, Long userId) throws DBExeption;
    CourseInfoDTO updateCourse(ProductInfoEntity productInfoEntity, long courseId) throws DBExeption;
    Boolean deleteCourse(long id, long userId) throws DBExeption, CantDeleteObjectExeption;
}
