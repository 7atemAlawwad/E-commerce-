package com.example.ecommerce.Controller;

import com.example.ecommerce.Api.ApiResponse;
import com.example.ecommerce.Model.MerchantStock;
import com.example.ecommerce.Service.MerchantStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/merchant-stock")
@RequiredArgsConstructor
public class MerchantStockController {

    public final MerchantStockService merchantStockList;


    @GetMapping("/get")
    public ResponseEntity<?> getMerchantStockList() {
        return ResponseEntity.status(200).body(merchantStockList.getAllMerchantsStocks());
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> add(@Valid @RequestBody MerchantStock ms, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        switch (merchantStockList.addMerchantStock(ms)) {
            case 1:  return ResponseEntity.ok(new ApiResponse("Merchant Stock added"));
            case 0:  return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
            case -1: return ResponseEntity.status(400).body(new ApiResponse("Merchant not found"));
            case 2:  return ResponseEntity.status(400).body(new ApiResponse("Row already exists"));
            default: return ResponseEntity.status(400).body(new ApiResponse("Unexpected error"));
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateMerchantStock(@Valid @RequestBody MerchantStock merchantStock, Errors errors, @PathVariable String id) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        if (merchantStockList.updateMerchantStock(merchantStock, id) == 1) {
            return ResponseEntity.status(200).body(new ApiResponse("Merchant Stock updated successfully"));
        } else if (merchantStockList.updateMerchantStock(merchantStock, id) == -1) {
            return ResponseEntity.status(400).body("Merchant not found");
        } else if (merchantStockList.updateMerchantStock(merchantStock, id) == 0) {
            return ResponseEntity.status(400).body("product not found");
        } else {
            return ResponseEntity.status(400).body("Merchant Stock not found");
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteMerchantStock(@PathVariable String id) {
        if (merchantStockList.removeMerchantStock(id)) {
            return ResponseEntity.status(200).body(new ApiResponse("Merchant Stock deleted successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Merchant Stock not found"));
    }



}

