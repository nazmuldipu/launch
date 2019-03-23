package com.ship.nazmul.ship.services;

import com.ship.nazmul.ship.entities.pojo.UploadProperties;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {
    UploadProperties uploadFile(MultipartFile file, String namespace, String uniqueProperty, boolean scaled) throws IOException;
}
