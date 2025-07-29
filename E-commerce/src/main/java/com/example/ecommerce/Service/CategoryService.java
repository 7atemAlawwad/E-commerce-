package com.example.ecommerce.Service;

import com.example.ecommerce.Model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CategoryService {

    ArrayList<Category> categories = new ArrayList<>();


    public ArrayList<Category> getCategories() {
        return categories;
    }
    public void addCategory(Category category) {
        categories.add(category);
    }

    public boolean updateCategory(Category category, String id) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId().equals(id)) {
                categories.set(i, category);
                return true;
            }

        }
        return false;
    }

    public boolean removeCategory(String id) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId().equals(id)) {
                categories.remove(i);
                return true;
            }
        }
        return false;
    }

}
