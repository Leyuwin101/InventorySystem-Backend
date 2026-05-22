package com.example.inventorysystembackend.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "product_suppliers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"product_id", "supplier_id"})
        }
)
public class ProductSupplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_supplier_id")
    private Long productSupplierID;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    @JsonIgnore
    private Supplier supplier;

    @Column(name = "supplier_price", nullable = false)
    private BigDecimal supplierPrice;

    @Column(name = "lead_time_days")
    private Integer leadTimeDays;
}
