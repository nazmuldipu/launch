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
import java.time.LocalDate;
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

    Integer getFare(Long categoryId, LocalDate date);

    Integer getPriceMapFare(Long categoryId, LocalDate date);

    Integer getDiscount(Long categoryId, LocalDate date);

    Map<LocalDate, Integer> getFareMap(Long categoryId, LocalDate startDate, LocalDate endDate) throws NotFoundException;

    Map<LocalDate, Integer> getDiscountMap(Long categoryId, LocalDate startDate, LocalDate endDate) throws NotFoundException;

    Map<LocalDate, Integer> getPriceMap(Long categoryId, LocalDate startDate, LocalDate endDate) throws NotFoundException;

    Map<String, String> updateCategoryDiscount(Long categoryId, LocalDate startDate, LocalDate endDate, int discountAmount) throws ForbiddenException;

    Map<String, String> updateCategoryPrice(Long categoryId, LocalDate startDate, LocalDate endDate, int price) throws ForbiddenException;

    boolean exists(Long id);

    boolean delete(Long id);

    void uploadAssets(Long categoryId, MultipartFile multipartFile) throws ImageInvalidException, LimitExceededException, IOException;
}
