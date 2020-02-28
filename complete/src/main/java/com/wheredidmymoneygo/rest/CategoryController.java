package com.wheredidmymoneygo.rest;

import com.wheredidmymoneygo.dto.Category;
import com.wheredidmymoneygo.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@AllArgsConstructor
@RequestMapping("/category")
public class CategoryController {

	private CategoryService categoryService;

	@PostMapping()
	public ResponseEntity addCategory(@Valid @RequestBody Category category) {
		return categoryService.saveCategory(category);
	}

	@PutMapping()
	public ResponseEntity updateCategory(@Valid @RequestBody Category category) {
		return categoryService.updateGoal(category);
	}

	@DeleteMapping("/{name}")
	public ResponseEntity deleteCategory(@PathVariable String name) {
		return categoryService.deleteCategory(name);
	}

	@GetMapping("/allCategories")
	public List<Category> getAllCategories() {
		return categoryService.getAllCategories();
	}

}
