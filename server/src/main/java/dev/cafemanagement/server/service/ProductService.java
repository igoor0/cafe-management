package dev.cafemanagement.server.service;

import dev.cafemanagement.server.model.Product;
import dev.cafemanagement.server.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    public Product updateProduct(Long productId, Product updatedProduct) {
        Product existingProduct = getProductById(productId);
        if(existingProduct != null) {
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setCategory(updatedProduct.getCategory());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setImageUrl(updatedProduct.getImageUrl());
            existingProduct.setOnSale(updatedProduct.isOnSale());
            existingProduct.setDiscountPercentage(updatedProduct.getDiscountPercentage());
            existingProduct.setQuantityInStock(updatedProduct.getQuantityInStock());
            existingProduct.setId(updatedProduct.getId());
            return productRepository.save(existingProduct);
        } else {
            return null;
        }
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }
}
