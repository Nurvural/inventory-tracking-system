package com.example.inventory.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.inventory.dto.category.CategoryDTO;
import com.example.inventory.entity.Category;
import com.example.inventory.exception.ResourceNotFoundException;
import com.example.inventory.repository.CategoryRepository;
import com.example.inventory.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDTO create(CategoryDTO dto) {
        Category category = Category.builder()
                .name(dto.getCategoryName())
                .deleted(false) // yeni kategori aktif
                .build();
        Category saved = categoryRepository.save(category);
        return new CategoryDTO(saved.getId(), saved.getName());
    }

    @Override
    public List<CategoryDTO> getAll() {
        return categoryRepository.findAllByDeletedFalse().stream()
                .map(c -> new CategoryDTO(c.getId(), c.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getById(Long id) {
        Category category = categoryRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id " + id
                ));
        return new CategoryDTO(category.getId(), category.getName());
    }

    @Override
    public CategoryDTO update(Long id, CategoryDTO dto) {
        Category category = categoryRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id " + id
                ));
        category.setName(dto.getCategoryName());
        Category updated = categoryRepository.save(category);
        return new CategoryDTO(updated.getId(), updated.getName());
    }

    @Override
    public void delete(Long id) {
        Category category = categoryRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id " + id
                ));
        category.setDeleted(true); // soft delete
        categoryRepository.save(category);
    }
}
