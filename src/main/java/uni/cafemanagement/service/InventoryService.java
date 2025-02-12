package uni.cafemanagement.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import uni.cafemanagement.exception.ApiRequestException;
import uni.cafemanagement.model.InventoryProduct;
import uni.cafemanagement.repository.InventoryProductRepository;
import uni.simulatedpos.model.MenuProduct;
import uni.simulatedpos.repository.MenuProductRepository;
import uni.simulatedpos.repository.TransactionRepository;

import java.util.List;

@Service
public class InventoryService {

    private final MenuProductRepository menuProductRepository;
    private final InventoryProductRepository inventoryProductRepository;
    private final TransactionRepository transactionRepository;

    public InventoryService(MenuProductRepository menuProductRepository,
                            InventoryProductRepository inventoryProductRepository,
                            TransactionRepository transactionRepository) {
        this.menuProductRepository = menuProductRepository;
        this.inventoryProductRepository = inventoryProductRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<MenuProduct> getAllMenuProducts() {
        return menuProductRepository.findAll();
    }

    public void addMenuProduct(MenuProduct product) {
        menuProductRepository.save(product);
    }

    public void deleteMenuProduct(Long productId) {
        menuProductRepository.deleteById(productId);
    }

    public List<InventoryProduct> getAllInventoryProducts() {
        return inventoryProductRepository.findAll();
    }

    public InventoryProduct getInventoryProductById(Long id) {
        return inventoryProductRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("InventoryProduct not found with ID: " + id));
    }

    public InventoryProduct addInventoryProduct(InventoryProduct inventoryProduct) {
        return inventoryProductRepository.save(inventoryProduct);
    }

    @Transactional
    public InventoryProduct updateInventoryProduct(Long id, InventoryProduct updatedProduct) {
        InventoryProduct existingProduct = inventoryProductRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("InventoryProduct not found with ID: " + id));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setCategory(updatedProduct.getCategory());

        return inventoryProductRepository.save(existingProduct);
    }

    public void deleteInventoryProduct(Long id) {
        if (!inventoryProductRepository.existsById(id)) {
            throw new ApiRequestException("InventoryProduct not found with ID: " + id);
        }
        inventoryProductRepository.deleteById(id);
    }

    public void updateInventoryProductQuantity(Long productId, int quantity) {
        InventoryProduct product = inventoryProductRepository.findById(productId)
                .orElseThrow(() -> new ApiRequestException("Product not found"));
        product.setQuantity(quantity);
        inventoryProductRepository.save(product);
    }

    // Metody do monitorowania stanów magazynowych
    public List<InventoryProduct> getLowStockProducts() {
        return inventoryProductRepository.findByQuantityLessThan(10.0);
    }

    public void alertLowStockProducts() {
        List<InventoryProduct> lowStockProducts = getLowStockProducts();
        if (!lowStockProducts.isEmpty()) {
            lowStockProducts.forEach(product ->
                    System.out.println("ALERT: Low stock for product: " + product.getName() + ", Quantity: " + product.getQuantity()));
        }
    }
}