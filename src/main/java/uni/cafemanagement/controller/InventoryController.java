package uni.cafemanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.cafemanagement.dto.InventoryProductDTO;
import uni.cafemanagement.model.InventoryProduct;
import uni.cafemanagement.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryProductDTO>> getAllInventoryProducts() {
        return ResponseEntity.ok(inventoryService.getAllInventoryProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryProductDTO> getInventoryProductById(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getInventoryProductById(id));
    }

    @PostMapping
    public ResponseEntity<InventoryProductDTO> addInventoryProduct(@RequestBody InventoryProduct inventoryProduct) {
        return ResponseEntity.ok(inventoryService.addInventoryProduct(inventoryProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryProductDTO> updateInventoryProduct(@PathVariable Long id, @RequestBody InventoryProduct updatedProduct) {
        return ResponseEntity.ok(inventoryService.updateInventoryProduct(id, updatedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventoryProduct(@PathVariable Long id) {
        inventoryService.deleteInventoryProduct(id);
        return ResponseEntity.noContent().build();
    }
}