package com.ship.nazmul.ship.services;

import com.ship.nazmul.ship.entities.Category;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.invalid.ImageInvalidException;
import com.ship.nazmul.ship.exceptions.invalid.InvalidException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.LimitExceededException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CategoryService {
    Category save(Long shipId, Category category) throws NotFoundException, ForbiddenException, InvalidException;

    Category getOne(Long id);

    Category findByName(String name);

    List<Category> getAll();

    Page<Category> getCategoryByShipId(Long id, int page);

    List<Category> getCategoryByShipId(Long id);

    Integer getFare(Long categoryId, Date date);

    Map<Date, Integer> getFareMap(Long categoryId, Date startDate, Date endDate) throws NotFoundException;

    boolean exists(Long id);

    boolean delete(Long id);

    void uploadAssets(Long categoryId, MultipartFile multipartFile) throws ImageInvalidException, LimitExceededException, IOException;
}
