package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.Base;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.Details;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.New;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.SubscribedUser;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.CardInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.security.Role;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Email;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    @JsonView({Details.class, Base.class, SubscribedUser.class})
    @Null(groups = {New.class})
    @Min(value = 1, groups = {Details.class})
    @Digits(fraction = 0, groups = {Details.class}, integer = 9)
    @NotNull(groups = {Details.class})
    private Long id;
    @JsonView({Details.class, Base.class, SubscribedUser.class})
    @Email(message = "Email address has invalid format: ${validatedValue}",
            regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", groups = {New.class, Details.class})
    @NotNull(groups = {New.class, Details.class})
    private String email;
    @Length(min = 2, max = 50, groups = {New.class, Details.class})
    @NotNull(groups = {New.class, Details.class})
    private String password;
    @JsonView({Details.class, SubscribedUser.class})
    @Length(min = 2, max = 50, groups = {Details.class})
    @NotNull(groups = {Details.class})
    private String firstName;
    @JsonView({Details.class, SubscribedUser.class})
    @Length(min = 2, max = 50, groups = {Details.class})
    @NotNull(groups = {Details.class})
    private String lastName;
    @JsonView(Details.class)
    private Role role;
    @JsonView(Details.class)
    @NotNull(groups = {Details.class})
    private CardInfoEntity card;
}
