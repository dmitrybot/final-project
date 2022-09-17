package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.config;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.CardInfoDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.CourseInfoDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.DirectionSectionDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.ProductInfoDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.CourseDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.LessonDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.LessonInfoDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.PaymentDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.ReportDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.StudyDirectionDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.TagDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.UserDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ICardInfoDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ICourseInfoDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IDirectionSectionDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IProductInfoDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ICourseDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ILessonDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ILessonInfoDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IPaymentDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IReportDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IStudyDirectionDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ITagDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IUserDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.CardInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.CourseInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.DirectionSectionEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.ProductInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.CourseEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.LessonEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.LessonInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.PaymentEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.ReportEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.StudyDirectionEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.TagEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.UserEntity;


@Configuration
public class RepositoryConfig {

    private static final Logger log = Logger.getLogger(RepositoryConfig.class);

    @Bean
    public ICardInfoDAO cardInfoDAO() {
        CardInfoDAO cardInfoDAO = new CardInfoDAO();
        cardInfoDAO.settClass(CardInfoEntity.class);
        log.info("CardInfoDAO bean created and configured");
        return cardInfoDAO;
    }

    @Bean
    public IDirectionSectionDAO directionSectionDAO() {
        DirectionSectionDAO directionSectionDAO = new DirectionSectionDAO();
        directionSectionDAO.settClass(DirectionSectionEntity.class);
        log.info("DirectionSectionDAO bean created and configured");
        return directionSectionDAO;
    }

    @Bean
    public IProductInfoDAO productInfoDAO() {
        ProductInfoDAO productInfoDAO = new ProductInfoDAO();
        productInfoDAO.settClass(ProductInfoEntity.class);
        log.info("ProductInfoDAO bean created and configured");
        return productInfoDAO;
    }

    @Bean
    public ICourseInfoDAO courseInfoDAO() {
        CourseInfoDAO courseInfoDAO = new CourseInfoDAO();
        courseInfoDAO.settClass(CourseInfoEntity.class);
        log.info("CourseInfoDAO bean created and configured");
        return courseInfoDAO;
    }

    @Bean
    public ICourseDAO courseDAO() {
        CourseDAO courseDAO = new CourseDAO();
        courseDAO.settClass(CourseEntity.class);
        log.info("CourseDAO bean created and configured");
        return courseDAO;
    }

    @Bean
    public ILessonDAO lessonDAO() {
        LessonDAO lessonDAO = new LessonDAO();
        lessonDAO.settClass(LessonEntity.class);
        log.info("LessonDAO bean created and configured");
        return lessonDAO;
    }

    @Bean
    public ILessonInfoDAO lessonInfoDAO() {
        LessonInfoDAO lessonInfoDAO = new LessonInfoDAO();
        lessonInfoDAO.settClass(LessonInfoEntity.class);
        log.info("LessonInfoDAO bean created and configured");
        return lessonInfoDAO;
    }

    @Bean
    public IReportDAO reportDAO() {
        ReportDAO reportDAO = new ReportDAO();
        reportDAO.settClass(ReportEntity.class);
        log.info("ReportDAO bean created and configured");
        return reportDAO;
    }

    @Bean
    public IStudyDirectionDAO studyDirectionDAO() {
        StudyDirectionDAO studyDirectionDAO = new StudyDirectionDAO();
        studyDirectionDAO.settClass(StudyDirectionEntity.class);
        log.info("StudyDirectionDAO bean created and configured");
        return studyDirectionDAO;
    }

    @Bean
    public ITagDAO tagDAO() {
        TagDAO tagDAO = new TagDAO();
        tagDAO.settClass(TagEntity.class);
        log.info("TagDAO bean created and configured");
        return tagDAO;
    }

    @Bean
    public IUserDAO userDAO() {
        UserDAO userDAO = new UserDAO();
        userDAO.settClass(UserEntity.class);
        log.info("UserDAO bean created and configured");
        return userDAO;
    }

    @Bean
    public IPaymentDAO paymentDAO() {
        PaymentDAO paymentDAO = new PaymentDAO();
        paymentDAO.settClass(PaymentEntity.class);
        log.info("PaymentDAO bean created and configured");
        return paymentDAO;
    }
}
