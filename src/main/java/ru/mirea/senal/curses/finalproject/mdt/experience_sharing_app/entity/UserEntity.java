package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.security.Role;

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
import javax.persistence.OneToMany;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "account")
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "email", length = 50, unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "first_name", length = 50)
    private String firstName;
    @Column(name = "last_name", length = 50)
    private String lastName;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", length = 50)
    private Role role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "card_id")
    private CardInfoEntity card;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "User_Lesson",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "lesson_id")}
    )
    private Set<LessonEntity> lessons;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "User_Course",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "course_id")}
    )
    private Set<CourseEntity> courses;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
    private Set<ReportEntity> reports;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
    private List<ProductInfoEntity> createdProducts;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "buyer")
    private List<PaymentEntity> sendedPayments;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seller")
    private List<PaymentEntity> receivedPayments;

    public void addReport(ReportEntity reportEntity) {
        reports.add(reportEntity);
    }

    public void update(UserEntity u) {
        this.password = u.getPassword();
        this.firstName = u.getFirstName();
        this.lastName = u.getLastName();
        this.card = u.getCard();
    }
}
