package uni.simulatedpos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.simulatedpos.model.MenuProductCategory;
import uni.simulatedpos.service.MenuProductCategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/menu-product-categories")
public class MenuProductCategoryController {

    private final MenuProductCategoryService menuProductCategoryService;

    @Autowired
    public MenuProductCategoryController(MenuProductCategoryService menuProductCategoryService) {
        this.menuProductCategoryService = menuProductCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<MenuProductCategory>> getAllCategories() {
        List<MenuProductCategory> categories = menuProductCategoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuProductCategory> getCategoryById(@PathVariable Long id) {
        MenuProductCategory category = menuProductCategoryService.getCategoryById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MenuProductCategory> createCategory(@RequestBody String menuProductCategory) {
        MenuProductCategory createdCategory = menuProductCategoryService.createCategory(menuProductCategory);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuProductCategory> updateCategory(
            @PathVariable Long id, @RequestBody String updatedCategory) {
        MenuProductCategory category = menuProductCategoryService.updateCategory(id, updatedCategory);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        menuProductCategoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}