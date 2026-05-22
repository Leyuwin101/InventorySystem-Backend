    package com.example.inventorysystembackend.service;

    import com.example.inventorysystembackend.dto.request.SaleRequest;
    import com.example.inventorysystembackend.dto.response.SaleResponse;
    import com.example.inventorysystembackend.model.entity.Sale;

    import java.util.List;

    public interface SaleService {

        SaleResponse createSale(SaleRequest request);

        SaleResponse getSaleById(Long saleId);

        List<SaleResponse> getAllSales();

        List<SaleResponse> getSalesByUser(Long userId);

        void cancelSale(Long saleId);
    }
