package com.ship.nazmul.ship.commons.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class FileIO {
    public static File convertToFile(MultipartFile multipartFile) {
        File file = new File(multipartFile.getOriginalFilename());
        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (IOException e) {
        }
        return file;
    }

    public static byte[] scaleImage(byte[] image,String ext, int height) throws IOException {
        // Get a BufferedImage object from a byte array

        InputStream in = new ByteArrayInputStream(image);
        BufferedImage originalImage = ImageIO.read(in);
        BufferedImage resizedImage = resizeImage(originalImage, height);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, ext, baos);
        baos.flush();
        image = baos.toByteArray();
        baos.close();


        return image;
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int height){

        int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

        int IMG_HEIGHT = height;
        int IMG_WIDTH = height * originalImage.getWidth()/originalImage.getHeight();

        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
        g.dispose();
        g.setComposite(AlphaComposite.Src);

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        return resizedImage;
    }
}
