package com.secureshop.backend.category;

import com.secureshop.backend.dto.CategoryRequestDTO;
import com.secureshop.backend.dto.CategoryResponseDTO;
import com.secureshop.backend.dto.PagedResponse;
import com.secureshop.backend.exception.CategoryNotFoundException;
import com.secureshop.backend.mapper.CategoryMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger log =
            LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryServiceImpl(
            CategoryRepository repository,
            CategoryMapper mapper) {

        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public PagedResponse<CategoryResponseDTO> getAllCategories(
            Pageable pageable) {

        log.info(
                "Fetching categories - page: {}, size: {}, sort: {}",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort());

        Page<CategoryResponseDTO> page = repository
                .findAll(pageable)
                .map(mapper::toResponse);

        return PagedResponse.from(page);
    }

    @Override
    public PagedResponse<CategoryResponseDTO> searchCategories(
            String keyword,
            Pageable pageable) {

        log.info("Searching categories with keyword: {}", keyword);

        Page<CategoryResponseDTO> page = repository
                .findByNameContainingIgnoreCase(keyword, pageable)
                .map(mapper::toResponse);

        return PagedResponse.from(page);
    }

    @Override
    public CategoryResponseDTO getCategoryById(Long id) {

        log.info("Fetching category with id {}", id);

        Category category = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Category not found with id {}", id);

                    return new CategoryNotFoundException(id);
                });

        return mapper.toResponse(category);
    }

    @Override
    public CategoryResponseDTO createCategory(
            CategoryRequestDTO request) {

        log.info("Creating category: {}", request.getName());

        Category saved = repository.save(
                mapper.toEntity(request));

        log.info("Category created with id {}", saved.getId());

        return mapper.toResponse(saved);
    }

    @Override
    public CategoryResponseDTO updateCategory(
            Long id,
            CategoryRequestDTO request) {

        log.info("Updating category {}", id);

        Category category = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Category not found {}", id);

                    return new CategoryNotFoundException(id);
                });

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category saved = repository.save(category);

        log.info("Category {} updated successfully", id);

        return mapper.toResponse(saved);
    }

    @Override
    public void deleteCategory(Long id) {

        log.info("Deleting category {}", id);

        Category category = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Category not found {}", id);

                    return new CategoryNotFoundException(id);
                });

        repository.delete(category);

        log.info("Category {} deleted successfully", id);
    }
}