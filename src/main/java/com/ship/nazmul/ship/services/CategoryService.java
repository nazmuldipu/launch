package com.ship.nazmul.ship.services;

import com.ship.nazmul.ship.entities.Category;
import com.ship.nazmul.ship.exceptions.invalid.ImageInvalidException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.LimitExceededException;
import java.io.IOException;
import java.util.List;

public interface CategoryService {
    Category save(Long shipId, Category category) throws NotFoundException;

    Category getOne(Long id);

    Category findByName(String name);

    List<Category> getAll();

    Page<Category> getCategoryByShipId(Long id, int page);

    List<Category> getCategoryByShipId(Long id);

    boolean exists(Long id);

    boolean delete(Long id);

    void uploadAssets(Long categoryId, MultipartFile multipartFile) throws ImageInvalidException, LimitExceededException, IOException;
}
