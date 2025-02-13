package uni.simulatedpos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.simulatedpos.dto.MenuProductDTO;
import uni.simulatedpos.dto.MenuProductIngredientDTO;
import uni.simulatedpos.model.MenuProduct;
import uni.simulatedpos.service.MenuProductService;

import java.util.List;

@RestController
@RequestMapping("/api/menu-products")
public class MenuProductController {

    private final MenuProductService menuProductService;

    @Autowired
    public MenuProductController(MenuProductService menuProductService) {
        this.menuProductService = menuProductService;
    }

    @GetMapping
    public ResponseEntity<List<MenuProduct>> getAllProducts() {
        List<MenuProduct> products = menuProductService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuProduct> getProductById(@PathVariable Long id) {
        MenuProduct product = menuProductService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MenuProduct> createProduct(@RequestBody MenuProductDTO menuProduct) {
        MenuProduct createdProduct = menuProductService.createProduct(menuProduct);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuProduct> updateProduct(
            @PathVariable Long id, @RequestBody MenuProductDTO updatedProduct) {
        MenuProduct product = menuProductService.updateProduct(id, updatedProduct);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        menuProductService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{productId}/add-ingredients")
    public ResponseEntity<MenuProduct> addIngredientsToProduct(
            @PathVariable Long productId,
            @RequestBody List<MenuProductIngredientDTO> ingredientDTOs) {
        MenuProduct updatedProduct = menuProductService.addIngredientsToProduct(productId, ingredientDTOs);
        return ResponseEntity.ok(updatedProduct);
    }
}