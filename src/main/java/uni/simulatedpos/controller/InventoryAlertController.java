package uni.simulatedpos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uni.simulatedpos.model.InventoryAlert;
import uni.simulatedpos.repository.InventoryAlertRepository;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class InventoryAlertController {

    private final InventoryAlertRepository inventoryAlertRepository;

    @Autowired
    public InventoryAlertController(InventoryAlertRepository inventoryAlertRepository) {
        this.inventoryAlertRepository = inventoryAlertRepository;
    }

    @GetMapping
    public List<InventoryAlert> getAllAlerts() {
        return inventoryAlertRepository.findAll();
    }

}