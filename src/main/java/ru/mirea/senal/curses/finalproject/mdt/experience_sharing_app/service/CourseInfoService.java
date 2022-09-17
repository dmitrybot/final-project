package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.ProductType;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.Status;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ICourseInfoDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IUserDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.CourseInfoDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.CourseInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.ProductInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.UserEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.security.Role;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces.ICourseInfoService;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util.IMappingUtils;

import javax.persistence.NoResultException;

@Service
public class CourseInfoService implements ICourseInfoService {

    private ICourseInfoDAO courseInfoDAO;
    private IUserDAO userDAO;
    private IMappingUtils mappingUtils;

    private static final Logger log = Logger.getLogger(CourseInfoService.class);

    public CourseInfoService(ICourseInfoDAO courseInfoDAO, IUserDAO userDAO, IMappingUtils mappingUtils) {
        this.courseInfoDAO = courseInfoDAO;
        this.userDAO = userDAO;
        this.mappingUtils = mappingUtils;
    }

    @Transactional
    public CourseInfoDTO createCourse(CourseInfoEntity course, Long userId) throws DBExeption {
        try {
            UserEntity userEntity = userDAO.findOne(userId);
            course.getProductInfoEntity().setAuthor(userEntity);
            course.getProductInfoEntity().setStatus(Status.INACTIVE);
            course.getProductInfoEntity().setProductType(ProductType.COURSE);
            return mappingUtils.mapToCourseInfoDTO(courseInfoDAO.create(course));
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db courseInfo creation operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public CourseInfoDTO updateCourse(ProductInfoEntity productInfoEntity, long courseId) throws DBExeption {
        try {
            CourseInfoEntity course = courseInfoDAO.findOne(courseId);
            course.getProductInfoEntity().update(productInfoEntity);
            return mappingUtils.mapToCourseInfoDTO(courseInfoDAO.update(course));
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db courseInfo update operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public Boolean deleteCourse(long id, long userId) throws DBExeption {
        try {
            UserEntity user = userDAO.findOne(userId);
            CourseInfoEntity courseInfoEntity = courseInfoDAO.findOne(id);
            if (courseInfoEntity.getProductInfoEntity().getStatus() != Status.ACTIVE
                    && (userId == courseInfoEntity.getProductInfoEntity().getAuthor().getId() || user.getRole() == Role.ADMIN)) {
                courseInfoDAO.delete(courseInfoEntity);
            } else {
                log.warn("input was wrong operation not available to this user");
                throw new CantDeleteObjectExeption();
            }
            return true;
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db courseInfo delete operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }
}
