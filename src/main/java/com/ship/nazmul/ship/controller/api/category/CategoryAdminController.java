package com.ship.nazmul.ship.controller.api.category;

import com.ship.nazmul.ship.entities.Category;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.invalid.ImageInvalidException;
import com.ship.nazmul.ship.exceptions.invalid.InvalidException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.LimitExceededException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/category")
public class CategoryAdminController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryAdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("{shipId}")
    private ResponseEntity<Page<Category>> getAllCategory(@PathVariable("shipId") Long shipId,
                                                          @RequestParam(value = "page", defaultValue = "0") Integer page) {
        return ResponseEntity.ok(this.categoryService.getCategoryByShipId(shipId, page));
    }

    @GetMapping("/list/{shipId}")
    private ResponseEntity<List<Category>> getAllCategoryList(@PathVariable("shipId") Long shipId) {
        return ResponseEntity.ok(this.categoryService.getCategoryByShipId(shipId));
    }

    @GetMapping("/id/{categoryId}")
    private ResponseEntity<Category> getCategory(@PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.ok(this.categoryService.getOne(categoryId));
    }

    @PostMapping("/{shipId}")
    private ResponseEntity<Category> saveCategory(@PathVariable("shipId") Long shipId, @RequestBody Category category) throws NotFoundException, ForbiddenException, InvalidException {
        return ResponseEntity.ok(this.categoryService.save(shipId, category));
    }

    @PutMapping("/{shipId}/category/{categoryId}")
    private ResponseEntity<Category> updateCategory(@PathVariable("shipId") Long shipId, @PathVariable("categoryId") Long categoryId, @RequestBody Category category) throws NotFoundException, ForbiddenException, InvalidException {
        category.setId(categoryId);
        return ResponseEntity.ok(this.categoryService.save(shipId, category));
    }

    @PutMapping("/{categoryId}/images/upload")
    private ResponseEntity<?> uploadImage(@PathVariable("categoryId") Long categoryId,
                                          @RequestParam("image") MultipartFile multipartFile) throws LimitExceededException, ImageInvalidException, IOException {
        this.categoryService.uploadAssets(categoryId, multipartFile);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    private ResponseEntity deleteCategory(@PathVariable("id")Long id) {
        Map<String, String> response = new HashMap<>();
        boolean res = this.categoryService.delete(id);
        if(res)
            response.put("response", "success");
        else
            response.put("response", "failed");
        return ResponseEntity.ok(response);
    }
}
