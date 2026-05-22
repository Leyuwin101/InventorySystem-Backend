package com.example.inventorysystembackend.dto.request;

import com.example.inventorysystembackend.model.enums.PaymentMethod;
import com.example.inventorysystembackend.model.enums.SaleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleRequest {

    @NotNull(message = "User id is required")
    private Long userId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Status is required")
    private SaleStatus status;

    private List<SaleItemRequest> items;
}
