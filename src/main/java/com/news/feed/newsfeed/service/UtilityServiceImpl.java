package com.news.feed.newsfeed.service;

import org.springframework.stereotype.Component;

@Component(value = "utilityService")
public class UtilityServiceImpl implements UtilityService {
    @Override
    public String generateHash(String plainText) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(plainText.replaceAll("\\s","").getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
