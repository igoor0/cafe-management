package uni.simulatedpos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.simulatedpos.model.MenuProduct;

@Repository
public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
}