package com.example.ecommerce.Controller;

import com.example.ecommerce.Api.ApiResponse;
import com.example.ecommerce.Model.User;
import com.example.ecommerce.Service.MerchantStockService;
import com.example.ecommerce.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userList;
    private final MerchantStockService merchantStockList;


    @GetMapping("/get")
    public ResponseEntity<?> getUserList(){
        return ResponseEntity.status(200).body(userList.getAllUserList());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@Valid @RequestBody User user, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        userList.addUser(user);
        return ResponseEntity.status(200).body(new ApiResponse("User added successfully"));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user, Errors errors, @PathVariable String id){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        if(userList.updateUser(user, id)){
            return ResponseEntity.status(200).body(new ApiResponse("User updated successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("User not found"));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id){
        if(userList.removeUser(id)){
            return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("User not found"));
    }


    @PutMapping("/purchase/direct")
    public ResponseEntity<?> buyDirect(@RequestParam String userId, @RequestParam String productId, @RequestParam String merchantId) {


        switch (userList.buyDirect(userId, productId, merchantId)) {
            case 1:
                return ResponseEntity.status(200).body(new ApiResponse("Purchase completed"));
            case 0:
                return ResponseEntity.status(400).body(new ApiResponse("User not found"));
            case -1:
                return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
            case -2:
                return ResponseEntity.status(400).body(new ApiResponse("Merchant not found"));
            case -3:
                return ResponseEntity.status(400).body(new ApiResponse("Merchant has no stock for this product"));
            case -4:
                return ResponseEntity.status(400).body(new ApiResponse("Insufficient balance"));
            case -5:
                return ResponseEntity.status(400).body(new ApiResponse("MerchantStock row not found"));
            case -6:
                return ResponseEntity.status(400).body(new ApiResponse("Merchant is on vacation"));
            default:
                return ResponseEntity.status(400).body(new ApiResponse("Unexpected error"));
        }


    }

    @PutMapping("/purchase/random")
    public ResponseEntity<?> buyRandomInRange(@RequestParam String userId, @RequestParam String categoryId, @RequestParam double minPrice, @RequestParam double maxPrice) {

        if (minPrice <= 0 || maxPrice <= 0 || minPrice > maxPrice) {
            return ResponseEntity.status(400)
                    .body(new ApiResponse("Price is not correct"));
        }


        switch (userList.buyRandomInRange(userId, categoryId, minPrice, maxPrice)) {
            case 1:
                return ResponseEntity.status(200).body(new ApiResponse("Purchase completed"));
            case 0:
                return ResponseEntity.status(400).body(new ApiResponse("user not found"));
            case -1:
                return ResponseEntity.status(400).body(new ApiResponse("category not found"));
            case -2:
                return ResponseEntity.status(400).body(new ApiResponse("there is no product in this range"));
            case -3:
                return ResponseEntity.status(400).body(new ApiResponse("stock is empty"));
            case -4:
                return ResponseEntity.status(400).body(new ApiResponse("balance insufficient"));
            default:
                return ResponseEntity.status(400).body(new ApiResponse("Unexpected error"));
        }
    }

    @PostMapping("/waitlist/join")
    public ResponseEntity<?> joinWaitlist(@RequestParam String userId,
                                                    @RequestParam String productId,
                                                    @RequestParam String merchantId,
                                                    @RequestParam boolean autoBuy) {


        switch (userList.joinWaitlist(userId, productId, merchantId, autoBuy)) {
            case 1:
                return ResponseEntity.status(200).body(new ApiResponse("waitlist joined successfully"));
            case 0:
                return ResponseEntity.status(400).body(new ApiResponse("user not found"));
            case -1:
                return ResponseEntity.status(400).body(new ApiResponse("product not found"));
            case -2:
                return ResponseEntity.status(400).body(new ApiResponse("merchant not found"));
            case 2:
                return ResponseEntity.status(400).body(new ApiResponse("already waitlisted"));
            default:
                return ResponseEntity.status(400).body(new ApiResponse("Unexpected error"));
        }
    }

    @PostMapping("/payment/split")
    public ResponseEntity<?> splitPayment(@RequestParam String productId,
                                                    @RequestParam String merchantId,
                                                    @RequestParam ArrayList<String> userIds,
                                                    @RequestParam ArrayList<Double> amounts) {

        int code = userList.splitBuyOneItem(productId, merchantId, userIds, amounts);

        switch (code) {
            case 1:  return ResponseEntity.ok(new ApiResponse("Payment completed and item purchased"));
            case 0:  return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
            case -1: return ResponseEntity.status(400).body(new ApiResponse("Merchant not found"));
            case -2: return ResponseEntity.status(400).body(new ApiResponse("Stock row not found"));
            case -3: return ResponseEntity.status(400).body(new ApiResponse("No stock available"));
            case -4: return ResponseEntity.status(400).body(new ApiResponse("userIds and amounts mismatch or empty"));
            case -5: return ResponseEntity.status(400).body(new ApiResponse("Split amounts do not equal product price"));
            case -6: return ResponseEntity.status(404).body(new ApiResponse("One or more users not found"));
            case -7: return ResponseEntity.status(400).body(new ApiResponse("Insufficient balance for one or more users"));
            default: return ResponseEntity.status(400).body(new ApiResponse("Unexpected error"));
        }
    }

    @PostMapping("/loyalty")
    public ResponseEntity<?> redeemPoints(@RequestParam String userId,
                                                    @RequestParam int points) {

        int code = userList.redeemPoints(userId, points);

        switch (code) {
            case 1:  return ResponseEntity.ok(new ApiResponse("Points redeemed successfully"));
            case 0:  return ResponseEntity.status(404).body(new ApiResponse("User not found"));
            case -1: return ResponseEntity.status(400).body(new ApiResponse("Points must be > 0"));
            case -2: return ResponseEntity.status(400).body(new ApiResponse("Not enough points"));
            default: return ResponseEntity.status(500).body(new ApiResponse("Unexpected error"));
        }
    }


    @PutMapping("/stock/add")
    public ResponseEntity<?> addMoreStock(@RequestParam String merchantId, @RequestParam String productId, @RequestParam int amount) {

        if (amount <= 0) {
            return ResponseEntity.status(400)
                    .body(new ApiResponse("amount must be positive"));
        }

        switch (userList.addMoreStock(merchantId, productId, amount)) {
            case 1:
                return ResponseEntity.status(200).body(new ApiResponse("Stock increased successfully"));
            case 0:
                return ResponseEntity.status(400).body(new ApiResponse("Product does not exist"));
            case -1:
                return ResponseEntity.status(400).body(new ApiResponse("Merchant does not exist"));
            case 2:
                return ResponseEntity.status(400).body(new ApiResponse("Merchant Stock not found"));
            case -2:
                return ResponseEntity.status(400).body(new ApiResponse("Amount invalid"));
            default:
                return ResponseEntity.status(400).body(new ApiResponse("Unexpected error"));
        }
    }






}
