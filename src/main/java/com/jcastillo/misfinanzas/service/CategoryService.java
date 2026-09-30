package com.jcastillo.misfinanzas.service;

import com.jcastillo.misfinanzas.dto.Category.UpdateCategoryRequest;
import com.jcastillo.misfinanzas.exception.CategoryInUseException;
import com.jcastillo.misfinanzas.exception.CategoryNotFoundException;
import com.jcastillo.misfinanzas.model.Category;
import com.jcastillo.misfinanzas.repository.CategoryRepository;
import com.jcastillo.misfinanzas.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) throws CategoryNotFoundException {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Category update(Long id, UpdateCategoryRequest request) throws CategoryNotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        category.setName(request.name());
        category.setType(request.type());
        category.setColor(request.color());

        return categoryRepository.save(category);
    }

    public void deleteById(Long id) throws CategoryInUseException {
        if(transactionRepository.existsByCategoryId(id)){
            throw new CategoryInUseException(id);
        }
        categoryRepository.deleteById(id);
    }
}
