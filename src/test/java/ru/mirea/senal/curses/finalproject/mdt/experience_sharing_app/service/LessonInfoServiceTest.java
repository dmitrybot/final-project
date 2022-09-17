package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.ProductType;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.Status;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.LessonInfoDAO;
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
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util.IMappingUtils;

import javax.persistence.NoResultException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class LessonInfoServiceTest {
    @Mock
    private ILessonInfoDAO lessonInfoDAO;
    @Mock
    private ICourseInfoDAO courseInfoDAO;
    @Mock
    private IUserDAO userDAO;
    @Mock
    private IMappingUtils mappingUtils;
    @InjectMocks
    private LessonInfoService lessonInfoService;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createLessonForCourse_positive() {
        UserEntity userEntity = mock(UserEntity.class);
        CourseInfoEntity courseInfoEntity = mock(CourseInfoEntity.class);
        LessonInfoEntity lessonInfoEntityInput = mock(LessonInfoEntity.class);
        LessonInfoEntity lessonInfoEntityOutput = mock(LessonInfoEntity.class);
        LessonInfoDTO lessonInfoDTO = mock(LessonInfoDTO.class);
        ProductInfoEntity productInfoEntityLesson = mock(ProductInfoEntity.class);
        ProductInfoEntity productInfoEntityCourse = mock(ProductInfoEntity.class);

        when(courseInfoDAO.findOne(anyLong())).thenReturn(courseInfoEntity);
        when(lessonInfoEntityInput.getProductInfoEntity()).thenReturn(productInfoEntityLesson);
        when(courseInfoEntity.getProductInfoEntity()).thenReturn(productInfoEntityCourse);
        when(productInfoEntityCourse.getAuthor()).thenReturn(userEntity);
        when(lessonInfoDAO.create(lessonInfoEntityInput)).thenReturn(lessonInfoEntityOutput);
        when(mappingUtils.mapToLessonInfoDTO(lessonInfoEntityOutput)).thenReturn(lessonInfoDTO);

        assertEquals(lessonInfoDTO, lessonInfoService.createLessonForCourse(1L, lessonInfoEntityInput));

        verify(courseInfoDAO).findOne(anyLong());
        verify(lessonInfoEntityInput).setCourse(courseInfoEntity);
        verify(lessonInfoEntityInput, times(3)).getProductInfoEntity();
        verify(courseInfoEntity).getProductInfoEntity();
        verify(productInfoEntityCourse).getAuthor();
        verify(productInfoEntityLesson).setAuthor(userEntity);
        verify(productInfoEntityLesson).setStatus(any());
        verify(productInfoEntityLesson).setProductType(any());
        verify(lessonInfoDAO).create(lessonInfoEntityInput);
        verify(mappingUtils).mapToLessonInfoDTO(any());
    }

    @Test
    void createLessonForCourse_negative_error_with_db() {
        LessonInfoEntity lessonInfoEntityInput = mock(LessonInfoEntity.class);

        when(courseInfoDAO.findOne(anyLong())).thenThrow(NullPointerException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            lessonInfoService.createLessonForCourse(1L, lessonInfoEntityInput);
        }, "Cant find data in db");

        verify(courseInfoDAO).findOne(anyLong());
    }

    @Test
    void createLessonsForCourse_positive() {
        UserEntity userEntity = mock(UserEntity.class);
        CourseInfoEntity courseInfoEntity = mock(CourseInfoEntity.class);
        List<LessonInfoEntity> lessonInfoEntityList = mock(List.class);
        Set<LessonInfoEntity> lessonInfoEntityListCourse = mock(Set.class);
        LessonInfoEntity lessonInfoEntity = mock(LessonInfoEntity.class);
        LessonInfoEntity lessonInfoEntity1 = mock(LessonInfoEntity.class);
        ProductInfoEntity productInfoEntityLesson = mock(ProductInfoEntity.class);
        ProductInfoEntity productInfoEntityCourse = mock(ProductInfoEntity.class);
        Iterator<LessonInfoEntity> iterator = mock(Iterator.class);
        Iterator<LessonInfoEntity> iterator2 = mock(Iterator.class);

        when(courseInfoDAO.findOne(anyLong())).thenReturn(courseInfoEntity);
        when(courseInfoEntity.getLessons()).thenReturn(lessonInfoEntityListCourse);
        when(lessonInfoEntityListCourse.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, true, true, false);
        when(iterator.next()).thenReturn(lessonInfoEntity1);
        when(lessonInfoEntityList.iterator()).thenReturn(iterator2);
        when(iterator2.hasNext()).thenReturn(true, true, true, false);
        when(iterator2.next()).thenReturn(lessonInfoEntity);
        when(lessonInfoEntity.getProductInfoEntity()).thenReturn(productInfoEntityLesson);
        when(courseInfoEntity.getProductInfoEntity()).thenReturn(productInfoEntityCourse);
        when(productInfoEntityCourse.getAuthor()).thenReturn(userEntity);

        assertEquals(true, lessonInfoService.createLessonsForCourse(1L, lessonInfoEntityList));

        verify(courseInfoDAO).findOne(anyLong());
        verify(lessonInfoDAO, times(3)).update(lessonInfoEntity1);
        verify(lessonInfoDAO, times(3)).create(lessonInfoEntity);
        verify(lessonInfoEntity, times(9)).getProductInfoEntity();
        verify(lessonInfoEntity1, times(3)).setCourse(null);
        verify(productInfoEntityLesson, times(3)).setStatus(Status.INACTIVE);
        verify(productInfoEntityLesson, times(3)).setAuthor(userEntity);
        verify(productInfoEntityLesson, times(3)).setProductType(ProductType.LESSON);
        verify(productInfoEntityCourse, times(3)).getAuthor();
    }

    @Test
    void createLessonsForCourse_negative_error_with_db() {
        List<LessonInfoEntity> lessonInfoEntityList = mock(List.class);;

        when(courseInfoDAO.findOne(anyLong())).thenThrow(NullPointerException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            lessonInfoService.createLessonsForCourse(1L, lessonInfoEntityList);
        }, "Cant find data in db");

        verify(courseInfoDAO).findOne(anyLong());
    }

    @Test
    void createLesson_positive() {
        UserEntity userEntity = mock(UserEntity.class);
        LessonInfoEntity lessonInfoEntity = mock(LessonInfoEntity.class);
        LessonInfoEntity lessonInfoEntityOutput = mock(LessonInfoEntity.class);
        ProductInfoEntity productInfoEntityLesson = mock(ProductInfoEntity.class);
        ProductInfoEntity productInfoEntityCourse = mock(ProductInfoEntity.class);
        LessonInfoDTO lessonInfoDTO = mock(LessonInfoDTO.class);

        when(userDAO.findOne(anyLong())).thenReturn(userEntity);
        when(lessonInfoEntity.getProductInfoEntity()).thenReturn(productInfoEntityLesson);
        when(productInfoEntityCourse.getAuthor()).thenReturn(userEntity);
        when(lessonInfoDAO.create(lessonInfoEntity)).thenReturn(lessonInfoEntityOutput);
        when(mappingUtils.mapToLessonInfoDTO(lessonInfoEntityOutput)).thenReturn(lessonInfoDTO);

        assertEquals(lessonInfoDTO, lessonInfoService.createLesson(lessonInfoEntity, 1L));

        verify(userDAO).findOne(anyLong());
        verify(lessonInfoEntity, times(3)).getProductInfoEntity();
        verify(productInfoEntityLesson).setStatus(Status.INACTIVE);
        verify(productInfoEntityLesson).setAuthor(userEntity);
        verify(productInfoEntityLesson).setProductType(ProductType.LESSON);
        verify(lessonInfoDAO).create(lessonInfoEntity);
        verify(mappingUtils).mapToLessonInfoDTO(lessonInfoEntityOutput);
    }

    @Test
    void createLesson_negative() {
        LessonInfoEntity lessonInfoEntity = mock(LessonInfoEntity.class);

        when(userDAO.findOne(anyLong())).thenThrow(NullPointerException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            lessonInfoService.createLesson(lessonInfoEntity, 1L);
        }, "Cant find data in db");

        verify(userDAO).findOne(anyLong());
    }

    @Test
    void updateLesson_positive() {
        LessonInfoEntity lessonInfoEntityInput = mock(LessonInfoEntity.class);
        LessonInfoEntity lessonInfoEntityOutput = mock(LessonInfoEntity.class);
        ProductInfoEntity productInfoEntityInput = mock(ProductInfoEntity.class);
        ProductInfoEntity productInfoEntityOutput = mock(ProductInfoEntity.class);
        LessonInfoDTO lessonInfoDTO = mock(LessonInfoDTO.class);

        when(lessonInfoDAO.findOne(anyLong())).thenReturn(lessonInfoEntityInput);
        when(lessonInfoEntityInput.getProductInfoEntity()).thenReturn(productInfoEntityOutput);
        when(lessonInfoDAO.update(lessonInfoEntityInput)).thenReturn(lessonInfoEntityOutput);
        when(mappingUtils.mapToLessonInfoDTO(lessonInfoEntityOutput)).thenReturn(lessonInfoDTO);

        assertEquals(lessonInfoDTO, lessonInfoService.updateLesson(productInfoEntityInput, 1L));

        verify(lessonInfoDAO).findOne(anyLong());
        verify(lessonInfoEntityInput).getProductInfoEntity();
        verify(productInfoEntityOutput).update(productInfoEntityInput);
        verify(lessonInfoDAO).update(lessonInfoEntityInput);
        verify(mappingUtils).mapToLessonInfoDTO(lessonInfoEntityOutput);
    }

    @Test
    void updateLesson_negative() {
        ProductInfoEntity productInfoEntityInput = mock(ProductInfoEntity.class);
        LessonInfoDTO lessonInfoDTO = mock(LessonInfoDTO.class);

        when(lessonInfoDAO.findOne(anyLong())).thenThrow(NullPointerException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            lessonInfoService.updateLesson(productInfoEntityInput, 1L);
        }, "Cant find data in db");

        verify(lessonInfoDAO).findOne(anyLong());
    }

    @Test
    void deleteLesson_positive_delete_was_successful_by_author() {
        UserEntity userEntity = mock(UserEntity.class);
        UserEntity author = mock(UserEntity.class);
        LessonInfoEntity lessonInfoEntity = mock(LessonInfoEntity.class);
        ProductInfoEntity productInfoEntity = mock(ProductInfoEntity.class);

        when(userDAO.findOne(anyLong())).thenReturn(userEntity);
        when(lessonInfoDAO.findOne(anyLong())).thenReturn(lessonInfoEntity);
        when(lessonInfoEntity.getProductInfoEntity()).thenReturn(productInfoEntity);
        when(productInfoEntity.getStatus()).thenReturn(Status.INACTIVE);
        when(productInfoEntity.getAuthor()).thenReturn(author);
        when(author.getId()).thenReturn(1L);

        assertEquals(true, lessonInfoService.deleteLesson(anyLong(), 1L));
        verify(userDAO).findOne(anyLong());
        verify(lessonInfoDAO).findOne(anyLong());
        verify(lessonInfoEntity, times(2)).getProductInfoEntity();
        verify(productInfoEntity).getStatus();
        verify(productInfoEntity).getAuthor();
        verify(author).getId();
        verify(lessonInfoDAO).delete(lessonInfoEntity);
    }

    @Test
    void deleteLesson_positive_delete_was_successful_by_admin() {
        UserEntity userEntity = mock(UserEntity.class);
        UserEntity author = mock(UserEntity.class);
        LessonInfoEntity lessonInfoEntity = mock(LessonInfoEntity.class);
        ProductInfoEntity productInfoEntity = mock(ProductInfoEntity.class);

        when(userDAO.findOne(anyLong())).thenReturn(userEntity);
        when(lessonInfoDAO.findOne(anyLong())).thenReturn(lessonInfoEntity);
        when(lessonInfoEntity.getProductInfoEntity()).thenReturn(productInfoEntity);
        when(productInfoEntity.getStatus()).thenReturn(Status.INACTIVE);
        when(productInfoEntity.getAuthor()).thenReturn(author);
        when(author.getId()).thenReturn(2L);
        when(userEntity.getRole()).thenReturn(Role.ADMIN);

        assertEquals(true, lessonInfoService.deleteLesson(anyLong(), 1L));
        verify(userDAO).findOne(anyLong());
        verify(lessonInfoDAO).findOne(anyLong());
        verify(lessonInfoEntity, times(2)).getProductInfoEntity();
        verify(productInfoEntity).getStatus();
        verify(productInfoEntity).getAuthor();
        verify(author).getId();
        verify(userEntity).getRole();
        verify(lessonInfoDAO).delete(lessonInfoEntity);
    }

    @Test
    void deleteLesson_negative_delete_was_rejected_becouse_active_status() {
        UserEntity userEntity = mock(UserEntity.class);
        LessonInfoEntity lessonInfoEntity = mock(LessonInfoEntity.class);
        ProductInfoEntity productInfoEntity = mock(ProductInfoEntity.class);

        when(userDAO.findOne(anyLong())).thenReturn(userEntity);
        when(lessonInfoDAO.findOne(anyLong())).thenReturn(lessonInfoEntity);
        when(lessonInfoEntity.getProductInfoEntity()).thenReturn(productInfoEntity);
        when(productInfoEntity.getStatus()).thenReturn(Status.ACTIVE);

        Assertions.assertThrows(CantDeleteObjectExeption.class, () -> {
            lessonInfoService.deleteLesson(anyLong(), 1L);
        }, "Cant delete this object becouse of status");
        verify(userDAO).findOne(anyLong());
        verify(lessonInfoDAO).findOne(anyLong());
        verify(lessonInfoEntity, times(1)).getProductInfoEntity();
        verify(productInfoEntity).getStatus();
    }

    @Test
    void deleteLesson_negative_delete_was_rejected_becouse_user_rules() {
        UserEntity userEntity = mock(UserEntity.class);
        UserEntity author = mock(UserEntity.class);
        LessonInfoEntity lessonInfoEntity = mock(LessonInfoEntity.class);
        ProductInfoEntity productInfoEntity = mock(ProductInfoEntity.class);

        when(userDAO.findOne(anyLong())).thenReturn(userEntity);
        when(lessonInfoDAO.findOne(anyLong())).thenReturn(lessonInfoEntity);
        when(lessonInfoEntity.getProductInfoEntity()).thenReturn(productInfoEntity);
        when(productInfoEntity.getStatus()).thenReturn(Status.INACTIVE);
        when(productInfoEntity.getAuthor()).thenReturn(author);
        when(author.getId()).thenReturn(2L);
        when(userEntity.getRole()).thenReturn(Role.USER);

        Assertions.assertThrows(CantDeleteObjectExeption.class, () -> {
            lessonInfoService.deleteLesson(anyLong(), 1L);
        }, "Cant delete this object, user dosent have rules to do it");
        verify(userDAO).findOne(anyLong());
        verify(lessonInfoDAO).findOne(anyLong());
        verify(lessonInfoEntity, times(2)).getProductInfoEntity();
        verify(productInfoEntity).getStatus();
        verify(productInfoEntity).getAuthor();
        verify(author).getId();
        verify(userEntity).getRole();
    }

    @Test
    void deleteLesson_negative_delete_was_rejected_becouse_NoResultExeption() {

        when(userDAO.findOne(anyLong())).thenThrow(NoResultException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            lessonInfoService.deleteLesson(anyLong(), 1L);
        }, "Cant delete this object, error while finding data in database");

        verify(userDAO).findOne(anyLong());
    }
}