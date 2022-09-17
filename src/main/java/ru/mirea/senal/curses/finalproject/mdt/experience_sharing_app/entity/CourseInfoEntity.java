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
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "course_info")
public class CourseInfoEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_info_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_info_id")
    private ProductInfoEntity productInfoEntity;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    private Set<CourseEntity> courses = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    private Set<LessonInfoEntity> lessons = new HashSet<>();

    @Override
    public String toString() {
        return "CourseInfoEntity{} " + super.toString();
    }
}
