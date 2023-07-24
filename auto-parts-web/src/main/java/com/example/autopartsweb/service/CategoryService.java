package com.example.autopartsweb.service;


import com.example.autopartscommon.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    List<Category> findAllCategories();
    Category findById(int id);
    Page<Category> findAll(Pageable pageable);
    void addCategory (String name);
    Category save(Category category);
    void deleteById(int id);
}
