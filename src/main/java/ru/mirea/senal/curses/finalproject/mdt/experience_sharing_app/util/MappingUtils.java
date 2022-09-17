package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util;

import org.springframework.stereotype.Service;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.CourseInfoDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.LessonInfoDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.ProductDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.UserDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.LessonScheduleDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.CourseDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.DirectionDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.LessonDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.ReportDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.SectionDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.TagDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.CourseInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.LessonInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.ProductInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.UserEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.CourseEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.DirectionSectionEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.LessonEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.PaymentEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.ReportEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.StudyDirectionEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.TagEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MappingUtils implements IMappingUtils {

    //из entity в dto
    public UserDTO mapToUserDTO(UserEntity entity) {
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setRole(entity.getRole());
        dto.setCard(entity.getCard());
        return dto;
    }

    //из dto в entity
    public UserEntity mapToUserEntity(UserDTO dto) {
        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setRole(dto.getRole());
        entity.setCard(dto.getCard());
        return entity;
    }

    @Override
    public ProductDTO mapToProductDTO(ProductInfoEntity entity) {
        ProductDTO dto = new ProductDTO();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setStatus(entity.getStatus());
        dto.setCapacity(entity.getCapacity());
        dto.setProductType(entity.getProductType());

        if (entity.getAuthor() != null) dto.setAuthor(mapToUserDTO(entity.getAuthor()));
        if (entity.getReports() != null) dto.setReports(entity.getReports()
                .stream()
                .map(o -> mapToReportDTO(o))
                .collect(Collectors.toList()));
        if (entity.getTags() != null) dto.setTags(entity.getTags()
                .stream()
                .map(o -> mapToTagDTO(o))
                .collect(Collectors.toSet()));
        return dto;
    }

    @Override
    public ProductInfoEntity mapToProductInfoEntity(ProductDTO dto) {
        ProductInfoEntity entity = new ProductInfoEntity();
        entity.setId(dto.getId());
        entity.setDescription(dto.getDescription());
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setStatus(dto.getStatus());
        entity.setCapacity(dto.getCapacity());

        if (dto.getAuthor() != null) entity.setAuthor(mapToUserEntity(dto.getAuthor()));
        if (dto.getReports() != null) entity.setReports(dto.getReports()
                .stream()
                .map(o -> mapToReportEntity(o))
                .collect(Collectors.toList()));
        if (dto.getTags() != null) entity.setTags(dto.getTags()
                .stream()
                .map(o -> mapToTagEntity(o))
                .collect(Collectors.toSet()));
        return entity;
    }

    //из entity в dto
    public CourseInfoDTO mapToCourseInfoDTO(CourseInfoEntity entity) {
        CourseInfoDTO dto = new CourseInfoDTO();
        dto.setId(entity.getId());
        if (entity.getProductInfoEntity() != null) dto.setProductDTO(mapToProductDTO(entity.getProductInfoEntity()));
        return dto;
    }

    //из dto в entity
    public CourseInfoEntity mapToCourseInfoEntity(CourseInfoDTO dto) {
        CourseInfoEntity entity = new CourseInfoEntity();
        entity.setId(dto.getId());
        if (dto.getProductDTO() != null) entity.setProductInfoEntity(mapToProductInfoEntity(dto.getProductDTO()));
        return entity;
    }

    //из entity в dto
    public LessonInfoDTO mapToLessonInfoDTO(LessonInfoEntity entity) {
        LessonInfoDTO dto = new LessonInfoDTO();
        dto.setId(entity.getId());
        if (entity.getProductInfoEntity() != null) dto.setProductDTO(mapToProductDTO(entity.getProductInfoEntity()));
        return dto;
    }

    //из dto в entity
    public LessonInfoEntity mapToLessonInfoEntity(LessonInfoDTO dto) {
        LessonInfoEntity entity = new LessonInfoEntity();
        entity.setId(dto.getId());
        if (dto.getProductDTO() != null) entity.setProductInfoEntity(mapToProductInfoEntity(dto.getProductDTO()));
        return entity;
    }

    //из entity в dto
    public CourseDTO mapToCourseDTO(CourseEntity entity) {
        CourseDTO dto = new CourseDTO();
        dto.setId(entity.getId());
        dto.setCapacity(entity.getCapacity());
        dto.setNoteDate(entity.getNoteDate());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    //из dto в entity
    public CourseEntity mapToCourseEntity(CourseDTO dto) {
        CourseEntity entity = new CourseEntity();
        entity.setId(dto.getId());
        entity.setCapacity(dto.getCapacity());
        entity.setNoteDate(dto.getNoteDate());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    //из entity в dto
    public LessonDTO mapToLessonDTO(LessonEntity entity) {
        LessonDTO dto = new LessonDTO();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setMinDonation(entity.getMinDonation());
        dto.setLessonLink(entity.getLessonLink());
        dto.setHomeWork(entity.getHomeWork());
        dto.setStartDate(entity.getStartDate());
        dto.setCapacity(entity.getCapacity());
        dto.setLessonInfoId(entity.getLessonInfo().getId());
        return dto;
    }

    //из dto в entity
    public LessonEntity mapToLessonEntity(LessonDTO dto) {
        LessonEntity entity = new LessonEntity();
        entity.setId(dto.getId());
        entity.setStatus(dto.getStatus());
        entity.setMinDonation(dto.getMinDonation());
        entity.setLessonLink(dto.getLessonLink());
        entity.setHomeWork(dto.getHomeWork());
        entity.setStartDate(dto.getStartDate());
        entity.setCapacity(dto.getCapacity());
        return entity;
    }

    //из entity в dto
    public LessonScheduleDTO mapToLessonScheduleDTO(LessonEntity entity) {
        LessonScheduleDTO dto = new LessonScheduleDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getLessonInfo().getProductInfoEntity().getName());
        dto.setStartDate(entity.getStartDate());
        return dto;
    }

    //из entity в dto
    public ReportDTO mapToReportDTO(ReportEntity entity) {
        ReportDTO dto = new ReportDTO();
        dto.setId(entity.getId());
        dto.setCreationDate(entity.getCreationDate());
        dto.setMessage(entity.getMessage());
        dto.setAuthorName(entity.getAuthor().getFirstName() + " " + entity.getAuthor().getLastName());
        dto.setAuthor_id(entity.getAuthor().getId());
        return dto;
    }

    //из dto в entity
    public ReportEntity mapToReportEntity(ReportDTO dto) {
        ReportEntity entity = new ReportEntity();
        entity.setId(dto.getId());
        entity.setCreationDate(dto.getCreationDate());
        entity.setMessage(dto.getMessage());
        return entity;
    }

    //из entity в dto
    public TagDTO mapToTagDTO(TagEntity entity) {
        TagDTO dto = new TagDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    //из dto в entity
    public TagEntity mapToTagEntity(TagDTO dto) {
        TagEntity entity = new TagEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        return entity;
    }

    //из entity в dto
    public SectionDTO mapToSectionDTO(DirectionSectionEntity entity) {
        SectionDTO dto = new SectionDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        if (entity.getTags() != null) dto.setTags(entity.getTags()
                .stream()
                .map(o -> mapToTagDTO(o))
                .collect(Collectors.toSet()));
        return dto;
    }

    //из dto в entity
    public DirectionSectionEntity mapToSectionEntity(SectionDTO dto) {
        DirectionSectionEntity entity = new DirectionSectionEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        return entity;
    }

    //из entity в dto
    public DirectionDTO mapToDirectionDTO(StudyDirectionEntity entity) {
        DirectionDTO dto = new DirectionDTO();
        dto.setId(entity.getId());
        dto.setTag(mapToTagDTO(entity.getTag()));
        return dto;
    }

    //из dto в entity
    public StudyDirectionEntity mapToStudyDirectionEntity(DirectionDTO dto) {
        StudyDirectionEntity entity = new StudyDirectionEntity();
        entity.setId(dto.getId());
        entity.setTag(mapToTagEntity(dto.getTag()));
        return entity;
    }

    public String mapListLongToString(List<Long> l) {
        String result = "'" + l.get(0).toString() + "'";
        for (int i = 1; i < l.size(); i++) {
            result += ", '" + l.get(i) + "'";
        }
        return result;
    }

    public PaymentEntity makePaymentEntity() {
        return new PaymentEntity();
    }
}