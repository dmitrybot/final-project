package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.Status;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "course")
public class CourseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;
    @Column(name = "capacity")
    private Integer capacity;
    @Temporal(TemporalType.DATE)
    @Column(name = "notedate")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date noteDate;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "course_info_id")
    private CourseInfoEntity course;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    private List<LessonEntity> lessons;
    @ManyToMany(mappedBy = "courses")
    private Set<UserEntity> users;

    @Override
    public String toString() {
        return "CourseEntity{" +
                "id=" + id +
                ", capacity=" + capacity +
                ", noteDate=" + noteDate +
                '}';
    }
}
