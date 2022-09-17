package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.Status;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.Owner;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.StartLesson;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.StartLessonForCourse;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.UpdateLesson;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LessonDTO {
    @JsonView(Owner.class)
    @Null(groups = {StartLessonForCourse.class, StartLesson.class})
    @NotNull(groups = {UpdateLesson.class})
    private Long id;
    @JsonView(Owner.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss X")
    @NotNull(groups = {StartLessonForCourse.class, StartLesson.class, UpdateLesson.class})
    private Date startDate;
    @NotNull(groups = {StartLesson.class, UpdateLesson.class})
    private Integer capacity;
    @NotNull(groups = {UpdateLesson.class})
    private Double minDonation;
    @NotNull(groups = {UpdateLesson.class})
    private String lessonLink;
    @NotNull(groups = {UpdateLesson.class})
    private String homeWork;
    private Status status;
    @NotNull(groups = {StartLessonForCourse.class})
    private Long lessonInfoId;
}
