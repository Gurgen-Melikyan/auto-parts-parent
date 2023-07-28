package com.example.autopartsrest.service;

import com.example.autopartscommon.entity.Category;
import com.example.autopartscommon.entity.User;
import com.example.autopartsrest.dto.CategoryDto;
import com.example.autopartsrest.dto.CreateCategoryRequestDto;
import com.example.autopartsrest.dto.CreateCategoryResponseDto;
import com.example.autopartsrest.dto.UpdateCategoryDto;
import com.example.autopartsrest.exception.EntityNotFoundException;
import com.example.autopartsrest.exception.UserUnauthorizedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> findAllCategories();

    Optional<Category> findByName(String name);

    Category findById(int id);

    Page<Category> findAll(Pageable pageable);

    void addCategory(String name);

    Category save(Category category);

    void deleteCategoryById(int id, User currentUser) throws UserUnauthorizedException, EntityNotFoundException;

    List<CategoryDto> getAllCategories(int page, int size) throws EntityNotFoundException;

    CreateCategoryResponseDto createCategory(CreateCategoryRequestDto requestDto) throws EntityNotFoundException;

    CategoryDto updateCategory(int id, UpdateCategoryDto updateCategoryDto, User currentUser) throws EntityNotFoundException, UserUnauthorizedException;

    boolean existsById(int id);
}
