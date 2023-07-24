package com.example.autopartsweb.controller;



import com.example.autopartscommon.entity.Category;
import com.example.autopartsweb.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping()
    public String categoriesPage(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            ModelMap modelMap) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Sort sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);

        Page<Category> result = categoryService.findAll(pageable);
        int totalPages = result.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }
        modelMap.addAttribute("categories", result);
        return "categories";
    }

    @GetMapping("/add")
    public String addCategoryPage() {
        return "addCategory";
    }


    @PostMapping("/add")
    public String addCategory(@RequestParam("name") String name) {
        categoryService.addCategory(name);
        return "redirect:/categories";
    }

    @GetMapping("/remove")
    public String removeCategory(@RequestParam("id") int id) {
        categoryService.deleteById(id);
        return "redirect:/categories";
    }

    @GetMapping("/edit")
    public String editPage(@RequestParam("id") int id, ModelMap modelMap) {
        modelMap.addAttribute("category", categoryService.findById(id));
        return "updateCategory";
    }

    @PostMapping("/edit")
    public String updateCategory(@RequestParam("id") int id, @ModelAttribute Category category) {
        if (category.getName() == null){
            return "updateCategory";
        }
        category.setId(id);
        categoryService.save(category);
        return "redirect:/categories";
    }
}
