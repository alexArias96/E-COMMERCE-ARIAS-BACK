package com.arias_code.ecom.service.admin.category;

import com.arias_code.ecom.dto.CategoryDTO;
import com.arias_code.ecom.entity.Category;

public interface CategoryService {

    Category createCategory(CategoryDTO categoryDTO);
}
