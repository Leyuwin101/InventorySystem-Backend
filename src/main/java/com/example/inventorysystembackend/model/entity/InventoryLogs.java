package com.example.inventorysystembackend.model.entity;

import com.example.inventorysystembackend.model.enums.InventoryType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "inventory_logs")
public class InventoryLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_log_id")
    private Long inventoryLogID;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private InventoryType type;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "reason")
    private String reason;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
