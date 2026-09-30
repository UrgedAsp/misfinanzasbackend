package com.jcastillo.misfinanzas.dto.Category;

import com.jcastillo.misfinanzas.model.Category;
import com.jcastillo.misfinanzas.model.CategoryType;

public record CategoryResponse(
        Long id,
        String name,
        CategoryType type,
        String color
) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getType(), category.getColor());
    }
}
