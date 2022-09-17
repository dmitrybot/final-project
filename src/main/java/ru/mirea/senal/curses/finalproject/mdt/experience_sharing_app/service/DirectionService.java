package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.DirectionDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.SectionDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.DirectionSectionEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.StudyDirectionEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IDirectionSectionDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IStudyDirectionDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.ITagDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.WrongIdExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces.IDirectionService;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util.IMappingUtils;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DirectionService implements IDirectionService {
    private IDirectionSectionDAO directionSectionDAO;
    private ITagDAO tagDAO;
    private IStudyDirectionDAO studyDirectionDAO;
    private IMappingUtils mappingUtils;

    private static final Logger log = Logger.getLogger(CourseService.class);

    public DirectionService(IDirectionSectionDAO directionSectionDAO, ITagDAO tagDAO, IStudyDirectionDAO studyDirectionDAO, IMappingUtils mappingUtils) {
        this.directionSectionDAO = directionSectionDAO;
        this.tagDAO = tagDAO;
        this.studyDirectionDAO = studyDirectionDAO;
        this.mappingUtils = mappingUtils;
    }

    @Transactional
    public DirectionDTO createDirection(long tagId) throws DBExeption {
        try {
            StudyDirectionEntity studyDirectionEntity = studyDirectionDAO.create(new StudyDirectionEntity());
            studyDirectionEntity.setTag(tagDAO.findOne(tagId));
            return mappingUtils.mapToDirectionDTO(studyDirectionDAO.update(studyDirectionEntity));
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db studyDirection create operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public List<DirectionDTO> getDirections() throws DBExeption {
        try {
            return studyDirectionDAO.findAll()
                    .stream()
                    .map(o -> mappingUtils.mapToDirectionDTO(o))
                    .collect(Collectors.toList());
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db studyDirections get operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public List<SectionDTO> getSections(long id) throws DBExeption {
        try {
            return studyDirectionDAO.findOne(id).getSections()
                    .stream()
                    .map(o -> mappingUtils.mapToSectionDTO(o))
                    .collect(Collectors.toList());
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db studyDirection's sections get operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public Boolean deleteDirection(long id) throws DBExeption, CantDeleteObjectExeption {
        try {
            studyDirectionDAO.deleteById(id);
            return true;
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db studyDirection delete operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new WrongIdExeption();
        }
    }

    @Transactional
    public Boolean addSectionToDirection(long id, long sectionId) throws DBExeption {
        try {
            StudyDirectionEntity direction = studyDirectionDAO.findOne(id);
            DirectionSectionEntity sectionEntity = directionSectionDAO.findOne(sectionId);
            sectionEntity.getDirections().add(direction);
            direction.getSections().add(sectionEntity);
            studyDirectionDAO.update(direction);
            return true;
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db studyDirection's section add operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new WrongIdExeption();
        }
    }

    @Transactional
    public Boolean addSectionsToDirection(long id, Set<DirectionSectionEntity> sections) throws DBExeption {
        try {
            StudyDirectionEntity studyDirectionEntity = studyDirectionDAO.findOne(id);
            studyDirectionEntity.setSections(sections);
            studyDirectionDAO.update(studyDirectionEntity);
            return true;
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db studyDirection's sections add operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new WrongIdExeption();
        }
    }
}
