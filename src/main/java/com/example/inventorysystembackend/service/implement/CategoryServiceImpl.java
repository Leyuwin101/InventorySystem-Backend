package com.example.inventorysystembackend.service.implement;

import com.example.inventorysystembackend.dto.request.CategoryRequest;
import com.example.inventorysystembackend.dto.response.CategoryResponse;
import com.example.inventorysystembackend.exception.CategoryNotFoundException;
import com.example.inventorysystembackend.mapper.CategoryMapper;
import com.example.inventorysystembackend.model.entity.Category;
import com.example.inventorysystembackend.repository.CategoryRepository;
import com.example.inventorysystembackend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    /**
     * Create category
     *
     * Process:
     * - Maps the request DTO To category entity
     * - Saves the category to database
     *
     * @param request category registration data
     * @return saved category response
     */
    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "categories", allEntries = true),
            @CacheEvict(cacheNames = "dashboard", allEntries = true),
            @CacheEvict(cacheNames = "reports", allEntries = true)
    })
    public CategoryResponse createCategory(CategoryRequest request) {

        log.info("[CATEGORY][CREATE] Start name={}", request.getName());

        Category category = categoryMapper.toEntity(request);

        Category saved = categoryRepository.save(category);

        log.info("[CATEGORY][CREATE] Success id={}, name={}", saved.getCategoryID(), saved.getName());

        return categoryMapper.toDTO(saved);
    }

    /**
     * Update existing category
     *
     * Process:
     * - Retrieves the category by id
     * - Throws CategoryNotFoundException if the category does not exist
     * - Updates editable category fields
     * - Saved the updated category
     * - Returns the updated category as a response DTO
     *
     * @param categoryId ID of the category to update
     * @param request updated category data
     * @return updated category response
     */

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "categories", allEntries = true),
            @CacheEvict(cacheNames = "products", allEntries = true),
            @CacheEvict(cacheNames = "dashboard", allEntries = true),
            @CacheEvict(cacheNames = "reports", allEntries = true)
    })
    public CategoryResponse updateCategory(Long categoryId, CategoryRequest request) {

        log.info("[CATEGORY][UPDATE] Start id={}", categoryId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("[CATEGORY][UPDATE][NOT_FOUND] id={}", categoryId);
                    return new CategoryNotFoundException("Category not found: " + categoryId);
                });

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updated = categoryRepository.save(category);

        log.info("[CATEGORY][UPDATE] Success id={}", categoryId);

        return categoryMapper.toDTO(updated);
    }

    /**
     * Delete Category
     *
     * Process:
     * - Validates if the category with that id exists
     * - Throw CategoryNotFoundException if the category does not exist
     * - Delete category
     *
     * @param categoryId id of the category to delete
     */
    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "categories", allEntries = true),
            @CacheEvict(cacheNames = "products", allEntries = true),
            @CacheEvict(cacheNames = "dashboard", allEntries = true),
            @CacheEvict(cacheNames = "reports", allEntries = true)
    })
    public void deleteCategory(Long categoryId) {

        log.info("[CATEGORY][DELETE] Start id={}", categoryId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("[CATEGORY][DELETE][NOT_FOUND] id={}", categoryId);
                    return new CategoryNotFoundException("Category not found: " + categoryId);
                });

        categoryRepository.delete(category);

        log.info("[CATEGORY][DELETE] Success id={}", categoryId);

    }

    /**
     * Get category by id
     *
     * Process:
     * - Retrieves the category by id
     * - Throw CategoryNotFoundException if not found
     * - Return category
     *
     * @param categoryId id of the category to get
     * @return category response
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "categories", key = "'detail:' + #categoryId")
    public CategoryResponse getCategoryById(Long categoryId) {

        log.info("[CATEGORY][GET] Start id={}", categoryId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("[CATEGORY][GET][NOT_FOUND] id={}", categoryId);
                    return new CategoryNotFoundException("Category not found: " + categoryId);
                });

        log.info("[CATEGORY][GET] Success id={}", categoryId);

        return categoryMapper.toDTO(category);
    }

    /**
     * Get all categories
     *
     * Process:
     * - Retrieve all categories
     *
     * @return category response
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "categories", key = "'all'")
    public List<CategoryResponse> getAllCategories() {

        log.info("[CATEGORY][GET_ALL] Fetching all categories");

        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(categoryMapper::toDTO)
                .toList();
    }



}
