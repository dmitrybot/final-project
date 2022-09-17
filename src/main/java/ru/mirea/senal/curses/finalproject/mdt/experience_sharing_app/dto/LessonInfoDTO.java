package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.NewLessonInfo;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.NewLessonInfoWithoutCourse;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.Owner;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.UpdateLessonInfo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LessonInfoDTO {
    @JsonView(Owner.class)
    @Null(groups = {NewLessonInfo.class, NewLessonInfoWithoutCourse.class})
    @NotNull(groups = {UpdateLessonInfo.class})
    private Long id;
    @NotNull(groups = {NewLessonInfo.class, NewLessonInfoWithoutCourse.class, UpdateLessonInfo.class})
    private ProductDTO productDTO;
    private CourseInfoDTO courseInfoDTO;
    @JsonView(Owner.class)
    private Set<LessonDTO> lessons;
}
