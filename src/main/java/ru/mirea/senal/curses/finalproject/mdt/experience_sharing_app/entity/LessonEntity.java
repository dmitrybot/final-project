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
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.ManyToOne;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "lesson")
public class LessonEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "startdate")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @Column(name = "capacity")
    private Integer capacity;
    @Column(name = "min_donation", columnDefinition = "numeric")
    private Double minDonation = 1.0;
    @Column(name = "lesson_link")
    private String lessonLink;
    @Column(name = "homework_link")
    private String homeWork;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "lesson_info_id")
    private LessonInfoEntity lessonInfo;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseEntity course;
    @ManyToMany(mappedBy = "lessons")
    private Set<UserEntity> users;
}
