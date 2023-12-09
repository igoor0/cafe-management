package dev.cafemanagement.server.service;

import dev.cafemanagement.server.model.Order;
import dev.cafemanagement.server.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
    public Order addOrder(Order order) {
        return orderRepository.save(order);
    }
    public Order updateOrder(Long orderId, Order updatedOrder) {
        Order existingOrder = getOrderById(orderId);
        if(existingOrder != null) {
            existingOrder.setOrderId(updatedOrder.getOrderId());
            existingOrder.setOrderDateTime(updatedOrder.getOrderDateTime());
            existingOrder.setCustomer(updatedOrder.getCustomer());
            existingOrder.setOrderItemsList(updatedOrder.getOrderItemsList());
            existingOrder.setTotalAmount(updatedOrder.getTotalAmount());
            return orderRepository.save(existingOrder);
        } else {
            return null;
        }
    }

    public List<Order> getAllOrders() { return orderRepository.findAll();
    }
}
