package com.example.inventorysystembackend.dto.response;

import com.example.inventorysystembackend.dto.nested.SaleItemDTO;
import com.example.inventorysystembackend.dto.nested.UserDTO;
import com.example.inventorysystembackend.model.enums.PaymentMethod;
import com.example.inventorysystembackend.model.enums.SaleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleResponse {

    private Long saleId;

    private UserDTO user;

    private BigDecimal totalAmount;

    private PaymentMethod paymentMethod;

    private SaleStatus status;

    private LocalDateTime createdAt;

    private List<SaleItemDTO> items;
}
