package com.ship.nazmul.ship.services.impl;

import com.ship.nazmul.ship.commons.PageAttr;
import com.ship.nazmul.ship.commons.utils.ImageValidator;
import com.ship.nazmul.ship.entities.Category;
import com.ship.nazmul.ship.entities.pojo.UploadProperties;
import com.ship.nazmul.ship.exceptions.invalid.ImageInvalidException;
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
import java.util.List;

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
    public Category save(Long shipId, Category category) throws NotFoundException {
        category.setShip(this.shipService.getOne(shipId));
        if(category.getId() != null){
            category.setImagePaths(this.getOne(category.getId()).getImagePaths());
        }
        return this.categoryRepo.save(category);
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
