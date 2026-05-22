package com.example.inventorysystembackend.mapper;

import com.example.inventorysystembackend.dto.request.CategoryRequest;
import com.example.inventorysystembackend.dto.response.CategoryResponse;
import com.example.inventorysystembackend.model.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest request) {

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        return category;
    }

    public CategoryResponse toDTO(Category category) {

        return new CategoryResponse(
                category.getCategoryID(),
                category.getName(),
                category.getDescription()
        );

    }
}
