package uni.simulatedpos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.simulatedpos.model.MenuProductCategory;
import uni.simulatedpos.repository.MenuProductCategoryRepository;

import java.util.List;

@Service
public class MenuProductCategoryService {

    private final MenuProductCategoryRepository menuProductCategoryRepository;

    @Autowired
    public MenuProductCategoryService(MenuProductCategoryRepository menuProductCategoryRepository) {
        this.menuProductCategoryRepository = menuProductCategoryRepository;
    }

    public List<MenuProductCategory> getAllCategories() {
        return menuProductCategoryRepository.findAll();
    }

    public MenuProductCategory getCategoryById(Long id) {
        return menuProductCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));
    }

    public MenuProductCategory createCategory(String menuProductCategory) {
        return menuProductCategoryRepository.save(new MenuProductCategory(menuProductCategory));
    }

    public MenuProductCategory updateCategory(Long id, String updatedCategoryName) {
        MenuProductCategory existingCategory = menuProductCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));

        existingCategory.setName(updatedCategoryName);

        return menuProductCategoryRepository.save(existingCategory);
    }

    public void deleteCategory(Long id) {
        menuProductCategoryRepository.deleteById(id);
    }
}