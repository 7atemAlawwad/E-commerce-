package com.example.ecommerce.Service;

import com.example.ecommerce.Model.MerchantStock;
import com.example.ecommerce.Model.Product;
import com.example.ecommerce.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MerchantStockService {
    ArrayList<MerchantStock> merchantStocks =  new ArrayList<>();

    private final MerchantService merchantList;
    private final ProductService productList;
//    private final UserService userList;


    public ArrayList<MerchantStock> getAllMerchantsStocks() {
        return merchantStocks;
    }


    public int addMerchantStock(MerchantStock ms) {

        boolean productFound = false;
        for (int i = 0; i < productList.products.size(); i++) {
            if (productList.products.get(i).getId().equals(ms.getProductid())) {
                productFound = true;
                break;
            }
        }
        if (!productFound) return 0;

        boolean merchantFound = false;
        for (int i = 0; i < merchantList.merchants.size(); i++) {
            if (merchantList.merchants.get(i).getId().equals(ms.getMerchantid())) {
                merchantFound = true;
                break;
            }
        }
        if (!merchantFound) return -1;

        for (int i = 0; i < merchantStocks.size(); i++) {
            MerchantStock existing = merchantStocks.get(i);
            if (existing.getMerchantid().equals(ms.getMerchantid())
                    && existing.getProductid().equals(ms.getProductid())) {
                return 2;
            }
        }

        merchantStocks.add(ms);
        return 1;
    }


    public int updateMerchantStock(MerchantStock ms, String id) {
        // 1 = done
        // -1 = merchant missing
        // 0  = product missing
        // 2  = stock row missing


        int indexToUpdate = -1;

        for (int i = 0; i < merchantStocks.size(); i++) {
            if (merchantStocks.get(i).getId().equals(id)) {
                indexToUpdate = i;
                break;
            }
        }
        if (indexToUpdate == -1) return 2;

        boolean productFound = false;
        for (int i = 0; i < productList.products.size(); i++) {
            if (productList.products.get(i).getId().equals(ms.getProductid())) {
                productFound = true;
                break;
            }
        }
        if (!productFound) return 0;

        boolean merchantFound = false;
        for (int i = 0; i < merchantList.merchants.size(); i++) {
            if (merchantList.merchants.get(i).getId().equals(ms.getMerchantid())) {
                merchantFound = true;
                break;
            }
        }
        if (!merchantFound) return -1;


        merchantStocks.set(indexToUpdate, ms);
        return 1;
    }

    public boolean removeMerchantStock(String id) {
        for (int i = 0; i < merchantStocks.size(); i++) {
            if (merchantStocks.get(i).getId().equals(id)) {
                merchantStocks.remove(i);
                return true;
            }
        }
        return false;
    }






}
