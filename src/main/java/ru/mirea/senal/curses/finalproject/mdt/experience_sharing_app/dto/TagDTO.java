package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.Details;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.New;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.NewCourse;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.NewSection;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.ProductFiltration;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.Owner;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.Base;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class TagDTO {
    @Null(groups = {New.class})
    @NotNull(groups = {Details.class, NewCourse.class, NewSection.class, ProductFiltration.class})
    @JsonView({Base.class, Details.class, Owner.class})
    private Long id;
    @JsonView({Base.class, Details.class, Owner.class})
    @Length(min = 1, max = 50, groups = {New.class, Details.class, NewCourse.class})
    @NotNull(groups = {New.class, Details.class, NewCourse.class})
    private String name;
}
