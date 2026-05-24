package com.example.inventorysystembackend.model.entity;

import com.example.inventorysystembackend.model.enums.PaymentMethod;
import com.example.inventorysystembackend.model.enums.SaleStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(
        name = "sales",
        indexes = {
                @Index(name = "idx_sales_user_created", columnList = "user_id, created_at"),
                @Index(name = "idx_sales_status_created", columnList = "status, created_at")
        }
)
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Long salesID;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SaleStatus status;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<SaleItems> items = new ArrayList<>();


}
