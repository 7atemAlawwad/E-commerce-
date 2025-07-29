package com.example.ecommerce.Controller;

import com.example.ecommerce.Api.ApiResponse;
import com.example.ecommerce.Model.Merchant;
import com.example.ecommerce.Service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantList;


    @GetMapping("/get")
    public ResponseEntity<?> getMerchantList(){
        return ResponseEntity.status(200).body(merchantList.getAllMerchants());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMerchant(@Valid @RequestBody Merchant merchant, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        merchantList.addMerchant(merchant);
        return ResponseEntity.status(200).body(new ApiResponse("Merchant added successfully"));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateMerchant(@Valid @RequestBody Merchant merchant, Errors errors, @PathVariable String id){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        boolean isUpdated = merchantList.updateMerchant(merchant, id);
        if(isUpdated){
            return ResponseEntity.status(200).body(new ApiResponse("Merchant updated successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Merchant not found"));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteMerchant(@PathVariable String id){
        if(merchantList.removeMerchant(id)){
            return ResponseEntity.status(200).body(new ApiResponse("Merchant deleted successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Merchant not found"));
    }

    @PutMapping("/vacation")
    public ResponseEntity<?> setVacation(@RequestParam String merchantId,
                                                   @RequestParam(required = false)
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate vacationFrom,
                                                   @RequestParam(required = false)
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate vacationTo) {

        switch (merchantList.setVacation(merchantId, vacationFrom, vacationTo)) {
            case 1:
                if (vacationFrom == null && vacationTo == null) {
                    return ResponseEntity.status(200).body(new ApiResponse("Vacation cleared"));
                }
                return ResponseEntity.status(200).body(new ApiResponse("Vacation saved"));
            case 0:
                return ResponseEntity.status(400).body(new ApiResponse("Merchant not found"));
            case -1:
                return ResponseEntity.status(400).body(new ApiResponse(
                        "Provide both dates or none"));
            case -2:
                return ResponseEntity.status(400).body(new ApiResponse(
                        "vacationTo must be on or after vacationFrom"));
            default:
                return ResponseEntity.status(400).body(new ApiResponse("Unexpected error"));
        }
    }

}
