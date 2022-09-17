package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.Details;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.New;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "card")
public class CardInfoEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Details.class)
    @Min(value = 1, groups = {Details.class})
    @Digits(fraction = 0, groups = {Details.class}, integer = 9)
    @Column(name = "card_id")
    private Long id;
    @JsonView(Details.class)
    @NotNull(groups = {Details.class})
    @Length(min = 16, max = 16, groups = {Details.class})
    @Column(name = "number", length = 16)
    private String number;
    @JsonView(Details.class)
    @Length(min = 2, max = 50, groups = {New.class, Details.class})
    @NotNull(groups = {Details.class})
    @Column(name = "full_name", length = 50)
    private String fullName;
    @JsonView(Details.class)
    @Length(min = 5, max = 5, groups = {New.class, Details.class})
    @NotNull(groups = {Details.class})
    @Column(name = "expiration_date", length = 5)
    private String expirationDate;
    @JsonView(Details.class)
    @Length(min = 3, max = 3, groups = {New.class, Details.class})
    @NotNull(groups = {Details.class})
    @Column(name = "cvc", length = 3)
    private String cvc;
}
