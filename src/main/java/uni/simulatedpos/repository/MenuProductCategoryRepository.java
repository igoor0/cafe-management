package uni.simulatedpos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.simulatedpos.model.MenuProductCategory;

import java.util.Optional;

@Repository
public interface MenuProductCategoryRepository extends JpaRepository<MenuProductCategory, Long> {
    Optional<MenuProductCategory> findByName(String categoryName);
}