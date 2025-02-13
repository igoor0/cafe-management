package uni.cafemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.cafemanagement.model.InventoryProduct;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryProductRepository extends JpaRepository<InventoryProduct, Integer> {
    List<InventoryProduct> findByQuantityLessThan(double v);

    Optional<InventoryProduct> findById(Long productId);

    Optional<InventoryProduct> findByName(String name);

    boolean existsById(Long id);

    void deleteById(Long id);
}