package uni.cafemanagement.service;

import org.springframework.stereotype.Service;
import uni.cafemanagement.dto.ProductCategoryDTO;
import uni.cafemanagement.exception.ApiRequestException;
import uni.cafemanagement.mapper.ProductCategoryMapper;
import uni.cafemanagement.model.ProductCategory;
import uni.cafemanagement.repository.ProductCategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    public List<ProductCategoryDTO> getAllCategories() {
        List<ProductCategory> categories = productCategoryRepository.findAll();
        return categories.stream()
                .map(ProductCategoryMapper.MAPPER::productCategoryToProductCategoryDTO)
                .collect(Collectors.toList());
    }

    public ProductCategoryDTO createCategory(ProductCategoryDTO productCategoryDTO) {
        ProductCategory productCategory = ProductCategoryMapper.MAPPER.productCategoryDTOToProductCategory(productCategoryDTO);
        ProductCategory savedCategory = productCategoryRepository.save(productCategory);
        return ProductCategoryMapper.MAPPER.productCategoryToProductCategoryDTO(savedCategory);
    }


    public ProductCategoryDTO updateCategory(Long id, ProductCategoryDTO productCategoryDTO) {
        ProductCategory existingCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Category not found with ID: " + id));
        existingCategory.setName(productCategoryDTO.getName());
        ProductCategory updatedCategory = productCategoryRepository.save(existingCategory);
        return ProductCategoryMapper.MAPPER.productCategoryToProductCategoryDTO(updatedCategory);
    }

    public void deleteCategory(Long id) {
        if (!productCategoryRepository.existsById(id)) {
            throw new ApiRequestException("Category not found with ID: " + id);
        }
        productCategoryRepository.deleteById(id);
    }
}