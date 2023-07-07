package com.example.btl_web.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Map;

public class FileUtils {
    // Tạo đối tượng Cloudinary
    private static final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                                                "cloud_name", "dna9jawbi",
                                                        "api_key", "418723273453539",
                                                        "api_secret", "saDQGse7P5qcw9_9bA6IFT1MOKE"));

    public static String saveImageToServer(Part filePart, Long blogId)
    {
        String urlImage = null;

        // Lưu ảnh vào Cloudinary
        try
        {
            Map params = ObjectUtils.asMap(
                    "folder", "images/blog/",
                    "public_id", blogId + "_title",
                    "overwrite", true
            );
            byte[] imageBytes = IOUtils.toByteArray(filePart.getInputStream());
            if(imageBytes.length == 0)
                return null;

            cloudinary.uploader().upload(imageBytes, params);
            // Lấy URL của ảnh từ Cloudinary
            String publicId = "images/blog/" + blogId + "_title";
            urlImage = cloudinary.url().generate(publicId);

            System.out.println(urlImage);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return urlImage;
    }

    public static void deleteImage(Long blogId)
    {
        Map params = ObjectUtils.asMap(
            "invalidate", true
        );

        String publicId = "images/blog/" + blogId + "_title";
        try {
            cloudinary.uploader().destroy(publicId, params);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}