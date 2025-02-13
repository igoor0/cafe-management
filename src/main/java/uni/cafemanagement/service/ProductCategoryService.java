package uni.cafemanagement.service;

import org.springframework.stereotype.Service;
import uni.cafemanagement.exception.ApiRequestException;
import uni.cafemanagement.model.ProductCategory;
import uni.cafemanagement.repository.ProductCategoryRepository;

import java.util.List;

@Service
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    public List<ProductCategory> getAllCategories() {
        return productCategoryRepository.findAll();
    }

    public ProductCategory createCategory(String productCategoryName) {
        return productCategoryRepository.save(new ProductCategory(productCategoryName));
    }

    public ProductCategory updateCategory(Long id, String productCategory) {
        ProductCategory existingCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Category not found with ID: " + id));

        existingCategory.setName(productCategory);

        return productCategoryRepository.save(existingCategory);
    }

    public void deleteCategory(Long id) {
        if (!productCategoryRepository.existsById(id)) {
            throw new ApiRequestException("Category not found with ID: " + id);
        }
        productCategoryRepository.deleteById(id);
    }
}