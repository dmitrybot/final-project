package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.PaymentType;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ILessonDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IPaymentDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IProductInfoDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IUserDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ICourseDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.ProductDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.UserDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.LessonEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.PaymentEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.ProductInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.UserEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.CourseEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DontPayForProductExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.InputDataExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.NotEnoughMoneyExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.SubscribeExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces.IUserService;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util.IMappingUtils;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    private IUserDAO userDAO;
    private IMappingUtils mappingUtils;
    private PasswordEncoder passwordEncoder;
    private ILessonDAO lessonDAO;
    private IPaymentDAO paymentDAO;
    private IProductInfoDAO productInfoDAO;
    private ICourseDAO courseDAO;

    private static final Logger log = Logger.getLogger(TagService.class);

    public UserService(IUserDAO userDAO, IMappingUtils mappingUtils, PasswordEncoder passwordEncoder,
                       ILessonDAO lessonDAO, IPaymentDAO paymentDAO, IProductInfoDAO productInfoDAO, ICourseDAO courseDAO) {
        this.userDAO = userDAO;
        this.mappingUtils = mappingUtils;
        this.passwordEncoder = passwordEncoder;
        this.lessonDAO = lessonDAO;
        this.paymentDAO = paymentDAO;
        this.productInfoDAO = productInfoDAO;
        this.courseDAO = courseDAO;
    }

    @Transactional
    public UserDTO updateUser(UserEntity userEntity) throws DBExeption {
        try {
            UserEntity user = userDAO.findOne(userEntity.getId());
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            user.update(userEntity);
            return mappingUtils.mapToUserDTO(userDAO.update(user));
        } catch (NullPointerException e) {
            log.error("db user update operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        } catch (PersistenceException e) {
            log.error("input user update operation failed", e);
            e.printStackTrace();
            throw new InputDataExeption();
        }
    }

    @Transactional
    public UserDTO createUser(UserEntity userEntity) throws DBExeption {
        try {
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            UserEntity user = userDAO.create(userEntity);
            return mappingUtils.mapToUserDTO(user);
        } catch (NullPointerException e) {
            log.error("db user create operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        } catch (PersistenceException e) {
            log.error("input user create operation failed", e);
            e.printStackTrace();
            throw new InputDataExeption();
        }
    }

    @Transactional
    public UserDTO getUser(long id) throws DBExeption {
        try {
            return mappingUtils.mapToUserDTO(userDAO.findOne(id));
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db user get operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public List<ProductDTO> getUserCreatedProducts(long id) throws DBExeption {
        try {
            List<ProductInfoEntity> createdProducts = userDAO.findOne(id).getCreatedProducts();
            return createdProducts
                    .stream()
                    .map(o -> mappingUtils.mapToProductDTO(o))
                    .collect(Collectors.toList());
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db user's created products get operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public List<UserDTO> getSubscribedUsers(long id) throws DBExeption {
        try {
            List<Long> productIds = userDAO.findOne(id).getCreatedProducts()
                    .stream()
                    .map(o -> o.getId())
                    .collect(Collectors.toList());
            if (productIds == null) return new ArrayList<>();
            return paymentDAO.findByFiltration(mappingUtils.mapListLongToString(productIds))
                    .stream()
                    .map(o -> mappingUtils.mapToUserDTO(o.getBuyer()))
                    .collect(Collectors.toList());
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db user's products' subscribed users operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public Boolean makeDonation(long id, long lessonId, double cost) throws DBExeption {
        try {
            UserEntity buyer = userDAO.findOne(id);
            PaymentEntity paymentEntity = mappingUtils.makePaymentEntity();
            LessonEntity lessonEntity = lessonDAO.findOne(lessonId);
            UserEntity seller = lessonEntity.getLessonInfo().getProductInfoEntity().getAuthor();
            if (cost >= lessonEntity.getMinDonation()) {
                paymentEntity.setPaymentDate(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime());
                paymentEntity.setBuyer(buyer);
                paymentEntity.setSeller(seller);
                paymentEntity.setPrice(cost);
                paymentEntity.setPaymentType(PaymentType.DONATION);
                paymentEntity.setProduct(lessonEntity.getLessonInfo().getProductInfoEntity());
                paymentEntity.setProductRealization(lessonEntity.getId());
                paymentEntity.setChecked(false);
                paymentDAO.create(paymentEntity);
                return true;
            } else {
                log.warn("input not enough money to make donation operation");
                throw new NotEnoughMoneyExeption();
            }
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db user's donation operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public Boolean buyProduct(long id, long productId, double cost) throws DBExeption {
        try {
            UserEntity buyer = userDAO.findOne(id);
            PaymentEntity paymentEntity = mappingUtils.makePaymentEntity();
            ProductInfoEntity productInfoEntity = productInfoDAO.findOne(productId);
            if (cost == productInfoEntity.getPrice()) {
                paymentEntity.setPaymentDate(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime());
                paymentEntity.setBuyer(buyer);
                paymentEntity.setSeller(productInfoEntity.getAuthor());
                paymentEntity.setPrice(cost);
                paymentEntity.setPaymentType(PaymentType.SUBSCRIPTION);
                paymentEntity.setProduct(productInfoEntity);
                paymentEntity.setChecked(false);
                paymentDAO.create(paymentEntity);
                return true;
            } else {
                log.warn("input not enough money to buy product " + productId);
                throw new NotEnoughMoneyExeption();
            }
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db user's product purchase operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public Boolean subscribeCourse(long id, long courseId) throws DBExeption {
        try {
            CourseEntity courseEntity = courseDAO.findOne(courseId);
            PaymentEntity paymentEntity = paymentDAO.findByUserProductRealizationId(id, courseEntity.getCourse().getProductInfoEntity().getId());
            if (paymentEntity == null) {
                log.warn("User " + id + " didnt buy course " + courseId);
                throw new DontPayForProductExeption();
            }
            UserEntity user = paymentEntity.getBuyer();
            if (courseEntity.getCapacity() <= courseEntity.getUsers().size() || courseEntity.getNoteDate().before(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime())) {
                log.warn("User " + id + " cant be subscribed to course " + courseId + " now");
                throw new SubscribeExeption();
            }
            user.getCourses().add(courseEntity);
            userDAO.update(user);
            return true;
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db user's subscribe to course operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public Boolean subscribeLesson(long id, long lessonId) throws DBExeption {
        try {
            LessonEntity lessonEntity = lessonDAO.findOne(lessonId);
            PaymentEntity paymentEntity = paymentDAO.findByUserProductRealizationId(id, lessonEntity.getLessonInfo().getProductInfoEntity().getId());
            if (paymentEntity == null) {
                log.warn("User " + id + " didnt buy lesson " + lessonId);
                throw new DontPayForProductExeption();
            }
            UserEntity user = paymentEntity.getBuyer();
            if (lessonEntity.getCapacity() <= lessonEntity.getUsers().size() || lessonEntity.getStartDate().before(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime())) {
                log.warn("User " + id + " cant be subscribed to lesson " + lessonId + " now");
                throw new SubscribeExeption();
            }
            user.getLessons().add(lessonEntity);
            userDAO.update(user);
            return true;
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db user's subscribe to lesson operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public UserEntity findByEmail(String email) throws DBExeption {
        try {
            return userDAO.findByEmail(email);
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db user get by email operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }
}
