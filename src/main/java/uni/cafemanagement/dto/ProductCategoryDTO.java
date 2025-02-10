package uni.cafemanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductCategoryDTO {
    private Long id;
    private String name;
    private List<Long> productIds;
}