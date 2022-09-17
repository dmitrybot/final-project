package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.PaymentType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.TemporalType;
import javax.persistence.Temporal;
import javax.persistence.EnumType;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "payment")
public class PaymentEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "payment_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentDate;
    @Column(name = "payment_price", columnDefinition = "numeric")
    private Double price;
    @Column(name = "product_realization")
    private Long productRealization;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;
    @Column(name = "checked")
    private Boolean checked;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private UserEntity buyer;
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private UserEntity seller;
    @ManyToOne
    @JoinColumn(name = "product_info_id")
    private ProductInfoEntity product;
}
