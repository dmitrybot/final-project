package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.Base;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.Details;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DirectionDTO {
    @JsonView({Base.class, Details.class})
    private Long id;
    @JsonView({Base.class, Details.class})
    private TagDTO tag;
}
