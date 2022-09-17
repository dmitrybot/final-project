package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Update;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.Status;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.New;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.Owner;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.UpdateStatus;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseDTO {
    @JsonView(Owner.class)
    @Null(groups = {New.class})
    @NotNull(groups = {Update.class, UpdateStatus.class})
    private Long id;
    @Min(1)
    @NotNull(groups = {New.class, Update.class})
    private Integer capacity;
    @JsonView(Owner.class)
    @NotNull(groups = {New.class, Update.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd X")
    private Date noteDate;
    private Status status;
}
