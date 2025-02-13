package uni.simulatedpos.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.cafemanagement.model.InventoryProduct;
import uni.cafemanagement.repository.InventoryProductRepository;
import uni.simulatedpos.dto.MenuProductDTO;
import uni.simulatedpos.dto.MenuProductIngredientDTO;
import uni.simulatedpos.model.MenuProduct;
import uni.simulatedpos.model.MenuProductCategory;
import uni.simulatedpos.repository.MenuProductCategoryRepository;
import uni.simulatedpos.repository.MenuProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuProductService {

    private final MenuProductRepository menuProductRepository;
    private final MenuProductCategoryRepository menuProductCategoryRepository;
    private final InventoryProductRepository inventoryProductRepository;

    @Autowired
    public MenuProductService(MenuProductRepository menuProductRepository, MenuProductCategoryRepository menuProductCategoryRepository, InventoryProductRepository inventoryProductRepository) {
        this.menuProductRepository = menuProductRepository;
        this.menuProductCategoryRepository = menuProductCategoryRepository;
        this.inventoryProductRepository = inventoryProductRepository;
    }

    public List<MenuProduct> getAllProducts() {
        return menuProductRepository.findAll();
    }

    public MenuProduct getProductById(Long id) {
        return menuProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }

    public MenuProduct createProduct(MenuProductDTO menuProductDTO) {
        MenuProductCategory category = menuProductCategoryRepository.findById(menuProductDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        MenuProduct newProduct = new MenuProduct(
                menuProductDTO.getName(),
                menuProductDTO.getDescription(),
                category,
                BigDecimal.valueOf(menuProductDTO.getPrice()),
                menuProductDTO.getQuantity()
        );

        return menuProductRepository.save(newProduct);
    }

    @Transactional
    public MenuProduct addIngredientsToProduct(Long productId, List<MenuProductIngredientDTO> ingredientDTOs) {
        MenuProduct product = menuProductRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getIngredients() == null) {
            product.setIngredients(new HashMap<>());
        }

        for (MenuProductIngredientDTO dto : ingredientDTOs) {
            InventoryProduct inventoryProduct = inventoryProductRepository.findById(dto.getInventoryProductId())
                    .orElseThrow(() -> new RuntimeException("Inventory product not found"));

            if (product.getIngredients().containsKey(inventoryProduct)) {
                product.getIngredients().compute(inventoryProduct, (k, existingQuantity) -> existingQuantity + dto.getQuantity());
            } else {
                product.getIngredients().put(inventoryProduct, dto.getQuantity());
            }
        }

        return menuProductRepository.save(product);
    }

    public MenuProduct updateProduct(Long id, MenuProduct updatedProduct) {
        MenuProduct existingProduct = menuProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQuantity(updatedProduct.getQuantity());

        return menuProductRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        menuProductRepository.deleteById(id);
    }
}