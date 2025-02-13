package uni.cafemanagement.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import uni.cafemanagement.dto.InventoryProductDTO;
import uni.cafemanagement.exception.ApiRequestException;
import uni.cafemanagement.model.InventoryProduct;
import uni.cafemanagement.model.ProductCategory;
import uni.cafemanagement.repository.InventoryProductRepository;
import uni.cafemanagement.repository.ProductCategoryRepository;
import uni.simulatedpos.model.MenuProduct;
import uni.simulatedpos.repository.MenuProductRepository;
import uni.simulatedpos.repository.TransactionRepository;

import java.util.List;

@Service
public class InventoryService {

    private final MenuProductRepository menuProductRepository;
    private final InventoryProductRepository inventoryProductRepository;
    private final TransactionRepository transactionRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public InventoryService(MenuProductRepository menuProductRepository,
                            InventoryProductRepository inventoryProductRepository,
                            TransactionRepository transactionRepository, ProductCategoryRepository productCategoryRepository) {
        this.menuProductRepository = menuProductRepository;
        this.inventoryProductRepository = inventoryProductRepository;
        this.transactionRepository = transactionRepository;
        this.productCategoryRepository = productCategoryRepository;
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

    @Transactional
    public InventoryProduct addInventoryProduct(InventoryProductDTO inventoryProductDTO) {
        ProductCategory category = productCategoryRepository.findById(inventoryProductDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        InventoryProduct inventoryProduct;
        if (inventoryProductDTO.isCountable()) {
            inventoryProduct = InventoryProduct.createCountableInventoryProduct(
                    inventoryProductDTO.getName(),
                    inventoryProductDTO.getDescription(),
                    category,
                    inventoryProductDTO.getPrice(),
                    (int) inventoryProductDTO.getQuantity(),
                    inventoryProductDTO.getMinimalValue()
            );
        } else {
            inventoryProduct = InventoryProduct.createNonCountableInventoryProduct(
                    inventoryProductDTO.getName(),
                    inventoryProductDTO.getDescription(),
                    category,
                    inventoryProductDTO.getPrice(),
                    inventoryProductDTO.getWeightInGrams(),
                    inventoryProductDTO.getMinimalValue()
            );
        }

        return inventoryProductRepository.save(inventoryProduct);
    }

    @Transactional
    public InventoryProduct updateInventoryProduct(Long id, InventoryProductDTO updatedProductDTO) {
        InventoryProduct existingProduct = inventoryProductRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("InventoryProduct not found with ID: " + id));

        if (updatedProductDTO.getCategoryId() != null) {
            ProductCategory category = productCategoryRepository.findById(updatedProductDTO.getCategoryId())
                    .orElseThrow(() -> new ApiRequestException("Category not found with ID: " + updatedProductDTO.getCategoryId()));
            existingProduct.setCategory(category);
        }

        existingProduct.setName(updatedProductDTO.getName());
        existingProduct.setPrice(updatedProductDTO.getPrice());
        existingProduct.setMinimalValue(updatedProductDTO.getMinimalValue());

        if (updatedProductDTO.isCountable()) {
            existingProduct.setQuantity(updatedProductDTO.getQuantity());
            existingProduct.setWeightInGrams(0);
            existingProduct.setCountable(true);
        } else {
            existingProduct.setWeightInGrams(updatedProductDTO.getWeightInGrams());
            existingProduct.setQuantity(0);
            existingProduct.setCountable(false);
        }

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