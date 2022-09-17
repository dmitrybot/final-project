package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.Status;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ICourseInfoDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IUserDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.CourseInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.ProductInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.UserEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.security.Role;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces.ICourseInfoService;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util.IMappingUtils;

import javax.persistence.NoResultException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CourseInfoServiceTest {

    @Mock
    private ICourseInfoDAO courseInfoDAO;
    @Mock
    private IUserDAO userDAO;
    @Mock
    private IMappingUtils mappingUtils;
    @InjectMocks
    private CourseInfoService courseInfoService;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createCourse_positive_course_was_created() {
        UserEntity userEntity = mock(UserEntity.class);
        CourseInfoEntity courseInfoEntityInput = mock(CourseInfoEntity.class);
        ProductInfoEntity productInfoEntity = mock(ProductInfoEntity.class);
        CourseInfoEntity courseInfoEntityOutput = mock(CourseInfoEntity.class);

        when(userDAO.findOne(anyLong())).thenReturn(userEntity);
        when(courseInfoEntityInput.getProductInfoEntity()).thenReturn(productInfoEntity);
        when(courseInfoDAO.create(any())).thenReturn(courseInfoEntityOutput);

        courseInfoService.createCourse(courseInfoEntityInput, anyLong());
        verify(userDAO).findOne(anyLong());
        verify(courseInfoEntityInput, times(3)).getProductInfoEntity();
        verify(courseInfoDAO).create(any());
        verify(productInfoEntity).setAuthor(any());
        verify(productInfoEntity).setStatus(any());
        verify(productInfoEntity).setProductType(any());
        verify(mappingUtils).mapToCourseInfoDTO(any());
    }

    @Test
    void createCourse_negative_course_creation_was_interrupted_becouse_of_null_user() {
        UserEntity userEntity = mock(UserEntity.class);
        CourseInfoEntity courseInfoEntityInput = mock(CourseInfoEntity.class);
        ProductInfoEntity productInfoEntity = mock(ProductInfoEntity.class);

        when(userDAO.findOne(anyLong())).thenReturn(userEntity);
        when(courseInfoEntityInput.getProductInfoEntity()).thenReturn(productInfoEntity);
        when(courseInfoDAO.create(courseInfoEntityInput)).thenThrow(NullPointerException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            courseInfoService.createCourse(courseInfoEntityInput, anyLong());
        }, "Cant find data in db");

        verify(userDAO).findOne(anyLong());
        verify(courseInfoEntityInput, times(3)).getProductInfoEntity();
        verify(productInfoEntity).setAuthor(any());
        verify(productInfoEntity).setStatus(any());
        verify(productInfoEntity).setProductType(any());
        verify(courseInfoDAO).create(any());
    }

    @Test
    void updateCourse_positive_course_was_updated() {
        ProductInfoEntity productInfoEntityInput = mock(ProductInfoEntity.class);
        ProductInfoEntity productInfoEntityOutput = mock(ProductInfoEntity.class);
        CourseInfoEntity courseInfoEntityOutput = mock(CourseInfoEntity.class);
        CourseInfoEntity courseInfoEntityDbOutput = mock(CourseInfoEntity.class);

        when(courseInfoDAO.findOne(anyLong())).thenReturn(courseInfoEntityOutput);
        when(courseInfoEntityOutput.getProductInfoEntity()).thenReturn(productInfoEntityOutput);
        when(courseInfoDAO.update(any())).thenReturn(courseInfoEntityDbOutput);

        courseInfoService.updateCourse(productInfoEntityInput, anyLong());
        verify(courseInfoDAO).findOne(anyLong());
        verify(courseInfoEntityOutput).getProductInfoEntity();
        verify(productInfoEntityOutput).update(productInfoEntityInput);
        verify(courseInfoDAO).update(courseInfoEntityOutput);
        verify(mappingUtils).mapToCourseInfoDTO(courseInfoEntityDbOutput);
    }

    @Test
    void updateCourse_negative_course_or_product_id_was_wrong_NoResultExeption() {
        ProductInfoEntity productInfoEntityInput = mock(ProductInfoEntity.class);
        ProductInfoEntity productInfoEntityOutput = mock(ProductInfoEntity.class);
        CourseInfoEntity courseInfoEntityOutput = mock(CourseInfoEntity.class);

        when(courseInfoDAO.findOne(anyLong())).thenReturn(courseInfoEntityOutput);
        when(courseInfoEntityOutput.getProductInfoEntity()).thenReturn(productInfoEntityOutput);
        when(courseInfoDAO.update(any())).thenThrow(NoResultException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            courseInfoService.updateCourse(productInfoEntityInput, anyLong());
        }, "Cant find data in db");

        verify(courseInfoDAO).findOne(anyLong());
        verify(courseInfoEntityOutput).getProductInfoEntity();
        verify(productInfoEntityOutput).update(productInfoEntityInput);
        verify(courseInfoDAO).update(courseInfoEntityOutput);
    }

    @Test
    void deleteCourse_positive_delete_was_successful_by_author() {
        UserEntity userEntity = mock(UserEntity.class);
        UserEntity author = mock(UserEntity.class);
        CourseInfoEntity courseInfoEntity = mock(CourseInfoEntity.class);
        ProductInfoEntity productInfoEntity = mock(ProductInfoEntity.class);

        when(userDAO.findOne(anyLong())).thenReturn(userEntity);
        when(courseInfoDAO.findOne(anyLong())).thenReturn(courseInfoEntity);
        when(courseInfoEntity.getProductInfoEntity()).thenReturn(productInfoEntity);
        when(productInfoEntity.getStatus()).thenReturn(Status.INACTIVE);
        when(productInfoEntity.getAuthor()).thenReturn(author);
        when(author.getId()).thenReturn(1L);
        //when(userEntity.getRole()).thenReturn(Role.ADMIN);

        assertEquals(true, courseInfoService.deleteCourse(anyLong(), 1L));
        verify(userDAO).findOne(anyLong());
        verify(courseInfoDAO).findOne(anyLong());
        verify(courseInfoEntity, times(2)).getProductInfoEntity();
        verify(productInfoEntity).getStatus();
        verify(productInfoEntity).getAuthor();
        verify(author).getId();
        //verify(userEntity).getRole();
        verify(courseInfoDAO).delete(courseInfoEntity);
    }

    @Test
    void deleteCourse_positive_delete_was_successful_by_admin() {
        UserEntity userEntity = mock(UserEntity.class);
        UserEntity author = mock(UserEntity.class);
        CourseInfoEntity courseInfoEntity = mock(CourseInfoEntity.class);
        ProductInfoEntity productInfoEntity = mock(ProductInfoEntity.class);

        when(userDAO.findOne(anyLong())).thenReturn(userEntity);
        when(courseInfoDAO.findOne(anyLong())).thenReturn(courseInfoEntity);
        when(courseInfoEntity.getProductInfoEntity()).thenReturn(productInfoEntity);
        when(productInfoEntity.getStatus()).thenReturn(Status.INACTIVE);
        when(productInfoEntity.getAuthor()).thenReturn(author);
        when(author.getId()).thenReturn(2L);
        when(userEntity.getRole()).thenReturn(Role.ADMIN);

        assertEquals(true, courseInfoService.deleteCourse(anyLong(), 1L));
        verify(userDAO).findOne(anyLong());
        verify(courseInfoDAO).findOne(anyLong());
        verify(courseInfoEntity, times(2)).getProductInfoEntity();
        verify(productInfoEntity).getStatus();
        verify(productInfoEntity).getAuthor();
        verify(author).getId();
        verify(userEntity).getRole();
        verify(courseInfoDAO).delete(courseInfoEntity);
    }

    @Test
    void deleteCourse_negative_delete_was_rejected_becouse_active_status() {
        UserEntity userEntity = mock(UserEntity.class);
        CourseInfoEntity courseInfoEntity = mock(CourseInfoEntity.class);
        ProductInfoEntity productInfoEntity = mock(ProductInfoEntity.class);

        when(userDAO.findOne(anyLong())).thenReturn(userEntity);
        when(courseInfoDAO.findOne(anyLong())).thenReturn(courseInfoEntity);
        when(courseInfoEntity.getProductInfoEntity()).thenReturn(productInfoEntity);
        when(productInfoEntity.getStatus()).thenReturn(Status.ACTIVE);

        Assertions.assertThrows(CantDeleteObjectExeption.class, () -> {
            courseInfoService.deleteCourse(anyLong(), 1L);
        }, "Cant delete this object becouse of status");
        verify(userDAO).findOne(anyLong());
        verify(courseInfoDAO).findOne(anyLong());
        verify(courseInfoEntity, times(1)).getProductInfoEntity();
        verify(productInfoEntity).getStatus();
    }

    @Test
    void deleteCourse_negative_delete_was_rejected_becouse_user_rules() {
        UserEntity userEntity = mock(UserEntity.class);
        UserEntity author = mock(UserEntity.class);
        CourseInfoEntity courseInfoEntity = mock(CourseInfoEntity.class);
        ProductInfoEntity productInfoEntity = mock(ProductInfoEntity.class);

        when(userDAO.findOne(anyLong())).thenReturn(userEntity);
        when(courseInfoDAO.findOne(anyLong())).thenReturn(courseInfoEntity);
        when(courseInfoEntity.getProductInfoEntity()).thenReturn(productInfoEntity);
        when(productInfoEntity.getStatus()).thenReturn(Status.INACTIVE);
        when(productInfoEntity.getAuthor()).thenReturn(author);
        when(author.getId()).thenReturn(2L);
        when(userEntity.getRole()).thenReturn(Role.USER);

        Assertions.assertThrows(CantDeleteObjectExeption.class, () -> {
            courseInfoService.deleteCourse(anyLong(), 1L);
        }, "Cant delete this object, user dosent have rules to do it");
        verify(userDAO).findOne(anyLong());
        verify(courseInfoDAO).findOne(anyLong());
        verify(courseInfoEntity, times(2)).getProductInfoEntity();
        verify(productInfoEntity).getStatus();
        verify(productInfoEntity).getAuthor();
        verify(author).getId();
        verify(userEntity).getRole();
    }

    @Test
    void deleteCourse_negative_delete_was_rejected_becouse_NoResultExeption() {

        when(userDAO.findOne(anyLong())).thenThrow(NoResultException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            courseInfoService.deleteCourse(anyLong(), 1L);
        }, "Cant delete this object, error while finding data in database");

        verify(userDAO).findOne(anyLong());
    }
}