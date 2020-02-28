package com.wheredidmymoneygo.service;

import com.wheredidmymoneygo.dto.Category;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.wheredidmymoneygo.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {

    private CategoryRepository categoryRepository;

    public ResponseEntity saveCategory(Category category) {
        Optional<Category> catAlreadyPresent = categoryRepository.findByName(category.getName());
        catAlreadyPresent.orElseGet(() -> categoryRepository.insert(category));
        return new ResponseEntity(HttpStatus.CREATED);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public ResponseEntity updateGoal(Category category) {
        Optional<Category> categoryEntry = categoryRepository.findByName(category.getName());
        categoryEntry.ifPresent(cat -> {
            cat.setGoal(category.getGoal());
            categoryRepository.save(cat);
        });
        return categoryEntry.isPresent() ? new ResponseEntity(HttpStatus.OK) : new ResponseEntity("Category not found", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity deleteCategory(String name) {
        Optional<Category> categoryEntry = categoryRepository.findByName(name);
        categoryEntry.ifPresent(cat -> categoryRepository.delete(cat));
        return categoryEntry.isPresent() ? new ResponseEntity(HttpStatus.OK) : new ResponseEntity("Category not found", HttpStatus.BAD_REQUEST);
    }
}
