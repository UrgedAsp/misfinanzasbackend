package com.jcastillo.misfinanzas.controller;

import com.jcastillo.misfinanzas.dto.Budget.BudgetRequest;
import com.jcastillo.misfinanzas.dto.Budget.BudgetResponse;
import com.jcastillo.misfinanzas.dto.Budget.UpdateBudgetRequest;
import com.jcastillo.misfinanzas.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetResponse> create(@RequestBody @Valid BudgetRequest budgetRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetService.save(budgetRequest));
    }

    @GetMapping
    public List<BudgetResponse> findAll(){
        return budgetService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(budgetService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponse> update(@PathVariable Long id, @RequestBody @Valid UpdateBudgetRequest updateBudgetRequest){
        return ResponseEntity.ok(budgetService.update(id, updateBudgetRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        budgetService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
