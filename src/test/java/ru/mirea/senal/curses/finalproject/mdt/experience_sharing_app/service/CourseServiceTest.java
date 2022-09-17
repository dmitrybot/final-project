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
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IUserDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.CourseDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.LessonScheduleDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.*;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.IsAlreadyStartedExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util.IMappingUtils;

import javax.persistence.NoResultException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class CourseServiceTest {

    @Mock
    private ICourseInfoDAO courseInfoDAO;
    @Mock
    private ICourseDAO courseDAO;
    @Mock
    private IMappingUtils mappingUtils;
    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void startCourse_positive_successfully_start_course() {
        CourseEntity courseEntityInput = mock(CourseEntity.class);
        CourseEntity courseEntityOutput = mock(CourseEntity.class);
        CourseDTO courseDTO = mock(CourseDTO.class);
        CourseInfoEntity courseInfoEntity = mock(CourseInfoEntity.class);

        when(courseInfoDAO.findOne(anyLong())).thenReturn(courseInfoEntity);
        when(courseDAO.create(courseEntityInput)).thenReturn(courseEntityOutput);
        when(mappingUtils.mapToCourseDTO(courseEntityOutput)).thenReturn(courseDTO);


        assertEquals(courseDTO, courseService.startCourse(anyLong(), courseEntityInput));
        verify(courseInfoDAO).findOne(anyLong());
        verify(courseEntityInput).setCourse(courseInfoEntity);
        verify(courseEntityInput).setStatus(Status.INACTIVE);
        verify(courseDAO).create(courseEntityInput);
        verify(mappingUtils).mapToCourseDTO(courseEntityOutput);
    }

    @Test
    void startCourse_negative_error_while_working_with_db() {
        CourseEntity courseEntityInput = mock(CourseEntity.class);

        when(courseInfoDAO.findOne(anyLong())).thenThrow(NoResultException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            courseService.startCourse(anyLong(), courseEntityInput);
        }, "Cant start this course, error while finding data in database");
        verify(courseInfoDAO).findOne(anyLong());
    }

    @Test
    void updateCourse_positive_successfuly_update_course() {
        CourseEntity courseEntityInput = mock(CourseEntity.class);
        CourseEntity courseEntityDb = mock(CourseEntity.class);
        CourseEntity courseEntityNewDb = mock(CourseEntity.class);
        CourseDTO courseDTO = mock(CourseDTO.class);
        Date date = mock(Date.class);

        when(courseEntityInput.getId()).thenReturn(1L);
        when(courseDAO.findOne(anyLong())).thenReturn(courseEntityDb);
        when(courseEntityDb.getNoteDate()).thenReturn(date);
        when(date.after(any())).thenReturn(true);
        when(courseEntityInput.getCapacity()).thenReturn(1);
        when(courseEntityInput.getNoteDate()).thenReturn(mock(Date.class));
        when(courseDAO.update(courseEntityDb)).thenReturn(courseEntityNewDb);
        when(mappingUtils.mapToCourseDTO(courseEntityNewDb)).thenReturn(courseDTO);

        assertEquals(courseDTO, courseService.updateCourse(courseEntityInput));
        verify(courseDAO).findOne(anyLong());
        verify(courseEntityInput).getId();
        verify(courseEntityInput).getCapacity();
        verify(courseEntityInput).getNoteDate();
        verify(courseEntityDb).getNoteDate();
        verify(courseEntityDb).setNoteDate(any());
        verify(courseEntityDb).setCapacity(any());
        verify(date).after(any());
        verify(courseDAO).update(courseEntityDb);
        verify(mappingUtils).mapToCourseDTO(courseEntityNewDb);
    }

    @Test
    void updateCourse_negative_course_is_already_started() {
        CourseEntity courseEntityInput = mock(CourseEntity.class);
        CourseEntity courseEntityDb = mock(CourseEntity.class);
        Date date = mock(Date.class);

        when(courseEntityInput.getId()).thenReturn(1L);
        when(courseDAO.findOne(anyLong())).thenReturn(courseEntityDb);
        when(courseEntityDb.getNoteDate()).thenReturn(date);
        when(date.after(any())).thenReturn(false);

        Assertions.assertThrows(IsAlreadyStartedExeption.class, () -> {
            courseService.updateCourse(courseEntityInput);
        }, "Cant update this course, it is already started");
        verify(courseDAO).findOne(anyLong());
        verify(courseEntityInput).getId();
        verify(courseEntityDb).getNoteDate();
        verify(date).after(any());
    }

    @Test
    void updateCourse_negative_wrong_id() {
        CourseEntity courseEntityInput = mock(CourseEntity.class);

        when(courseEntityInput.getId()).thenReturn(1L);
        when(courseDAO.findOne(anyLong())).thenThrow(NoResultException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            courseService.updateCourse(courseEntityInput);
        }, "Cant update this course, wrong id was given");
        verify(courseDAO).findOne(anyLong());
        verify(courseEntityInput).getId();
    }

    @Test
    void updateCourseStatus_positive_course_status_was_updated() {
        CourseEntity courseEntityDb = mock(CourseEntity.class);

        when(courseDAO.findOne(anyLong())).thenReturn(courseEntityDb);

        assertEquals(true, courseService.updateCourseStatus(anyLong(), Status.ACTIVE));
        verify(courseDAO).findOne(anyLong());
        verify(courseEntityDb).setStatus(Status.ACTIVE);
        verify(courseDAO).update(courseEntityDb);
    }

    @Test
    void updateCourseStatus_negative_course_id_was_wrong() {
        CourseEntity courseEntityDb = mock(CourseEntity.class);

        when(courseDAO.findOne(anyLong())).thenThrow(NoResultException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            courseService.updateCourseStatus(anyLong(), Status.INACTIVE);
        }, "Cant update this course's status, wrong id was given");
        verify(courseDAO).findOne(anyLong());
    }

    @Test
    void deleteCourse_positive_couse_was_deleted() {
        CourseEntity courseEntityDb = mock(CourseEntity.class);

        when(courseDAO.findOne(anyLong())).thenReturn(courseEntityDb);
        when(courseEntityDb.getStatus()).thenReturn(Status.INACTIVE);

        assertEquals(true, courseService.deleteCourse(anyLong()));
        verify(courseDAO).findOne(anyLong());
        verify(courseEntityDb).getStatus();
        verify(courseDAO).delete(courseEntityDb);
    }

    @Test
    void deleteCourse_negative_couse_cant_be_deleted() {
        CourseEntity courseEntityDb = mock(CourseEntity.class);

        when(courseDAO.findOne(anyLong())).thenReturn(courseEntityDb);
        when(courseEntityDb.getStatus()).thenReturn(Status.ACTIVE);

        Assertions.assertThrows(CantDeleteObjectExeption.class, () -> {
            courseService.deleteCourse(anyLong());
        }, "Cant delete this course, becouse now it is in work");
        verify(courseDAO).findOne(anyLong());
        verify(courseEntityDb).getStatus();
    }

    @Test
    void deleteCourse_negative_couse_cant_be_deleted_wrong_id() {
        when(courseDAO.findOne(anyLong())).thenThrow(NoResultException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            courseService.deleteCourse(anyLong());
        }, "Cant delete this course, wrong id sended");
        verify(courseDAO).findOne(anyLong());
    }

    @Test
    void getCourseSchedule_positive_result() {
        CourseEntity courseEntityDb = mock(CourseEntity.class);
        List<LessonEntity> lessonEntityList = mock(ArrayList.class);
        Stream<LessonEntity> lessonEntityStream = mock(Stream.class);
        Stream<LessonScheduleDTO> lessonScheduleDTOStream = mock(Stream.class);
        Stream<LessonScheduleDTO> sortedLessonScheduleDTOStream = mock(Stream.class);
        List<LessonScheduleDTO> lessonScheduleDTOList = mock(ArrayList.class);

        when(courseDAO.findOne(anyLong())).thenReturn(courseEntityDb);
        when(courseEntityDb.getLessons()).thenReturn(lessonEntityList);
        when(lessonEntityList.stream()).thenReturn(lessonEntityStream);
        when(lessonEntityStream.map(o -> mappingUtils.mapToLessonScheduleDTO(o))).thenReturn(lessonScheduleDTOStream);
        when(lessonScheduleDTOStream.sorted(Comparator.comparing(obj -> obj.getStartDate()))).thenReturn(sortedLessonScheduleDTOStream);
        when(sortedLessonScheduleDTOStream.collect(Collectors.toList())).thenReturn(lessonScheduleDTOList);

        courseService.getCourseSchedule(anyLong());
        verify(courseDAO).findOne(anyLong());
        verify(courseEntityDb).getLessons();
        verify(lessonEntityList).stream();
        verify(lessonEntityStream).map(any());
    }

    @Test
    void getCourseSchedule_negative_result_course_doesnt_exist() {

        when(courseDAO.findOne(anyLong())).thenThrow(NoResultException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            courseService.getCourseSchedule(anyLong());
        }, "Cant get course's schedule, becouse it doesnt exist");
        verify(courseDAO).findOne(anyLong());
    }
}