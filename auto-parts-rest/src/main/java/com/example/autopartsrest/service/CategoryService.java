package com.example.autopartsrest.service;

import com.example.autopartscommon.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> findAllCategories();
    Optional<Category> findByName(String name);
    Category findById(int id);
    Page<Category> findAll(Pageable pageable);
    void addCategory (String name);
    Category save(Category category);
    void deleteById(int id);

    boolean existsById(int id);
}
