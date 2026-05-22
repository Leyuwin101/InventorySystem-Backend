package com.example.inventorysystembackend.dto.nested;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategoryDTO {

    private Long categoryId;

    private String name;
}
