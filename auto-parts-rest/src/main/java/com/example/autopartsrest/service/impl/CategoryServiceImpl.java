package com.example.autopartsrest.service.impl;


import com.example.autopartscommon.entity.Category;
import com.example.autopartscommon.entity.Role;
import com.example.autopartscommon.entity.User;
import com.example.autopartscommon.repository.CategoryRepository;
import com.example.autopartsrest.dto.CategoryDto;
import com.example.autopartsrest.dto.CreateCategoryRequestDto;
import com.example.autopartsrest.dto.CreateCategoryResponseDto;
import com.example.autopartsrest.dto.UpdateCategoryDto;
import com.example.autopartsrest.exception.EntityNotFoundException;
import com.example.autopartsrest.exception.UserUnauthorizedException;
import com.example.autopartsrest.mapper.CategoryMapper;
import com.example.autopartsrest.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category findById(int id) {
        Optional<Category> byId = categoryRepository.findById(id);
        if (byId.isEmpty()) {
            return null;
        }
        return byId.get();
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public void addCategory(String name) {
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);
    }

    @Override
    public Category save(Category category) {

        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategoryById(int id,User currentUser) throws UserUnauthorizedException, EntityNotFoundException {
        if (currentUser.getRole() != Role.ADMIN) {
            throw new UserUnauthorizedException("You are not ADMIN");
        }
        if (!categoryRepository.existsById(id)){
            throw new EntityNotFoundException("category with "+id+"id not found");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryDto> getAllCategories(int page, int size) throws EntityNotFoundException {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Category> result = categoryRepository.findAll(pageable);

        List<Category> all = result.getContent();
        if (all.size() == 0) {
            throw new EntityNotFoundException("Categoies does not exists");
        }
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : all) {
            categoryDtos.add(categoryMapper.mapToDto(category));
        }
        return categoryDtos;
    }

    @Override
    public CreateCategoryResponseDto createCategory(CreateCategoryRequestDto requestDto) throws EntityNotFoundException {
        Optional<Category> byName = categoryRepository.findByName(requestDto.getName());
        if (!byName.isEmpty()) {
            throw new EntityNotFoundException("CONFLICT");
        }
        Category category = categoryMapper.map(requestDto);
        categoryRepository.save(category);
        return categoryMapper.map(category);
    }

    @Override
    public CategoryDto updateCategory(int id, UpdateCategoryDto updateCategoryDto, User currentUser) throws EntityNotFoundException, UserUnauthorizedException {
        Optional<Category> byId = categoryRepository.findById(id);
        if (byId.isEmpty()) {
            throw new EntityNotFoundException("Category whit " + id + " not found");
        }
        Category categoryFromDb = byId.get();
        if (currentUser.getRole() != Role.ADMIN) {
            throw new UserUnauthorizedException("You are not ADMIN");
        }
        Category category = categoryMapper.mapUpdate(updateCategoryDto);
        if (category.getName() != null) {
            categoryFromDb.setName(category.getName());
        }
        return categoryMapper.mapToDto(categoryRepository.save(categoryFromDb));

    }

    @Override
    public boolean existsById(int id) {
        return categoryRepository.existsById(id);
    }
}
