package com.example.inventorysystembackend.service;

import com.example.inventorysystembackend.dto.request.CategoryRequest;
import com.example.inventorysystembackend.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse updateCategory(Long categoryId, CategoryRequest request);

    void deleteCategory(Long categoryId);

    CategoryResponse getCategoryById(Long categoryId);

    List<CategoryResponse> getAllCategories();
}
