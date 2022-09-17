package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces;

import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.Status;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.CourseDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.LessonScheduleDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.CourseEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.IsAlreadyStartedExeption;

import java.util.List;

public interface ICourseService {
    CourseDTO startCourse(long id, CourseEntity courseEntity) throws DBExeption;
    CourseDTO updateCourse(CourseEntity courseEntity) throws DBExeption, IsAlreadyStartedExeption;
    Boolean updateCourseStatus(long id, Status status) throws DBExeption;
    Boolean deleteCourse(long id) throws DBExeption, CantDeleteObjectExeption;
    List<LessonScheduleDTO> getCourseSchedule(long id) throws DBExeption;
}
