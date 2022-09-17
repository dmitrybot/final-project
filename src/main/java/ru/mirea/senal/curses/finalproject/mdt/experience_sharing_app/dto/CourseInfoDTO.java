package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.CourseUpdate;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.NewCourse;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.Owner;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.UpdateCourse;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.Base;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseInfoDTO {
    @JsonView({Owner.class, Base.class, CourseUpdate.class})
    @Null(groups = {NewCourse.class})
    @NotNull(groups = {UpdateCourse.class})
    private Long id;
    @JsonView({CourseUpdate.class})
    @NotNull(groups = {NewCourse.class, UpdateCourse.class})
    private ProductDTO productDTO;
}
