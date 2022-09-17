package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.Status;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ICourseDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ILessonDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ILessonInfoDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.LessonDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.CourseEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.LessonEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.LessonInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.IsAlreadyStartedExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.WrongIdExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces.ILessonService;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util.IMappingUtils;

import javax.persistence.NoResultException;
import java.util.Calendar;
import java.util.TimeZone;

@Service
public class LessonService implements ILessonService {
    private ILessonInfoDAO lessonInfoDAO;
    private ILessonDAO lessonDAO;
    private ICourseDAO courseDAO;
    private IMappingUtils mappingUtils;

    private static final Logger log = Logger.getLogger(LessonService.class);

    public LessonService(ILessonInfoDAO lessonInfoDAO, ILessonDAO lessonDAO, ICourseDAO courseDAO, IMappingUtils mappingUtils) {
        this.lessonInfoDAO = lessonInfoDAO;
        this.lessonDAO = lessonDAO;
        this.courseDAO = courseDAO;
        this.mappingUtils = mappingUtils;
    }

    @Transactional
    public LessonDTO startLessonForCourse(long courseId, LessonEntity lessonEntity, long lessonInfoId) throws DBExeption {
        try {
            LessonInfoEntity lessonInfoEntity = lessonInfoDAO.findOne(lessonInfoId);
            CourseEntity courseEntity = courseDAO.findOne(courseId);
            if (lessonInfoEntity == null || courseEntity == null) {
                log.warn("input was wrong id u send doesnt exist in db");
                throw new WrongIdExeption();
            }
            lessonEntity.setLessonInfo(lessonInfoEntity);
            lessonEntity.setCourse(courseEntity);
            lessonEntity.setStatus(Status.INACTIVE);
            return mappingUtils.mapToLessonDTO(lessonDAO.create(lessonEntity));
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db lesson for course start operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public LessonDTO startLesson(LessonEntity lesson, long lessonInfoId) throws DBExeption {
        try {
            LessonInfoEntity lessonInfoEntity = lessonInfoDAO.findOne(lessonInfoId);
            if (lessonInfoEntity != null) lesson.setLessonInfo(lessonInfoEntity);
            else {
                log.warn("input was wrong id u send doesnt exist in db");
                throw new WrongIdExeption();
            }
            lesson.setStatus(Status.INACTIVE);
            return mappingUtils.mapToLessonDTO(lessonDAO.create(lesson));
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db lesson start operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public LessonDTO updateLesson(LessonEntity lesson) throws DBExeption, IsAlreadyStartedExeption {
        try {
            LessonEntity lessonEntity = lessonDAO.findOne(lesson.getId());
            if (lessonEntity.getStartDate().after(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime())) {
                lessonEntity.setLessonLink(lesson.getLessonLink());
                lessonEntity.setHomeWork(lesson.getHomeWork());
                lessonEntity.setStartDate(lesson.getStartDate());
                lessonEntity.setMinDonation(lesson.getMinDonation());
                lessonEntity.setCapacity(lesson.getCapacity());
                return mappingUtils.mapToLessonDTO(lessonDAO.update(lessonEntity));
            } else {
                log.warn("input was wrong operation not available now");
                throw new IsAlreadyStartedExeption();
            }
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db lesson update operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public Boolean updateLessonStatus(long id, Status status) throws DBExeption {
        try {
            LessonEntity lesson = lessonDAO.findOne(id);
            lesson.setStatus(status);
            lessonDAO.update(lesson);
            return true;
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db course update operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public Boolean deleteLesson(long id) throws DBExeption, CantDeleteObjectExeption {
        try {
            LessonEntity lessonEntity = lessonDAO.findOne(id);
            if (lessonEntity.getStatus() != Status.ACTIVE) {
                lessonDAO.delete(lessonEntity);
                return true;
            } else {
                log.warn("input was wrong operation not available to this user");
                throw new CantDeleteObjectExeption();
            }
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db lesson delete operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }
}
