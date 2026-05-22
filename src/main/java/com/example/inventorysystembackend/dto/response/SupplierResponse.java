package com.example.inventorysystembackend.dto.response;

import com.example.inventorysystembackend.dto.nested.ProductSupplierDTO;
import com.example.inventorysystembackend.dto.nested.SupplierProductDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierResponse {

    private Long supplierId;

    private String name;

    private String email;

    private String phone;

    private String address;

    private String companyName;

    private List<SupplierProductDTO> products;
}
