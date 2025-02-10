package uni.cafemanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import uni.cafemanagement.model.InventoryProduct;
import uni.cafemanagement.model.ProductCategory;
import uni.cafemanagement.dto.InventoryProductDTO;
import uni.cafemanagement.dto.ProductCategoryDTO;

@Mapper
public interface InventoryProductMapper {

    InventoryProductMapper MAPPER = Mappers.getMapper(InventoryProductMapper.class);

    @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "price", target = "price"),
            @Mapping(source = "isCountable", target = "isCountable"),
            @Mapping(source = "quantity", target = "quantity"),
            @Mapping(source = "weightInGrams", target = "weightInGrams"),
            @Mapping(source = "category.id", target = "categoryId")
    })
    InventoryProductDTO toInventoryProductDTO(InventoryProduct inventoryProduct);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name")
    })
    ProductCategoryDTO toProductCategoryDTO(ProductCategory productCategory);
}