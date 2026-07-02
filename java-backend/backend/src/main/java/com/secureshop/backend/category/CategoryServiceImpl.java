package com.secureshop.backend.category;

import com.secureshop.backend.exception.CategoryNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryServiceImpl(CategoryRepository repository,
                               CategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDTO getCategoryById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO request) {
        Category category = mapper.toEntity(request);
        Category saved = repository.save(category);
        return mapper.toResponse(saved);
    }

    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO request) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updated = repository.save(category);
        return mapper.toResponse(updated);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        repository.delete(category);
    }
}
