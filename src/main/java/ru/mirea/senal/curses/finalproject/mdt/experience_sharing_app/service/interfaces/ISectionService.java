package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces;

import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.SectionDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.DirectionSectionEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.TagEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;

import java.util.List;
import java.util.Set;

public interface ISectionService {
    SectionDTO createSection(DirectionSectionEntity entity) throws DBExeption;
    List<SectionDTO> getSections() throws DBExeption;
    Boolean deleteSection(long id) throws DBExeption, CantDeleteObjectExeption;
    SectionDTO addTagToSection(long id, long tagId) throws DBExeption;
    SectionDTO updateSectionTags(Set<TagEntity> tags, long id) throws DBExeption;
}
