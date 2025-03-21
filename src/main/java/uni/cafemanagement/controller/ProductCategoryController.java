package uni.cafemanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.cafemanagement.model.ProductCategory;
import uni.cafemanagement.service.ProductCategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<ProductCategory>> getAllCategories() {
        return ResponseEntity.ok(productCategoryService.getAllCategories());
    }

    @PostMapping
    public ResponseEntity<ProductCategory> createCategory(@RequestBody String productCategoryName) {
        return ResponseEntity.ok(productCategoryService.createCategory(productCategoryName));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductCategory> updateCategory(@PathVariable Long id, @RequestBody String productCategoryName) {
        return ResponseEntity.ok(productCategoryService.updateCategory(id, productCategoryName));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        productCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}