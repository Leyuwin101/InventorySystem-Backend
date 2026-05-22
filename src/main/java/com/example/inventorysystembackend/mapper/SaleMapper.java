package com.example.inventorysystembackend.mapper;

import com.example.inventorysystembackend.dto.nested.SaleItemDTO;
import com.example.inventorysystembackend.dto.nested.UserDTO;
import com.example.inventorysystembackend.dto.request.SaleRequest;
import com.example.inventorysystembackend.dto.response.SaleResponse;
import com.example.inventorysystembackend.model.entity.Sale;
import com.example.inventorysystembackend.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SaleMapper {

    public Sale toEntity(SaleRequest request, User user) {

        Sale sale = new Sale();
        sale.setUser(user);
        sale.setPaymentMethod(request.getPaymentMethod());
        sale.setStatus(request.getStatus());

        return sale;
    }

    public SaleResponse toDTO(Sale sale) {

        if (sale == null) return null;

        UserDTO user = null;

        if (sale.getUser() != null) {
            user = new UserDTO(
                    sale.getUser().getUserID(),
                    sale.getUser().getUsername(),
                    sale.getUser().getRole()
            );
        }

        List<SaleItemDTO> items = (sale.getItems() == null)
                ? List.of()
                : sale.getItems()
                .stream()
                .map(item -> new SaleItemDTO(
                        item.getProduct().getProductID(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getSubtotal()
                ))
                .toList();

        return new SaleResponse(
                sale.getSalesID(),
                user,
                sale.getTotalAmount(),
                sale.getPaymentMethod(),
                sale.getStatus(),
                sale.getCreatedAt(),
                items
        );
    }
}
