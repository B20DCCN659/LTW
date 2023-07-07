package com.example.btl_web.service.impl;

import com.example.btl_web.service.HashPasswordService;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.ResourceBundle;
public class HashPasswordServiceimpl implements HashPasswordService {
    private ResourceBundle rb = ResourceBundle.getBundle("encrypt");
    @Override
    public String encryptPassword(String passWord) {
        String key = rb.getString("ecrypt_key");
        Mac sha256Hmac = null; // Khởi tạo đối tượng HMAC với thuật toán SHA-256
        try {
            sha256Hmac = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256"); // Khởi tạo khóa bí mật từ chuỗi khóa được cung cấp bởi người dùng
        try {
            sha256Hmac.init(secretKey); // Khởi tạo đối tượng HMAC với khóa bí mật
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        byte[] hashBytes = sha256Hmac.doFinal(passWord.getBytes()); // Mã hoá chuỗi với khóa bí mật bằng thuật toán SHA-256
        return Base64.getEncoder().encodeToString(hashBytes); // Trả về chuỗi đã được mã hoá dưới dạng Base64
    }
    //Thuật toán này không thể giải mã, đáp ứng yêu cầu Admin không thể xem được password của người dùng :)))
}
