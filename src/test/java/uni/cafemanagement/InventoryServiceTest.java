package uni.cafemanagement;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uni.cafemanagement.model.InventoryProduct;
import uni.cafemanagement.repository.InventoryProductRepository;
import uni.cafemanagement.service.InventoryService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryServiceTest {

    @Mock
    private InventoryProductRepository inventoryProductRepository;

    @InjectMocks
    private InventoryService inventoryService;

    public InventoryServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllInventoryProducts_shouldReturnAllProducts() {
        InventoryProduct product1 = new InventoryProduct("Milk", "Cow milk", null, 10.0, 20);
        InventoryProduct product2 = new InventoryProduct("Coffee", "Coffee beans", null, 15.0, 30);
        when(inventoryProductRepository.findAll()).thenReturn(List.of(product1, product2));

        List<InventoryProduct> products = inventoryService.getAllInventoryProducts();

        assertEquals(2, products.size());
        verify(inventoryProductRepository, times(1)).findAll();
    }

    @Test
    void updateInventoryProductQuantity_shouldUpdateQuantity() {
        InventoryProduct product = new InventoryProduct("Milk", "Cow milk", null, 10.0, 20);
        when(inventoryProductRepository.findById(1L)).thenReturn(Optional.of(product));

        inventoryService.updateInventoryProductQuantity(1L, 15);

        assertEquals(15.0, product.getQuantity());
        verify(inventoryProductRepository, times(1)).save(product);
    }

    @Test
    void getLowStockProducts_shouldReturnProductsWithLowStock() {
        InventoryProduct product = new InventoryProduct("Milk", "Cow milk", null, 10.0, 5.0);
        when(inventoryProductRepository.findByQuantityLessThan(10.0)).thenReturn(List.of(product));

        List<InventoryProduct> lowStockProducts = inventoryService.getLowStockProducts();

        assertEquals(1, lowStockProducts.size());
        assertEquals("Milk", lowStockProducts.get(0).getName());
        verify(inventoryProductRepository, times(1)).findByQuantityLessThan(10.0);
    }
}