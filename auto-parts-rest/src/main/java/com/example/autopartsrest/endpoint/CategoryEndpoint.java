package com.example.autopartsrest.endpoint;

import com.example.autopartscommon.entity.Category;
import com.example.autopartscommon.entity.Role;
import com.example.autopartsrest.dto.CategoryDto;
import com.example.autopartsrest.dto.CreateCategoryRequestDto;
import com.example.autopartsrest.dto.CreateCategoryResponseDto;
import com.example.autopartsrest.dto.UpdateCategoryDto;
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
    private final CategoryMapper categoryMapper;

    @PostMapping("/add")
    public ResponseEntity<CreateCategoryResponseDto> create(@RequestBody CreateCategoryRequestDto requestDto) {
        Optional<Category> byName = categoryService.findByName(requestDto.getName());
        if (byName.isEmpty()) {
            Category category = categoryMapper.map(requestDto);
            categoryService.save(category);
            return ResponseEntity.ok(categoryMapper.map(category));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();

    }

    @GetMapping
    private ResponseEntity<List<CategoryDto>> getAll(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Category> result = categoryService.findAll(pageable);

        List<Category> all = result.getContent();
        if (all.size() == 0) {
            return ResponseEntity.notFound().build();
        }
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : all) {
            categoryDtos.add(categoryMapper.mapToDto(category));
        }
        return ResponseEntity.ok(categoryDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> update(@PathVariable("id") int id, @RequestBody UpdateCategoryDto updateCategoryDto,
                                              @AuthenticationPrincipal CurrentUser currentUser) {
        Category categoryFromDb = categoryService.findById(id);
        if (categoryFromDb == null) {
            return ResponseEntity.notFound().build();
        }
        if (currentUser.getUser().getRole() != Role.ADMIN) {
            return ResponseEntity.notFound().build();
        }
        Category category = categoryMapper.mapUpdate(updateCategoryDto);
        if (category.getName() != null) {
            categoryFromDb.setName(category.getName());
        }
        return ResponseEntity.ok(categoryMapper.mapToDto(categoryService.save(categoryFromDb)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") int id) {
        if (categoryService.existsById(id)) {
            categoryService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}



