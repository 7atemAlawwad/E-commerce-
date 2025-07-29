package com.example.ecommerce.Controller;

import com.example.ecommerce.Api.ApiResponse;
import com.example.ecommerce.Model.Product;
import com.example.ecommerce.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productList;

    @GetMapping("/get")
    public ResponseEntity<?> getProductList(){
        return ResponseEntity.status(200).body(productList.getAllProducts());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@Valid @RequestBody Product product, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        if(productList.addProduct(product)== 1){
            return ResponseEntity.status(200).body(new ApiResponse("Product added successfully"));
        }
        else if (productList.addProduct(product)== 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Category not found"));
        }
        else {
            return ResponseEntity.status(400).body(new ApiResponse("Product already exists"));
        }


    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody Product product, Errors errors, @PathVariable String id) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        if (productList.updateProduct(product, id) == 1) {
            return ResponseEntity.status(200).body(new ApiResponse("Product updated successfully"));
        } else if (productList.updateProduct(product, id) == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Category not found"));
        } else {
            return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id){
        if(productList.removeProduct(id)){
            return ResponseEntity.status(200).body(new ApiResponse("Product deleted successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
    }



}
