package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IDirectionSectionDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IStudyDirectionDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ITagDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.SectionDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.DirectionSectionEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.TagEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.WrongIdExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces.ISectionService;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util.IMappingUtils;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SectionService implements ISectionService {
    private IDirectionSectionDAO directionSectionDAO;
    private ITagDAO tagDAO;
    private IStudyDirectionDAO studyDirectionDAO;
    private IMappingUtils mappingUtils;

    private static final Logger log = Logger.getLogger(SectionService.class);

    public SectionService(IDirectionSectionDAO directionSectionDAO, ITagDAO tagDAO, IStudyDirectionDAO studyDirectionDAO, IMappingUtils mappingUtils) {
        this.directionSectionDAO = directionSectionDAO;
        this.tagDAO = tagDAO;
        this.studyDirectionDAO = studyDirectionDAO;
        this.mappingUtils = mappingUtils;
    }

    @Transactional
    public SectionDTO createSection(DirectionSectionEntity entity) throws DBExeption {
        try {
            return mappingUtils.mapToSectionDTO(directionSectionDAO.create(entity));
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db section create operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public List<SectionDTO> getSections() throws DBExeption {
        try {
            return directionSectionDAO.findAll()
                    .stream()
                    .map(o -> mappingUtils.mapToSectionDTO(o))
                    .collect(Collectors.toList());
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db sections get operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public Boolean deleteSection(long id) throws DBExeption, CantDeleteObjectExeption {
        try {
            directionSectionDAO.deleteById(id);
            return true;
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db section delete operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new WrongIdExeption();
        }
    }

    @Transactional
    public SectionDTO addTagToSection(long id, long tagId) throws DBExeption {
        try {
            DirectionSectionEntity section = directionSectionDAO.findOne(id);
            TagEntity tagEntity = tagDAO.findOne(tagId);
            tagEntity.getSections().add(section);
            section.getTags().add(tagEntity);
            return mappingUtils.mapToSectionDTO(directionSectionDAO.update(section));
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db section's tag add operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public SectionDTO updateSectionTags(Set<TagEntity> tags, long id) throws DBExeption {
        try {
            DirectionSectionEntity directionSectionEntity = directionSectionDAO.findOne(id);
            directionSectionEntity.setTags(tags);
            directionSectionDAO.update(directionSectionEntity);
            return mappingUtils.mapToSectionDTO(directionSectionEntity);
        } catch (PersistenceException | NullPointerException e) {
            log.error("db section's tags update operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        } catch (IllegalArgumentException e) {
            log.error("input section's tags update operation failed because of nonexistent id", e);
            e.printStackTrace();
            throw new WrongIdExeption();
        }
    }
}
