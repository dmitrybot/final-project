package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces;

import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.DirectionDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.SectionDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.DirectionSectionEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;

import java.util.List;
import java.util.Set;

public interface IDirectionService {
    DirectionDTO createDirection(long tagId) throws DBExeption;
    List<DirectionDTO> getDirections() throws DBExeption;
    Boolean deleteDirection(long id) throws DBExeption, CantDeleteObjectExeption;
    List<SectionDTO> getSections(long id) throws DBExeption;
    Boolean addSectionToDirection(long id, long sectionId) throws DBExeption;
    Boolean addSectionsToDirection(long id, Set<DirectionSectionEntity> sections) throws DBExeption;
}
