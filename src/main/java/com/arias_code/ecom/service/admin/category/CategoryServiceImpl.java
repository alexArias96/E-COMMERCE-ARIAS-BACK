package com.arias_code.ecom.service.admin.category;

import com.arias_code.ecom.dto.CategoryDTO;
import com.arias_code.ecom.entity.Category;
import com.arias_code.ecom.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    public Category createCategory(CategoryDTO categoryDTO){
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }
}
