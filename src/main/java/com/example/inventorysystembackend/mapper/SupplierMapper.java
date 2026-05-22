package com.example.inventorysystembackend.mapper;

import com.example.inventorysystembackend.dto.nested.SupplierProductDTO;
import com.example.inventorysystembackend.dto.request.SupplierRequest;
import com.example.inventorysystembackend.dto.response.SupplierResponse;
import com.example.inventorysystembackend.model.entity.Supplier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SupplierMapper {

    public Supplier toEntity(SupplierRequest request) {

        Supplier supplier = new Supplier();

        supplier.setName(request.getName());
        supplier.setEmail(request.getEmail());
        supplier.setPhone(request.getPhone());
        supplier.setAddress(request.getAddress());
        supplier.setCompanyName(request.getCompanyName());

        return supplier;
    }

    public SupplierResponse toDTO(Supplier supplier) {

        List<SupplierProductDTO> suppliers =
                supplier.getProducts() == null
                        ? List.of()
                        : supplier.getProducts()
                          .stream()
                          .map(sp -> new SupplierProductDTO(
                                  sp.getProduct().getProductID(),
                                  sp.getProduct().getName(),
                                  sp.getProduct().getSku(),
                                  sp.getSupplierPrice(),
                                  sp.getLeadTimeDays()
                          ))
                          .toList();

        return new SupplierResponse(
                supplier.getSupplierID(),
                supplier.getName(),
                supplier.getEmail(),
                supplier.getPhone(),
                supplier.getAddress(),
                supplier.getCompanyName(),
                suppliers
        );
    }
}
