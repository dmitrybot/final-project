package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.Details;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.NewSection;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SectionDTO {
    @JsonView(Details.class)
    @Null(groups = {NewSection.class})
    private Long id;
    @NotNull(groups = {NewSection.class})
    @JsonView(Details.class)
    @Length(min = 2, max = 50, groups = {NewSection.class})
    private String name;
    @Valid
    @NotNull(groups = {NewSection.class})
    @JsonView(Details.class)
    private Set<TagDTO> tags;
}
