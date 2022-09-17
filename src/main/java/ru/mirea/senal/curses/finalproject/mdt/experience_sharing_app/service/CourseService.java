package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.Status;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ICourseDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ICourseInfoDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.CourseDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.LessonScheduleDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.CourseEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.CourseInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.IsAlreadyStartedExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces.ICourseService;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util.IMappingUtils;

import javax.persistence.NoResultException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
public class CourseService implements ICourseService {
    private ICourseDAO courseDAO;
    private ICourseInfoDAO courseInfoDAO;
    private IMappingUtils mappingUtils;

    private static final Logger log = Logger.getLogger(CourseService.class);

    public CourseService(ICourseDAO courseDAO, ICourseInfoDAO courseInfoDAO, IMappingUtils mappingUtils) {
        this.courseDAO = courseDAO;
        this.courseInfoDAO = courseInfoDAO;
        this.mappingUtils = mappingUtils;
    }

    @Transactional
    public CourseDTO startCourse(long id, CourseEntity courseEntity) throws DBExeption {
        try {
            CourseInfoEntity courseInfoEntity = courseInfoDAO.findOne(id);
            courseEntity.setCourse(courseInfoEntity);
            courseEntity.setStatus(Status.INACTIVE);
            return mappingUtils.mapToCourseDTO(courseDAO.create(courseEntity));
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db course start operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public CourseDTO updateCourse(CourseEntity courseEntity) throws DBExeption, IsAlreadyStartedExeption {
        try {
            CourseEntity course = courseDAO.findOne(courseEntity.getId());
            if (course.getNoteDate().after(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime())) {
                course.setCapacity(courseEntity.getCapacity());
                course.setNoteDate(courseEntity.getNoteDate());
                return mappingUtils.mapToCourseDTO(courseDAO.update(course));
            } else {
                throw new IsAlreadyStartedExeption();
            }
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db course's reports get operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public Boolean updateCourseStatus(long id, Status status) throws DBExeption {
        try {
            CourseEntity course = courseDAO.findOne(id);
            course.setStatus(status);
            courseDAO.update(course);
            return true;
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db course update operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public Boolean deleteCourse(long id) throws DBExeption, CantDeleteObjectExeption {
        try {
            CourseEntity courseEntity = courseDAO.findOne(id);
            if (courseEntity.getStatus() != Status.ACTIVE) {
                courseDAO.delete(courseEntity);
                return true;
            } else {
                log.warn("input was wrong operation not available to this user");
                throw new CantDeleteObjectExeption();
            }
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db course delete operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public List<LessonScheduleDTO> getCourseSchedule(long id) throws DBExeption {
        try {
            CourseEntity course = courseDAO.findOne(id);
            return course.getLessons()
                    .stream()
                    .map(o -> mappingUtils.mapToLessonScheduleDTO(o))
                    .sorted(Comparator.comparing(obj -> obj.getStartDate()))
                    .collect(Collectors.toList());
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db course's schedule get operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }
}
