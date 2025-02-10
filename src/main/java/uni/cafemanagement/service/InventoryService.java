package uni.cafemanagement.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import uni.cafemanagement.exception.ApiRequestException;
import uni.cafemanagement.mapper.InventoryProductMapper;
import uni.cafemanagement.dto.InventoryProductDTO;
import uni.cafemanagement.model.InventoryProduct;
import uni.cafemanagement.repository.InventoryProductRepository;
import uni.simulatedpos.model.MenuProduct;
import uni.simulatedpos.repository.MenuProductRepository;
import uni.simulatedpos.repository.TransactionRepository;

import java.util.List;
import java.util.stream.Collectors;

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

    public void updateInventoryProductQuantity(Long productId, int quantity) {
        InventoryProduct product = inventoryProductRepository.findById(productId)
                .orElseThrow(() -> new ApiRequestException("Product not found"));
        product.setQuantity(quantity);
        inventoryProductRepository.save(product);
    }

    public List<InventoryProductDTO> getLowStockProducts() {
        List<InventoryProduct> lowStockProducts = inventoryProductRepository.findByQuantityLessThan(10.0);
        return lowStockProducts.stream()
                .map(InventoryProductMapper.MAPPER::toInventoryProductDTO)
                .collect(Collectors.toList());
    }

    public void alertLowStockProducts() {
        List<InventoryProductDTO> lowStockProducts = getLowStockProducts();
        if (!lowStockProducts.isEmpty()) {
            lowStockProducts.forEach(product ->
                    System.out.println("ALERT: Low stock for product: " + product.getName() + ", Quantity: " + product.getQuantity()));
        }
    }

    public List<InventoryProductDTO> getAllInventoryProducts() {
        List<InventoryProduct> products = inventoryProductRepository.findAll();
        return products.stream()
                .map(InventoryProductMapper.MAPPER::toInventoryProductDTO)
                .collect(Collectors.toList());
    }

    public InventoryProductDTO getInventoryProductById(Long id) {
        InventoryProduct product = inventoryProductRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("InventoryProduct not found with ID: " + id));
        return InventoryProductMapper.MAPPER.toInventoryProductDTO(product);
    }

    public InventoryProductDTO addInventoryProduct(InventoryProduct inventoryProduct) {
        InventoryProduct savedProduct = inventoryProductRepository.save(inventoryProduct);
        return InventoryProductMapper.MAPPER.toInventoryProductDTO(savedProduct);
    }

    @Transactional
    public InventoryProductDTO updateInventoryProduct(Long id, InventoryProduct updatedProduct) {
        InventoryProduct existingProduct = inventoryProductRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("InventoryProduct not found with ID: " + id));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setCategory(updatedProduct.getCategory());

        InventoryProduct savedProduct = inventoryProductRepository.save(existingProduct);
        return InventoryProductMapper.MAPPER.toInventoryProductDTO(savedProduct);
    }

    public void deleteInventoryProduct(Long id) {
        if (!inventoryProductRepository.existsById(id)) {
            throw new ApiRequestException("InventoryProduct not found with ID: " + id);
        }
        inventoryProductRepository.deleteById(id);
    }
}