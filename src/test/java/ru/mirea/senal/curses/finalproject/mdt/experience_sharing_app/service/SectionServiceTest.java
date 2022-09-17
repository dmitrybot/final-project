package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service;

import org.hibernate.HibernateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.*;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.SectionDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.DirectionSectionEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.ProductInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.TagEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.WrongIdExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util.IMappingUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SectionServiceTest {
    @Mock
    private IDirectionSectionDAO directionSectionDAO;
    @Mock
    private IStudyDirectionDAO studyDirectionDAO;
    @Mock
    private IMappingUtils mappingUtils;
    @Mock
    private ITagDAO tagDAO;
    @InjectMocks
    private SectionService sectionService;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createSection_positive() {
        DirectionSectionEntity directionSectionEntity = mock(DirectionSectionEntity.class);
        DirectionSectionEntity directionSectionEntityOutput = mock(DirectionSectionEntity.class);
        SectionDTO sectionDTO = mock(SectionDTO.class);

        when(directionSectionDAO.create(directionSectionEntity)).thenReturn(directionSectionEntityOutput);
        when(mappingUtils.mapToSectionDTO(directionSectionEntityOutput)).thenReturn(sectionDTO);

        assertEquals(sectionDTO, sectionService.createSection(directionSectionEntity));

        verify(directionSectionDAO).create(directionSectionEntity);
        verify(mappingUtils).mapToSectionDTO(directionSectionEntityOutput);
    }

    @Test
    void createSection_negative_error_with_db() {
        DirectionSectionEntity directionSectionEntity = mock(DirectionSectionEntity.class);

        when(directionSectionDAO.create(directionSectionEntity)).thenThrow(HibernateException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            sectionService.createSection(directionSectionEntity);
        }, "Cant create section, error while working with database");

        verify(directionSectionDAO).create(directionSectionEntity);
    }

    @Test
    void getSections_positive() {
        List<DirectionSectionEntity> directionSectionEntityList = mock(List.class);
        Stream<DirectionSectionEntity> directionSectionEntityStream = mock(Stream.class);
        Stream<SectionDTO> sectionDTOStream = mock(Stream.class);
        List<SectionDTO> sectionDTOList = mock(List.class);

        when(directionSectionDAO.findAll()).thenReturn(directionSectionEntityList);
        when(directionSectionEntityList.stream()).thenReturn(directionSectionEntityStream);
        when(directionSectionEntityStream.map(o -> mappingUtils.mapToSectionDTO(o))).thenReturn(sectionDTOStream);
        when(sectionDTOStream.collect(Collectors.toList())).thenReturn(sectionDTOList);

        sectionService.getSections();

        verify(directionSectionDAO).findAll();
        verify(directionSectionEntityList).stream();
        verify(directionSectionEntityStream).map(any());
    }

    @Test
    void getSections_negative() {
        when(directionSectionDAO.findAll()).thenThrow(HibernateException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            sectionService.getSections();
        }, "Cant get sections, error while working with database");

        verify(directionSectionDAO).findAll();
    }

    @Test
    void deleteSection_positive() {
        assertEquals(true, sectionService.deleteSection(1L));

        verify(directionSectionDAO).deleteById(anyLong());
    }

    @Test
    void deleteSection_negative_error_with_db() {
        doThrow(NullPointerException.class).when(directionSectionDAO).deleteById(anyLong());

        Assertions.assertThrows(DBExeption.class, () -> {
            sectionService.deleteSection(1L);
        }, "Cant delete section, error while working with database");

        verify(directionSectionDAO).deleteById(anyLong());
    }

    @Test
    void deleteSection_negative_error_with_id() {
        doThrow(IllegalArgumentException.class).when(directionSectionDAO).deleteById(anyLong());

        Assertions.assertThrows(WrongIdExeption.class, () -> {
            sectionService.deleteSection(1L);
        }, "Cant delete section, error while working with database");

        verify(directionSectionDAO).deleteById(anyLong());
    }

    @Test
    void addTagToSection_positive() {
        DirectionSectionEntity directionSectionEntity = mock(DirectionSectionEntity.class);
        DirectionSectionEntity directionSectionEntityOutput = mock(DirectionSectionEntity.class);
        SectionDTO sectionDTO = mock(SectionDTO.class);
        TagEntity tagEntity = mock(TagEntity.class);
        Set<DirectionSectionEntity> directionSectionEntities = mock(Set.class);
        Set<TagEntity> tagEntities = mock(Set.class);

        when(directionSectionDAO.findOne(anyLong())).thenReturn(directionSectionEntity);
        when(tagDAO.findOne(anyLong())).thenReturn(tagEntity);
        when(tagEntity.getSections()).thenReturn(directionSectionEntities);
        when(directionSectionEntity.getTags()).thenReturn(tagEntities);
        when(directionSectionDAO.update(directionSectionEntity)).thenReturn(directionSectionEntityOutput);
        when(mappingUtils.mapToSectionDTO(directionSectionEntityOutput)).thenReturn(sectionDTO);

        assertEquals(sectionDTO, sectionService.addTagToSection(1L, 1L));

        verify(directionSectionDAO).findOne(anyLong());
        verify(tagDAO).findOne(anyLong());
        verify(tagEntity).getSections();
        verify(directionSectionEntities).add(any());
        verify(directionSectionEntity).getTags();
        verify(tagEntities).add(any());
        verify(directionSectionDAO).update(any());
        verify(mappingUtils).mapToSectionDTO(any());
    }

    @Test
    void addTagToSection_negative() {
        when(directionSectionDAO.findOne(anyLong())).thenThrow(NullPointerException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            sectionService.addTagToSection(1L, 1L);
        }, "Cant add tag to section, error while working with database");

        verify(directionSectionDAO).findOne(anyLong());
    }

    @Test
    void updateSectionTags_positive() {
        DirectionSectionEntity directionSectionEntity = mock(DirectionSectionEntity.class);
        Set<TagEntity> tags = mock(Set.class);
        SectionDTO sectionDTO = mock(SectionDTO.class);

        when(directionSectionDAO.findOne(anyLong())).thenReturn(directionSectionEntity);
        when(mappingUtils.mapToSectionDTO(directionSectionEntity)).thenReturn(sectionDTO);

        assertEquals(sectionDTO, sectionService.updateSectionTags(tags, 2L));

        verify(directionSectionDAO).findOne(anyLong());
        verify(directionSectionEntity).setTags(tags);
        verify(directionSectionDAO).update(directionSectionEntity);
        verify(mappingUtils).mapToSectionDTO(directionSectionEntity);
    }

    @Test
    void updateSectionTags_negative_error_with_db() {
        Set<TagEntity> tags = mock(Set.class);

        when(directionSectionDAO.findOne(anyLong())).thenThrow(NullPointerException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            sectionService.updateSectionTags(tags, 2L);
        }, "Cant update section's tags, error while working with database");

        verify(directionSectionDAO).findOne(anyLong());
    }

    @Test
    void updateSectionTags_negative_error_with_id() {
        Set<TagEntity> tags = mock(Set.class);

        when(directionSectionDAO.findOne(anyLong())).thenThrow(IllegalArgumentException.class);

        Assertions.assertThrows(WrongIdExeption.class, () -> {
            sectionService.updateSectionTags(tags, 2L);
        }, "Cant update section's tags, error while working with database");

        verify(directionSectionDAO).findOne(anyLong());
    }
}