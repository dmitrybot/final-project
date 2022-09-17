package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.ProductType;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.Status;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ICourseInfoDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ILessonInfoDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IUserDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.LessonInfoDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.CourseInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.LessonInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.ProductInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.UserEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.security.Role;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces.ILessonInfoService;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util.IMappingUtils;

import javax.persistence.NoResultException;
import java.util.Iterator;
import java.util.List;

@Service
public class LessonInfoService implements ILessonInfoService {
    private ILessonInfoDAO lessonInfoDAO;
    private IUserDAO userDAO;
    private ICourseInfoDAO courseInfoDAO;
    private IMappingUtils mappingUtils;

    private static final Logger log = Logger.getLogger(LessonInfoService.class);

    public LessonInfoService(ILessonInfoDAO lessonInfoDAO, IUserDAO userDAO, ICourseInfoDAO courseInfoDAO, IMappingUtils mappingUtils) {
        this.lessonInfoDAO = lessonInfoDAO;
        this.userDAO = userDAO;
        this.courseInfoDAO = courseInfoDAO;
        this.mappingUtils = mappingUtils;
    }

    @Transactional
    public LessonInfoDTO createLessonForCourse(long id, LessonInfoEntity lessonInfoEntity) throws DBExeption {
        try {
            CourseInfoEntity courseInfoEntity = courseInfoDAO.findOne(id);
            lessonInfoEntity.setCourse(courseInfoEntity);
            lessonInfoEntity.getProductInfoEntity().setAuthor(courseInfoEntity.getProductInfoEntity().getAuthor());
            lessonInfoEntity.getProductInfoEntity().setProductType(ProductType.LESSON);
            lessonInfoEntity.getProductInfoEntity().setStatus(Status.INACTIVE);
            return mappingUtils.mapToLessonInfoDTO(lessonInfoDAO.create(lessonInfoEntity));
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db lessonInfo for course create operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public Boolean createLessonsForCourse(long id, List<LessonInfoEntity> lessons) throws DBExeption {
        try {
            CourseInfoEntity course = courseInfoDAO.findOne(id);
            for (Iterator<LessonInfoEntity> it = course.getLessons().iterator(); it.hasNext(); ) {
                LessonInfoEntity l = it.next();
                l.setCourse(null);
                lessonInfoDAO.update(l);
            }
            for (Iterator<LessonInfoEntity> it = lessons.iterator(); it.hasNext(); ) {
                LessonInfoEntity l = it.next();
                l.setCourse(course);
                l.getProductInfoEntity().setAuthor(course.getProductInfoEntity().getAuthor());
                l.getProductInfoEntity().setProductType(ProductType.LESSON);
                l.getProductInfoEntity().setStatus(Status.INACTIVE);
                lessonInfoDAO.create(l);
            }
            return true;
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db lessonsInfo for course create operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public LessonInfoDTO createLesson(LessonInfoEntity lesson, Long userId) throws DBExeption {
        try {
            lesson.getProductInfoEntity().setAuthor(userDAO.findOne(userId));
            lesson.getProductInfoEntity().setProductType(ProductType.LESSON);
            lesson.getProductInfoEntity().setStatus(Status.INACTIVE);
            return mappingUtils.mapToLessonInfoDTO(lessonInfoDAO.create(lesson));
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db lessonInfo create operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public LessonInfoDTO updateLesson(ProductInfoEntity productInfoEntity, long lessonId) throws DBExeption {
        try {
            LessonInfoEntity lesson = lessonInfoDAO.findOne(lessonId);
            lesson.getProductInfoEntity().update(productInfoEntity);
            return mappingUtils.mapToLessonInfoDTO(lessonInfoDAO.update(lesson));
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db lessonInfo update operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public Boolean deleteLesson(long id, long userId) throws DBExeption, CantDeleteObjectExeption {
        try {
            UserEntity user = userDAO.findOne(userId);
            LessonInfoEntity lessonInfoEntity = lessonInfoDAO.findOne(id);
            if (lessonInfoEntity.getProductInfoEntity().getStatus() != Status.ACTIVE
                    && (userId == lessonInfoEntity.getProductInfoEntity().getAuthor().getId() || user.getRole() == Role.ADMIN)) {
                lessonInfoDAO.delete(lessonInfoEntity);
            } else {
                log.warn("input was wrong operation not available to this user");
                throw new CantDeleteObjectExeption();
            }
            return true;
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db lessonInfo delete operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }
}
