package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.Status;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ICourseDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ICourseInfoDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ILessonDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ILessonInfoDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.LessonDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.LessonInfoDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.*;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.IsAlreadyStartedExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.WrongIdExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util.IMappingUtils;

import javax.persistence.NoResultException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class LessonServiceTest {

    @Mock
    private ILessonInfoDAO lessonInfoDAO;
    @Mock
    private ILessonDAO lessonDAO;
    @Mock
    private ICourseDAO courseDAO;
    @Mock
    private IMappingUtils mappingUtils;
    @InjectMocks
    private LessonService lessonService;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void startLessonForCourse_positive() {
        LessonEntity lessonEntity = mock(LessonEntity.class);
        LessonEntity lessonEntityOutput = mock(LessonEntity.class);
        LessonInfoEntity lessonInfoEntity = mock(LessonInfoEntity.class);
        CourseEntity courseEntity = mock(CourseEntity.class);
        LessonDTO lessonDTO = mock(LessonDTO.class);

        when(courseDAO.findOne(anyLong())).thenReturn(courseEntity);
        when(lessonInfoDAO.findOne(anyLong())).thenReturn(lessonInfoEntity);
        when(lessonDAO.create(lessonEntity)).thenReturn(lessonEntityOutput);
        when(mappingUtils.mapToLessonDTO(lessonEntityOutput)).thenReturn(lessonDTO);

        assertEquals(lessonDTO, lessonService.startLessonForCourse(1L, lessonEntity, 1L));

        verify(lessonInfoDAO).findOne(anyLong());
        verify(courseDAO).findOne(anyLong());
        verify(lessonEntity).setCourse(courseEntity);
        verify(lessonEntity).setLessonInfo(lessonInfoEntity);
        verify(lessonEntity).setStatus(Status.INACTIVE);
        verify(lessonDAO).create(lessonEntity);
        verify(mappingUtils).mapToLessonDTO(lessonEntityOutput);
    }

    @Test
    void startLessonForCourse_negative_error_with_db() {
        LessonInfoEntity lessonInfoEntity = mock(LessonInfoEntity.class);
        LessonEntity lessonEntity = mock(LessonEntity.class);

        when(lessonInfoDAO.findOne(anyLong())).thenReturn(lessonInfoEntity);
        when(courseDAO.findOne(anyLong())).thenThrow(NullPointerException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            lessonService.startLessonForCourse(1L, lessonEntity, 1L);
        }, "Cant start this lesson, error while finding data in database");

        verify(lessonInfoDAO).findOne(anyLong());
        verify(courseDAO).findOne(anyLong());
    }

    @Test
    void startLessonForCourse_negative_error_with_id() {
        LessonEntity lessonEntity = mock(LessonEntity.class);

        when(courseDAO.findOne(anyLong())).thenReturn(null);
        when(lessonInfoDAO.findOne(anyLong())).thenReturn(null);


        Assertions.assertThrows(WrongIdExeption.class, () -> {
            lessonService.startLessonForCourse(1L, lessonEntity, 1L);
        }, "Cant start this lesson, error while finding data in database");

        verify(lessonInfoDAO).findOne(anyLong());
        verify(courseDAO).findOne(anyLong());
    }

    @Test
    void startLesson_positive() {
        LessonEntity lessonEntity = mock(LessonEntity.class);
        LessonEntity lessonEntityOutput = mock(LessonEntity.class);
        LessonInfoEntity lessonInfoEntity = mock(LessonInfoEntity.class);
        LessonDTO lessonDTO = mock(LessonDTO.class);

        when(lessonInfoDAO.findOne(anyLong())).thenReturn(lessonInfoEntity);
        when(lessonDAO.create(lessonEntity)).thenReturn(lessonEntityOutput);
        when(mappingUtils.mapToLessonDTO(lessonEntityOutput)).thenReturn(lessonDTO);

        assertEquals(lessonDTO, lessonService.startLesson(lessonEntity, 1L));

        verify(lessonInfoDAO).findOne(anyLong());
        verify(lessonEntity).setLessonInfo(lessonInfoEntity);
        verify(lessonEntity).setStatus(Status.INACTIVE);
        verify(lessonDAO).create(lessonEntity);
        verify(mappingUtils).mapToLessonDTO(lessonEntityOutput);
    }

    @Test
    void startLesson_negative_error_with_db() {
        LessonEntity lessonEntity = mock(LessonEntity.class);

        when(lessonInfoDAO.findOne(anyLong())).thenThrow(NullPointerException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            lessonService.startLesson(lessonEntity, 1L);
        }, "Cant start this lesson, error while finding data in database");

        verify(lessonInfoDAO).findOne(anyLong());
    }

    @Test
    void startLesson_negative_error_with_id() {
        LessonEntity lessonEntity = mock(LessonEntity.class);

        when(lessonInfoDAO.findOne(anyLong())).thenReturn(null);

        Assertions.assertThrows(WrongIdExeption.class, () -> {
            lessonService.startLesson(lessonEntity, 1L);
        }, "Cant start this lesson, error while finding data with such id");

        verify(lessonInfoDAO).findOne(anyLong());
    }

    @Test
    void updateLesson_positive() {
        LessonEntity lessonEntity = mock(LessonEntity.class);
        LessonEntity lessonEntityDb = mock(LessonEntity.class);
        LessonEntity lessonEntityOutput = mock(LessonEntity.class);
        LessonDTO lessonDTO = mock(LessonDTO.class);
        Date date = mock(Date.class);

        when(lessonDAO.findOne(anyLong())).thenReturn(lessonEntityDb);
        when(lessonEntityDb.getStartDate()).thenReturn(date);
        when(lessonEntity.getLessonLink()).thenReturn("");
        when(lessonEntity.getStartDate()).thenReturn(date);
        when(date.after(any())).thenReturn(true);
        when(lessonEntity.getCapacity()).thenReturn(1);
        when(lessonEntity.getMinDonation()).thenReturn(1.0);
        when(lessonEntity.getHomeWork()).thenReturn("");
        when(lessonDAO.update(lessonEntityDb)).thenReturn(lessonEntityOutput);
        when(mappingUtils.mapToLessonDTO(lessonEntityOutput)).thenReturn(lessonDTO);

        assertEquals(lessonDTO, lessonService.updateLesson(lessonEntity));

        verify(lessonDAO).findOne(anyLong());
        verify(lessonEntityDb).getStartDate();
        verify(lessonEntityDb).setLessonLink(any());
        verify(lessonEntityDb).setCapacity(any());
        verify(lessonEntityDb).setMinDonation(any());
        verify(lessonEntityDb).setHomeWork(any());
        verify(lessonEntityDb).setStartDate(date);
        verify(lessonEntity).getLessonLink();
        verify(lessonEntity).getCapacity();
        verify(lessonEntity).getMinDonation();
        verify(lessonEntity).getHomeWork();
        verify(lessonEntity).getStartDate();
        verify(date).after(any());
        verify(lessonDAO).update(lessonEntityDb);
        verify(mappingUtils).mapToLessonDTO(lessonEntityOutput);
    }

    @Test
    void updateLesson_negative_with_error_in_db() {
        LessonEntity lessonEntity = mock(LessonEntity.class);

        when(lessonDAO.findOne(anyLong())).thenThrow(NullPointerException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            lessonService.updateLesson(lessonEntity);
        }, "Cant start this lesson, error while finding data in database");

        verify(lessonDAO).findOne(anyLong());
    }

    @Test
    void updateLesson_negative_with_id_error() {
        LessonEntity lessonEntity = mock(LessonEntity.class);
        LessonEntity lessonEntityDb = mock(LessonEntity.class);
        Date date = mock(Date.class);

        when(lessonDAO.findOne(anyLong())).thenReturn(lessonEntityDb);
        when(lessonEntityDb.getStartDate()).thenReturn(date);
        when(date.after(any())).thenReturn(false);

        Assertions.assertThrows(IsAlreadyStartedExeption.class, () -> {
            lessonService.updateLesson(lessonEntity);
        }, "Cant start this lesson, error while finding data in database");

        verify(lessonDAO).findOne(anyLong());
        verify(lessonEntityDb).getStartDate();
        verify(date).after(any());
    }

    @Test
    void updateLessonStatus_positive_lesson_status_was_updated() {
        LessonEntity lessonEntity = mock(LessonEntity.class);

        when(lessonDAO.findOne(anyLong())).thenReturn(lessonEntity);

        assertEquals(true, lessonService.updateLessonStatus(anyLong(), Status.ACTIVE));
        verify(lessonDAO).findOne(anyLong());
        verify(lessonEntity).setStatus(Status.ACTIVE);
        verify(lessonDAO).update(lessonEntity);
    }

    @Test
    void updateLessonStatus_negative_lesson_id_was_wrong() {
        when(lessonDAO.findOne(anyLong())).thenThrow(NoResultException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            lessonService.updateLessonStatus(anyLong(), Status.INACTIVE);
        }, "Cant update this course's status, wrong id was given");
        verify(lessonDAO).findOne(anyLong());
    }

    @Test
    void deleteLesson_positive_lesson_was_deleted() {
        LessonEntity lessonEntity = mock(LessonEntity.class);

        when(lessonDAO.findOne(anyLong())).thenReturn(lessonEntity);
        when(lessonEntity.getStatus()).thenReturn(Status.INACTIVE);

        assertEquals(true, lessonService.deleteLesson(anyLong()));
        verify(lessonDAO).findOne(anyLong());
        verify(lessonEntity).getStatus();
        verify(lessonDAO).delete(lessonEntity);
    }

    @Test
    void deleteLesson_negative_lesson_cant_be_deleted() {
        LessonEntity lessonEntity = mock(LessonEntity.class);

        when(lessonDAO.findOne(anyLong())).thenReturn(lessonEntity);
        when(lessonEntity.getStatus()).thenReturn(Status.ACTIVE);

        Assertions.assertThrows(CantDeleteObjectExeption.class, () -> {
            lessonService.deleteLesson(anyLong());
        }, "Cant delete this lesson, becouse now it is in work");
        verify(lessonDAO).findOne(anyLong());
        verify(lessonEntity).getStatus();
    }

    @Test
    void deleteLesson_negative_lesson_cant_be_deleted_wrong_id() {
        when(lessonDAO.findOne(anyLong())).thenThrow(NoResultException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            lessonService.deleteLesson(anyLong());
        }, "Cant delete this lesson, wrong id sended");
        verify(lessonDAO).findOne(anyLong());
    }
}