package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.ProductType;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.Status;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.EnumType;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product_info")
public class ProductInfoEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_info_id")
    private Long id;
    @Column(name = "name", length = 50)
    private String name;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "capacity")
    private Integer capacity;
    @Column(name = "price", columnDefinition = "numeric")
    private Double price;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "product_type")
    private ProductType productType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<PaymentEntity> payments;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<ReportEntity> reports = new ArrayList<>();
    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "Tag_Product",
            joinColumns = {@JoinColumn(name = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private Set<TagEntity> tags;

    public void addReport(ReportEntity reportEntity) {
        reports.add(reportEntity);
    }

    public void update(ProductInfoEntity p) {
        if (p.getName() != null) this.name = p.getName();
        if (p.getCapacity() != null) this.capacity = p.getCapacity();
        if (p.getDescription() != null) this.description = p.getDescription();
        if (p.getPrice() != null) this.price = p.getPrice();
        if (p.getStatus() != null) this.status = p.getStatus();
    }
}
