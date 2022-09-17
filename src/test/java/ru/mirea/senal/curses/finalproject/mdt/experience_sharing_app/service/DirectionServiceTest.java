package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service;

import org.hibernate.HibernateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.*;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.DirectionDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.SectionDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.DirectionSectionEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.StudyDirectionEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.TagEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.WrongIdExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util.IMappingUtils;

import javax.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class DirectionServiceTest {

    @Mock
    private IDirectionSectionDAO directionSectionDAO;
    @Mock
    private ITagDAO tagDAO;
    @Mock
    private IStudyDirectionDAO studyDirectionDAO;
    @Mock
    private IMappingUtils mappingUtils;
    @InjectMocks
    private DirectionService directionService;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createDirection_positive_direction_successfuly_created() {
        StudyDirectionEntity studyDirectionEntity = mock(StudyDirectionEntity.class);
        StudyDirectionEntity studyDirectionEntity2 = mock(StudyDirectionEntity.class);
        DirectionDTO directionDTO = mock(DirectionDTO.class);
        TagEntity tagEntity = mock(TagEntity.class);

        when(studyDirectionDAO.create(any())).thenReturn(studyDirectionEntity);
        when(tagDAO.findOne(anyLong())).thenReturn(tagEntity);
        when(studyDirectionDAO.update(studyDirectionEntity)).thenReturn(studyDirectionEntity2);
        when(mappingUtils.mapToDirectionDTO(studyDirectionEntity2)).thenReturn(directionDTO);

        assertEquals(directionDTO, directionService.createDirection(anyLong()));

        verify(studyDirectionDAO).create(any());
        verify(tagDAO).findOne(anyLong());
        verify(studyDirectionEntity).setTag(tagEntity);
        verify(studyDirectionDAO).update(studyDirectionEntity);
        verify(mappingUtils).mapToDirectionDTO(studyDirectionEntity2);

    }

    @Test
    void createDirection_negative_direction_successfuly_created() {
        StudyDirectionEntity studyDirectionEntity = mock(StudyDirectionEntity.class);

        when(studyDirectionDAO.create(any())).thenReturn(studyDirectionEntity);
        when(tagDAO.findOne(anyLong())).thenThrow(NoResultException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            directionService.createDirection(anyLong());
        }, "Cant create study direction, error while finding data in database");

        verify(studyDirectionDAO).create(any());
        verify(tagDAO).findOne(anyLong());

    }

    @Test
    void getDirections_positive() {
        List<StudyDirectionEntity> studyDirectionEntityList = mock(ArrayList.class);
        Stream<StudyDirectionEntity> studyDirectionEntityStream = mock(Stream.class);
        Stream<DirectionDTO> directionDTOStream = mock(Stream.class);
        List<DirectionDTO> directionDTOList = mock(ArrayList.class);

        when(studyDirectionDAO.findAll()).thenReturn(studyDirectionEntityList);
        when(studyDirectionEntityList.stream()).thenReturn(studyDirectionEntityStream);
        when(studyDirectionEntityStream.map(o -> mappingUtils.mapToDirectionDTO(o))).thenReturn(directionDTOStream);
        when(directionDTOStream.collect(Collectors.toList())).thenReturn(directionDTOList);

        directionService.getDirections();

        verify(studyDirectionDAO).findAll();
        verify(studyDirectionEntityList).stream();
        verify(studyDirectionEntityStream).map(any());
    }

    @Test
    void getDirections_negative() {
        when(studyDirectionDAO.findAll()).thenThrow(HibernateException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            directionService.getDirections();
        }, "Cant get directions, error while working with db");

        verify(studyDirectionDAO).findAll();
    }

    @Test
    void getSections_positive_sections_returned() {
        StudyDirectionEntity studyDirectionEntity = mock(StudyDirectionEntity.class);
        Set<DirectionSectionEntity> directionSectionEntities = mock(Set.class);
        Stream<DirectionSectionEntity> directionSectionEntityStream = mock(Stream.class);
        Stream<SectionDTO> sectionDTOStream = mock(Stream.class);
        List<SectionDTO> sectionDTOList = mock(List.class);

        when(studyDirectionDAO.findOne(anyLong())).thenReturn(studyDirectionEntity);
        when(studyDirectionEntity.getSections()).thenReturn(directionSectionEntities);
        when(directionSectionEntities.stream()).thenReturn(directionSectionEntityStream);
        when(directionSectionEntityStream.map(o -> mappingUtils.mapToSectionDTO(o))).thenReturn(sectionDTOStream);
        when(sectionDTOStream.collect(Collectors.toList())).thenReturn(sectionDTOList);

        directionService.getSections(anyLong());

        verify(studyDirectionDAO).findOne(anyLong());
        verify(studyDirectionEntity).getSections();
        verify(directionSectionEntities).stream();
        verify(directionSectionEntityStream).map(any());
    }

    @Test
    void getSections_negative_wrong_id() {
        when(studyDirectionDAO.findOne(anyLong())).thenThrow(NoResultException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            directionService.getSections(anyLong());
        }, "Cant get direction sections, error while working with db");

        verify(studyDirectionDAO).findOne(anyLong());
    }

    @Test
    void deleteDirection_positive() {
        assertEquals(true, directionService.deleteDirection(anyLong()));
        verify(studyDirectionDAO).deleteById(anyLong());
    }

    @Test
    void deleteDirection_negative_error_with_db() {
        doThrow(NullPointerException.class).when(studyDirectionDAO).deleteById(anyLong());

        Assertions.assertThrows(DBExeption.class, () -> {
            directionService.deleteDirection(anyLong());
        }, "Cant get direction sections, error while working with db");

        verify(studyDirectionDAO).deleteById(anyLong());
    }

    @Test
    void deleteDirection_negative_error_with_id() {
        doThrow(IllegalArgumentException.class).when(studyDirectionDAO).deleteById(anyLong());

        Assertions.assertThrows(WrongIdExeption.class, () -> {
            directionService.deleteDirection(anyLong());
        }, "Cant get direction sections, error while working with id");

        verify(studyDirectionDAO).deleteById(anyLong());
    }

    @Test
    void addSectionToDirection_positive() {
        StudyDirectionEntity studyDirectionEntity = mock(StudyDirectionEntity.class);
        DirectionSectionEntity directionSectionEntity = mock(DirectionSectionEntity.class);
        Set<StudyDirectionEntity> studyDirectionEntityList = mock(Set.class);
        Set<DirectionSectionEntity> directionSectionEntities = mock(Set.class);

        when(studyDirectionDAO.findOne(anyLong())).thenReturn(studyDirectionEntity);
        when(directionSectionDAO.findOne(anyLong())).thenReturn(directionSectionEntity);
        when(studyDirectionEntity.getSections()).thenReturn(directionSectionEntities);
        when(directionSectionEntity.getDirections()).thenReturn(studyDirectionEntityList);

        assertEquals(true, directionService.addSectionToDirection(1L, 1L));

        verify(studyDirectionDAO).findOne(anyLong());
        verify(directionSectionDAO).findOne(anyLong());
        verify(studyDirectionEntity).getSections();
        verify(directionSectionEntity).getDirections();
        verify(studyDirectionEntityList).add(studyDirectionEntity);
        verify(directionSectionEntities).add(directionSectionEntity);
        verify(studyDirectionDAO).update(studyDirectionEntity);
    }

    @Test
    void addSectionToDirection_negative_error_with_db() {

        when(studyDirectionDAO.findOne(anyLong())).thenThrow(NullPointerException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            directionService.addSectionToDirection(1L, 1L);
        }, "Cant add section to direction, error while working with db");

        verify(studyDirectionDAO).findOne(anyLong());
    }

    @Test
    void addSectionToDirection_negative_error_with_id() {

        when(studyDirectionDAO.findOne(anyLong())).thenThrow(IllegalArgumentException.class);

        Assertions.assertThrows(WrongIdExeption.class, () -> {
            directionService.addSectionToDirection(1L, 1L);
        }, "Cant add section to direction, error while working with id");

        verify(studyDirectionDAO).findOne(anyLong());
    }

    @Test
    void addSectionsToDirection_positive() {
        Set<DirectionSectionEntity> directionSectionEntities = mock(Set.class);
        StudyDirectionEntity studyDirectionEntity = mock(StudyDirectionEntity.class);

        when(studyDirectionDAO.findOne(anyLong())).thenReturn(studyDirectionEntity);

        assertEquals(true, directionService.addSectionsToDirection(1L, directionSectionEntities));

        verify(studyDirectionDAO).findOne(anyLong());
        verify(studyDirectionEntity).setSections(directionSectionEntities);
        verify(studyDirectionDAO).update(studyDirectionEntity);
    }

    @Test
    void addSectionsToDirection_negative_error_with_db() {
        Set<DirectionSectionEntity> directionSectionEntities = mock(Set.class);

        when(studyDirectionDAO.findOne(anyLong())).thenThrow(NullPointerException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            directionService.addSectionsToDirection(1L, directionSectionEntities);
        }, "Cant add section to direction, error while working with db");

        verify(studyDirectionDAO).findOne(anyLong());
    }

    @Test
    void addSectionsToDirection_negative_error_with_id() {
        Set<DirectionSectionEntity> directionSectionEntities = mock(Set.class);

        when(studyDirectionDAO.findOne(anyLong())).thenThrow(IllegalArgumentException.class);

        Assertions.assertThrows(WrongIdExeption.class, () -> {
            directionService.addSectionsToDirection(1L, directionSectionEntities);
        }, "Cant add section to direction, error while working with id");

        verify(studyDirectionDAO).findOne(anyLong());
    }
}