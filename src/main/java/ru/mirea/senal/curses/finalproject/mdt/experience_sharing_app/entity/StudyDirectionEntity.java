package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;
import javax.persistence.JoinTable;
import java.io.Serializable;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "study_direction")
public class StudyDirectionEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "direction_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "tag_id")
    private TagEntity tag;
    @ManyToMany(cascade = { CascadeType.PERSIST })
    @JoinTable(
            name = "Section_Direction",
            joinColumns = { @JoinColumn(name = "direction_id") },
            inverseJoinColumns = { @JoinColumn(name = "section_id") }
    )
    private Set<DirectionSectionEntity> sections;
}
