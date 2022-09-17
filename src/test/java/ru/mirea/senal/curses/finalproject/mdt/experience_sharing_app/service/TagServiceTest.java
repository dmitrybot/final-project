package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service;

import org.hibernate.HibernateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IDirectionSectionDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IStudyDirectionDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ITagDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.SectionDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.TagDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.DirectionSectionEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.TagEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.WrongIdExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util.IMappingUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TagServiceTest {
    @Mock
    private IMappingUtils mappingUtils;
    @Mock
    private ITagDAO tagDAO;
    @InjectMocks
    private TagService tagService;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createTag_positive() {
        TagEntity tagEntityInput = mock(TagEntity.class);
        TagEntity tagEntityOutput = mock(TagEntity.class);
        TagDTO tagDTO = mock(TagDTO.class);

        when(tagDAO.create(tagEntityInput)).thenReturn(tagEntityOutput);
        when(mappingUtils.mapToTagDTO(tagEntityOutput)).thenReturn(tagDTO);

        assertEquals(tagDTO, tagService.createTag(tagEntityInput));

        verify(tagDAO).create(tagEntityInput);
        verify(mappingUtils).mapToTagDTO(tagEntityOutput);
    }

    @Test
    void createTag_negative() {
        TagEntity tagEntityInput = mock(TagEntity.class);

        when(tagDAO.create(tagEntityInput)).thenThrow(NullPointerException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            tagService.createTag(tagEntityInput);
        }, "Cant create tag, error while working with database");

        verify(tagDAO).create(tagEntityInput);
    }

    @Test
    void deleteTag_positive() {
        assertEquals(true, tagService.deleteTag(1L));

        verify(tagDAO).deleteById(anyLong());
    }

    @Test
    void deleteTag_negative_error_with_db() {
        doThrow(NullPointerException.class).when(tagDAO).deleteById(anyLong());

        Assertions.assertThrows(DBExeption.class, () -> {
            tagService.deleteTag(1L);
        }, "Cant delete tag, error while working with database");

        verify(tagDAO).deleteById(anyLong());
    }

    @Test
    void deleteTag_negative_error_with_id() {
        doThrow(IllegalArgumentException.class).when(tagDAO).deleteById(anyLong());

        Assertions.assertThrows(WrongIdExeption.class, () -> {
            tagService.deleteTag(1L);
        }, "Cant delete tag, error while working with database");

        verify(tagDAO).deleteById(anyLong());
    }

    @Test
    void getTags_positive() {
        List<TagEntity> tagEntities = mock(List.class);
        Stream<TagEntity> tagEntityStream = mock(Stream.class);
        Stream<TagDTO> tagDTOStream = mock(Stream.class);
        List<TagDTO> tagDTOList = mock(List.class);

        when(tagDAO.findAll()).thenReturn(tagEntities);
        when(tagEntities.stream()).thenReturn(tagEntityStream);
        when(tagEntityStream.map(o -> mappingUtils.mapToTagDTO(o))).thenReturn(tagDTOStream);
        when(tagDTOStream.collect(Collectors.toList())).thenReturn(tagDTOList);

        tagService.getTags();

        verify(tagDAO).findAll();
        verify(tagEntities).stream();
        verify(tagEntityStream).map(any());
    }

    @Test
    void getTags_negative() {
        when(tagDAO.findAll()).thenThrow(HibernateException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            tagService.getTags();
        }, "Cant get tags, error while working with database");

        verify(tagDAO).findAll();
    }
}