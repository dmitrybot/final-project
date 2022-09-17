package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util;

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

public interface IMappingUtils {
    UserDTO mapToUserDTO(UserEntity entity);

    UserEntity mapToUserEntity(UserDTO dto);

    ProductDTO mapToProductDTO(ProductInfoEntity entity);

    ProductInfoEntity mapToProductInfoEntity(ProductDTO dto);

    CourseInfoDTO mapToCourseInfoDTO(CourseInfoEntity entity);

    CourseInfoEntity mapToCourseInfoEntity(CourseInfoDTO dto);

    LessonInfoDTO mapToLessonInfoDTO(LessonInfoEntity entity);

    LessonInfoEntity mapToLessonInfoEntity(LessonInfoDTO dto);

    LessonDTO mapToLessonDTO(LessonEntity entity);

    LessonEntity mapToLessonEntity(LessonDTO dto);

    LessonScheduleDTO mapToLessonScheduleDTO(LessonEntity entity);

    CourseEntity mapToCourseEntity(CourseDTO dto);

    CourseDTO mapToCourseDTO(CourseEntity entity);

    ReportDTO mapToReportDTO(ReportEntity entity);

    ReportEntity mapToReportEntity(ReportDTO dto);

    TagDTO mapToTagDTO(TagEntity entity);

    TagEntity mapToTagEntity(TagDTO dto);

    SectionDTO mapToSectionDTO(DirectionSectionEntity entity);

    DirectionSectionEntity mapToSectionEntity(SectionDTO dto);

    DirectionDTO mapToDirectionDTO(StudyDirectionEntity entity);

    StudyDirectionEntity mapToStudyDirectionEntity(DirectionDTO dto);

    String mapListLongToString(List<Long> l);

    PaymentEntity makePaymentEntity();
}
