package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces;

import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.TagDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.TagEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;

import java.util.List;

public interface ITagService {
    TagDTO createTag(TagEntity tag) throws DBExeption;
    Boolean deleteTag(long id) throws DBExeption, CantDeleteObjectExeption;
    List<TagDTO> getTags() throws DBExeption;
}
