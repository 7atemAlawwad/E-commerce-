package com.example.ecommerce.Controller;

import com.example.ecommerce.Api.ApiResponse;
import com.example.ecommerce.Model.Category;
import com.example.ecommerce.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryList;

    @GetMapping("/get")
    public ResponseEntity<?> getCategoryList(){
        return ResponseEntity.status(200).body(categoryList.getCategories());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@Valid @RequestBody Category category, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        categoryList.addCategory(category);
        return ResponseEntity.status(200).body(new ApiResponse("Category added successfully"));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateCategory(@Valid @RequestBody Category category, Errors errors, @PathVariable String id){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        boolean isUpdated = categoryList.updateCategory(category, id);
        if(isUpdated){
            return ResponseEntity.status(200).body(new ApiResponse("Category updated successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Category not found"));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id){
        if(categoryList.removeCategory(id)){
            return ResponseEntity.status(200).body(new ApiResponse("Category deleted successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Category not found"));
    }


}
