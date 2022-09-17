package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ITagDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.TagDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.TagEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.WrongIdExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces.ITagService;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util.IMappingUtils;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService implements ITagService {
    private ITagDAO tagDAO;
    private IMappingUtils mappingUtils;

    private static final Logger log = Logger.getLogger(TagService.class);

    public TagService(ITagDAO tagDAO, IMappingUtils mappingUtils) {
        this.tagDAO = tagDAO;
        this.mappingUtils = mappingUtils;
    }

    @Transactional
    public TagDTO createTag(TagEntity tag) throws DBExeption {
        try {
            return mappingUtils.mapToTagDTO(tagDAO.create(tag));
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db tag create operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public Boolean deleteTag(long id) throws DBExeption, CantDeleteObjectExeption {
        try {
            tagDAO.deleteById(id);
            return true;
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db tag delete operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new WrongIdExeption();
        }
    }

    @Transactional
    public List<TagDTO> getTags() throws DBExeption {
        try {
            return tagDAO.findAll()
                    .stream()
                    .map(o -> mappingUtils.mapToTagDTO(o))
                    .collect(Collectors.toList());
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db tags get operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }
}
