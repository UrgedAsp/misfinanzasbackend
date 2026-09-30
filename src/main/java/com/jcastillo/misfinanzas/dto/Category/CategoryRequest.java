package com.jcastillo.misfinanzas.dto.Category;

import com.jcastillo.misfinanzas.model.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank @Size(max = 150) String name,
        @NotNull CategoryType type,
        String color
) {
}
