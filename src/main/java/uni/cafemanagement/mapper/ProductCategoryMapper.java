package uni.cafemanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import uni.cafemanagement.dto.ProductCategoryDTO;
import uni.cafemanagement.model.ProductCategory;

@Mapper
public interface ProductCategoryMapper {
    ProductCategoryMapper MAPPER = Mappers.getMapper(ProductCategoryMapper.class);

    @Mapping(source = "name", target = "name")
    ProductCategoryDTO productCategoryToProductCategoryDTO(ProductCategory productCategory);

    @Mapping(source = "name", target = "name")
    ProductCategory productCategoryDTOToProductCategory(ProductCategoryDTO productCategoryDTO);
}