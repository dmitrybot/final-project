package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.ProductType;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.Status;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.Base;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.CourseUpdate;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.NewCourse;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.Owner;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.NewLessonInfoWithoutCourse;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.NewLessonInfo;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.UpdateLessonInfo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDTO {
    @JsonView({Owner.class, Base.class, CourseUpdate.class})
    @Null(groups = {NewCourse.class, NewLessonInfoWithoutCourse.class, NewLessonInfo.class})
    @NotNull(groups = {UpdateLessonInfo.class})
    private Long id;
    @JsonView({Owner.class, CourseUpdate.class})
    @NotNull(groups = {NewCourse.class, NewLessonInfo.class, NewLessonInfoWithoutCourse.class, UpdateLessonInfo.class})
    private String description;
    @JsonView({Owner.class, CourseUpdate.class})
    @NotNull(groups = {NewCourse.class, NewLessonInfo.class, NewLessonInfoWithoutCourse.class, UpdateLessonInfo.class})
    private String name;
    @JsonView({Owner.class, CourseUpdate.class})
    private Integer capacity;
    @JsonView({Owner.class, CourseUpdate.class})
    private Double price;
    @JsonView({Owner.class, CourseUpdate.class})
    private Status status;
    @JsonView({Owner.class, CourseUpdate.class})
    private ProductType productType;

    @Null(groups = {NewCourse.class, NewLessonInfoWithoutCourse.class, NewLessonInfo.class, UpdateLessonInfo.class})
    private UserDTO author;
    @JsonView(Owner.class)
    @Null(groups = {NewCourse.class, NewLessonInfoWithoutCourse.class, NewLessonInfo.class, UpdateLessonInfo.class})
    private List<ReportDTO> reports;
    @JsonView(Owner.class)
    @Null(groups = {NewCourse.class, NewLessonInfoWithoutCourse.class, NewLessonInfo.class, UpdateLessonInfo.class})
    private Set<TagDTO> tags;
}
