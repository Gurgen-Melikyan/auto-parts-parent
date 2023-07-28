package com.example.autopartsrest.endpoint;

import com.example.autopartscommon.entity.Category;
import com.example.autopartscommon.entity.Role;
import com.example.autopartsrest.dto.CategoryDto;
import com.example.autopartsrest.dto.CreateCategoryRequestDto;
import com.example.autopartsrest.dto.CreateCategoryResponseDto;
import com.example.autopartsrest.dto.UpdateCategoryDto;
import com.example.autopartsrest.exception.EntityNotFoundException;
import com.example.autopartsrest.exception.UserUnauthorizedException;
import com.example.autopartsrest.mapper.CategoryMapper;
import com.example.autopartsrest.security.CurrentUser;
import com.example.autopartsrest.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("categories")
public class CategoryEndpoint {
    private final CategoryService categoryService;


    @PostMapping("/add")
    public ResponseEntity<CreateCategoryResponseDto> create(@RequestBody CreateCategoryRequestDto requestDto) throws EntityNotFoundException {
        return ResponseEntity.ok(categoryService.createCategory(requestDto));
    }

    @GetMapping
    private ResponseEntity<List<CategoryDto>> getAll(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) throws EntityNotFoundException {
        return ResponseEntity.ok(categoryService.getAllCategories(page, size));

    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> update(@PathVariable("id") int id, @RequestBody UpdateCategoryDto updateCategoryDto,
                                              @AuthenticationPrincipal CurrentUser currentUser) throws EntityNotFoundException, UserUnauthorizedException {
        return ResponseEntity.ok(categoryService.updateCategory(id, updateCategoryDto, currentUser.getUser()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") int id, @AuthenticationPrincipal CurrentUser currentUser) throws EntityNotFoundException, UserUnauthorizedException {
        categoryService.deleteCategoryById(id, currentUser.getUser());
        return ResponseEntity.ok().build();
    }
}



