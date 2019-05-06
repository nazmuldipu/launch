package com.ship.nazmul.ship.services.impl;

import com.ship.nazmul.ship.commons.PageAttr;
import com.ship.nazmul.ship.commons.utils.DateUtil;
import com.ship.nazmul.ship.commons.utils.ImageValidator;
import com.ship.nazmul.ship.config.security.SecurityConfig;
import com.ship.nazmul.ship.entities.Category;
import com.ship.nazmul.ship.entities.Seat;
import com.ship.nazmul.ship.entities.Ship;
import com.ship.nazmul.ship.entities.User;
import com.ship.nazmul.ship.entities.pojo.UploadProperties;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.invalid.ImageInvalidException;
import com.ship.nazmul.ship.exceptions.invalid.InvalidException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.repositories.CategoryRepository;
import com.ship.nazmul.ship.services.CategoryService;
import com.ship.nazmul.ship.services.FileUploadService;
import com.ship.nazmul.ship.services.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.LimitExceededException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepo;
    private final ShipService shipService;
    private final FileUploadService fileUploadService;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepo, ShipService shipService, FileUploadService fileUploadService) {
        this.categoryRepo = categoryRepo;
        this.shipService = shipService;
        this.fileUploadService = fileUploadService;
    }

    @Override
    public Category save(Long shipId, Category category) throws NotFoundException, ForbiddenException, InvalidException {
        Ship ship = this.shipService.getOne(shipId);
        if (ship == null) throw new NotFoundException("Ship with id : " + ship + " not found");

        category.setShip(ship);
        if(category.getId() != null){
            category.setImagePaths(this.getOne(category.getId()).getImagePaths());
        }
        ship = this.updateStartsFrom(ship,category);

        return this.categoryRepo.save(category);
    }

    private Ship updateStartsFrom(Ship ship, Category category) throws InvalidException, NotFoundException, ForbiddenException {
        if ((ship.getStartsFrom() == 0) || (ship.getStartsFrom() > (category.getFare() - category.getDiscount()))) {
            ship.setStartsFrom(category.getFare() - category.getDiscount());
            ship = this.shipService.save(ship);
        }
        return ship;
    }

    @Override
    public Category getOne(Long id) {
        return this.categoryRepo.findOne(id);
    }

    @Override
    public Category findByName(String name) {
        return this.categoryRepo.findByName(name);
    }

    @Override
    public List<Category> getAll() {
        return this.categoryRepo.findAll();
    }

    @Override
    public Page<Category> getCategoryByShipId(Long id, int page) {
        return this.categoryRepo.findByShipId(id, PageAttr.getPageRequest(page));
    }

    @Override
    public List<Category> getCategoryByShipId(Long id) {
        return this.categoryRepo.findByShipId(id);
    }

    @Override
    public Integer getFare(Long categoryId, Date date) {
        Category category = this.getOne(categoryId);
        Integer discount = category.getDiscountMap().get(date);
        return category.getFare() - discount;
    }

    @Override
    public Integer getDiscount(Long categoryId, Date date) {
        Category category = this.getOne(categoryId);
        return category.getDiscountMap().get(date);
    }

    @Override
    public Map<Date, Integer> getFareMap(Long categoryId, Date startDate, Date endDate) throws NotFoundException {
        Category category = this.getOne(categoryId);
        List<Date> dates = DateUtil.getDatesBetween(startDate, endDate);
        Map<Date, Integer> fareMap = new HashMap<>();
        for (int i = 0; i < dates.size(); i++) {
            Integer discount = category.getDiscountMap().get(dates.get(i));
            if (discount == null) discount = 0;
            fareMap.put(dates.get(i), (category.getFare() - discount));
        }
        return fareMap;
    }

    @Override
    public Map<Date, Integer> getDiscountMap(Long categoryId, Date startDate, Date endDate) throws NotFoundException {
        Category category = this.getOne(categoryId);
        List<Date> dates = DateUtil.getDatesBetween(startDate, endDate);
        Map<Date, Integer> discountMap = new HashMap<>();
        for (int i = 0; i < dates.size(); i++) {
            Integer discount = category.getDiscountMap().get(dates.get(i));
            if (discount == null) discount = 0;
            discountMap.put(dates.get(i),  discount);
        }
        return discountMap;
    }

    @Override
    public Map<String, String> updateCategoryDiscount(Long categoryId, Date startDate, Date endDate, int discountAmount) throws ForbiddenException {
        User user = SecurityConfig.getCurrentUser();
        Category category = this.getOne(categoryId);
        if (user.isOnlyUser() || (!user.isAdmin() && !user.getShips().contains(category.getShip())))
            throw new ForbiddenException("Access denied");

        List<Date> dates = DateUtil.getDatesBetween(startDate, endDate);
        for (int i = 0; i < dates.size(); i++) {
            category.getDiscountMap().put(dates.get(i), discountAmount);
        }
        category = this.categoryRepo.save(category);
        Map<String, String> response = new HashMap<>();
        response.put("response", "success");
        return response;
    }

    @Override
    public boolean exists(Long id) {
        return this.categoryRepo.exists(id);
    }

    @Override
    public boolean delete(Long id) {
        //TODO: Delete properly
//        List<Room> roomList = this.roomService.findByCategoryId(id);
//        if(roomList.size() > 0 )
//            return false;

        this.categoryRepo.delete(id);
        return true;
    }

    @Override
    public void uploadAssets(Long categoryId, MultipartFile multipartFile) throws ImageInvalidException, LimitExceededException, IOException {
        if (!ImageValidator.isImageValid(multipartFile))
            throw new ImageInvalidException("Invalid image!");
        if (multipartFile.getSize() > 1000000)
            throw new LimitExceededException("Image size should be less than 1 mb");
        Category category = this.getOne(categoryId);


        UploadProperties properties = this.fileUploadService.uploadFile(multipartFile, UploadProperties.NameSpaces.CATEGORY.getValue(), String.valueOf(category.getId()), false);
        category.addImagePath(properties.getFilePath());
        this.categoryRepo.save(category);
    }
}
