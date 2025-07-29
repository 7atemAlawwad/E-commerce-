package com.example.ecommerce.Service;

import com.example.ecommerce.Model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ProductService {

    ArrayList<Product> products = new ArrayList<>();
    private final CategoryService categoryList;

    public ArrayList<Product> getAllProducts() {
        return products;
    }

    public int addProduct(Product product) {
        // 1 = added, 0 = category not found, 2 = duplicate product id

        boolean categoryFound = false;
        for (int i = 0; i < categoryList.categories.size(); i++) {
            if (categoryList.categories.get(i).getId().equals(product.getCategoryID())) {
                categoryFound = true;
                break;
            }
        }
        if (!categoryFound) return 0;

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(product.getId())) {
                return 2;
            }
        }

        products.add(product);
        return 1;
    }



    // 1 = updated, 0 = category not found, 2 = new id already exists, 3 = product not found
    public int updateProduct(Product product, String id) {

        int indexToUpdate = -1;
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(id)) {
                indexToUpdate = i;
                break;
            }
        }
        if (indexToUpdate == -1) return 3;

        boolean categoryFound = false;
        for (int i = 0; i < categoryList.categories.size(); i++) {
            if (categoryList.categories.get(i).getId().equals(product.getCategoryID())) {
                categoryFound = true;
                break;
            }
        }
        if (!categoryFound) return 0;

        if (!product.getId().equals(id)) {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getId().equals(product.getId())) {
                    return 2;
                }
            }
        }

        products.set(indexToUpdate, product);
        return 1;
    }



    public boolean removeProduct(String id) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(id)) {
                products.remove(i);
                return true;
            }
        }
        return false;
    }
}
