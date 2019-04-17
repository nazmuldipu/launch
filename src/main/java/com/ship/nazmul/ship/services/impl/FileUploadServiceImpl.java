package com.ship.nazmul.ship.services.impl;

import com.ship.nazmul.ship.commons.utils.FileIO;
import com.ship.nazmul.ship.commons.utils.ImageUtils;
import com.ship.nazmul.ship.entities.pojo.UploadProperties;
import com.ship.nazmul.ship.services.FileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Value("${files.upload.path}")
    private String rootPath;

    @Override
    public UploadProperties uploadFile(MultipartFile multipartFile, String namespace, String uniqueProperty, boolean scaled) throws IOException {
        byte[] bytes = multipartFile.getBytes();
        if (scaled) {
            String ext = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1); //FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            bytes = FileIO.scaleImage(bytes, ext, 270);
        }

        UploadProperties uploadProperties = new UploadProperties();
        uploadProperties.setNamespace(namespace);
        uploadProperties.setUniqueProperty(uniqueProperty);
        uploadProperties.setRootPath(rootPath);
        File directory = new File(uploadProperties.getDirPath());
        if (!directory.exists()) {
            Files.createDirectories(directory.toPath());
        }
        File imageFile = File.createTempFile("upload_", ".png", directory);
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(imageFile));
        outputStream.write(bytes);
        outputStream.close();

//        if (!UploadProperties.NameSpaces.USERS.getValue().equals(namespace))
//            this.applyWatermark(imageFile);

        uploadProperties.setFileName(imageFile.getName());
        return uploadProperties;
    }

    private void applyWatermark(File imageFile) {
        File output = new File(imageFile.getAbsolutePath());
        String text = "ship.com";
        try {
            ImageUtils.addTextWatermark(text, "png", imageFile, output);
        } catch (IOException e) {
            System.out.println("Could not watermark image: " + e.getMessage());
        }
    }
}