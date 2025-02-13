package uni.simulatedpos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.simulatedpos.model.InventoryAlert;

public interface InventoryAlertRepository extends JpaRepository<InventoryAlert, Long> {
}