package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.New;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.Owner;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ReportDTO {
    @JsonView(Owner.class)
    @Null(groups = {New.class})
    private Long id;
    @JsonView(Owner.class)
    @NotNull(groups = {New.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss X")
    private Date creationDate;
    @JsonView(Owner.class)
    @NotNull(groups = {New.class})
    private String message;
    @JsonView(Owner.class)
    private String authorName;
    @NotNull(groups = {New.class})
    private Long author_id;
}
