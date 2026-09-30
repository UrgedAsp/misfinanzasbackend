package com.jcastillo.misfinanzas.service;

import com.jcastillo.misfinanzas.dto.Category.UpdateCategoryRequest;
import com.jcastillo.misfinanzas.exception.CategoryInUseException;
import com.jcastillo.misfinanzas.exception.CategoryNotFoundException;
import com.jcastillo.misfinanzas.model.Category;
import com.jcastillo.misfinanzas.model.CategoryType;
import com.jcastillo.misfinanzas.repository.CategoryRepository;
import com.jcastillo.misfinanzas.repository.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private CategoryService categoryService;

    // Aquí escribes tus @Test
    @Test
    void findById_WhenCategoryExists_ShouldReturnCategory() {
        // 1. ARRANGE (Preparar)
        // Creamos una categoría de mentiras
        Category category = new Category("Comida", CategoryType.EXPENSE, "#FF0000");
        category.setId(1L);

        // Le decimos al mock: "Oye categoryRepository, SI alguien te llama con findById(1L),
        // ENTONCES devuelve Optional con esta categoría"
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // 2. ACT (Actuar)
        // Llamamos al método real de nuestro servicio
        Category result = categoryService.findById(1L);

        // 3. ASSERT (Verificar)
        // Comprobamos que no sea nulo y que tenga los datos correctos
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Comida");
        assertThat(result.getType()).isEqualTo(CategoryType.EXPENSE);

        // Opcional pero pro: Verificamos que el servicio realmente consultó al repositorio exactamente 1 vez
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenCategoryDoesNotExist_ShouldThrowException() {
        // 1. ARRANGE
        // Le decimos al mock: "Si te piden el ID 99, devuelve vacío (no existe)"
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        // 2 y 3. ACT y ASSERT juntos
        // Verificamos que al llamar al método se lance CategoryNotFoundException
        assertThatThrownBy(() -> categoryService.findById(99L))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("99"); // opcional: verifica que el mensaje tenga el ID
    }

    @Test
    void deleteById_WhenCategoryIsInUse_ShouldThrowCategoryInUseException() {
        // 1. ARRANGE
        // Le decimos a transactionRepository: "Di que SÍ existe una transacción con categoryId = 1"
        when(transactionRepository.existsByCategoryId(1L)).thenReturn(true);

        // 2 y 3. ACT y ASSERT
        // Comprobamos que lance CategoryInUseException
        assertThatThrownBy(() -> categoryService.deleteById(1L))
                .isInstanceOf(CategoryInUseException.class);

        // Y LO MÁS IMPORTANTE: Verificamos que NUNCA intentó borrar la categoría
        verify(categoryRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteById_WhenCategoryIsNotUse_ShouldDelete(){
        // 1. ARRANGE
        // Le decimos a transactionRepository: "Di que NO existe una transacción con categoryId = 1"
        when(transactionRepository.existsByCategoryId(1L)).thenReturn(false);

        // 2. ACT (Actuar)
        // Llamamos al método real de nuestro servicio
        categoryService.deleteById(1L);

        // 3. ASSERT (Verificar)
        // Comprobamos que se eliminara la categoria
        verify(categoryRepository, times(1)).deleteById(1L);

    }


}