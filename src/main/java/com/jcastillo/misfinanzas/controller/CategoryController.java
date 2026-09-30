package com.jcastillo.misfinanzas.controller;

import com.jcastillo.misfinanzas.dto.Category.CategoryRequest;
import com.jcastillo.misfinanzas.dto.Category.CategoryResponse;
import com.jcastillo.misfinanzas.dto.Category.UpdateCategoryRequest;
import com.jcastillo.misfinanzas.model.Category;
import com.jcastillo.misfinanzas.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody @Valid CategoryRequest categoryRequest) {
        Category category = new Category(categoryRequest.name(), categoryRequest.type(), categoryRequest.color());
        Category categorySaved = categoryService.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoryResponse.from(categorySaved));
    }

    @GetMapping
    public List<CategoryResponse> findAll() {
        return categoryService.findAll().stream().map(CategoryResponse::from).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        return ResponseEntity.ok(CategoryResponse.from(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id, @RequestBody @Valid UpdateCategoryRequest updateCategoryRequest) {
        Category category = categoryService.update(id, updateCategoryRequest);
        return ResponseEntity.ok(CategoryResponse.from(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
