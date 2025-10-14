package com.example.inventory.service;

import java.util.List;

import com.example.inventory.dto.category.CategoryDTO;

public interface CategoryService {

    CategoryDTO create(CategoryDTO dto);

    List<CategoryDTO> getAll();

    CategoryDTO getById(Long id);

    CategoryDTO update(Long id, CategoryDTO dto);

    void delete(Long id);
}
