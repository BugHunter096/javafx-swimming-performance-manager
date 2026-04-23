package com.progresionnacion.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class PasswordUtils {

    private PasswordUtils() {
    }

    public static String sha256(String text) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(text.getBytes(StandardCharsets.UTF_8));

            StringBuilder builder = new StringBuilder();
            for (byte b : hash) {
                builder.append(String.format("%02x", b));
            }

            return builder.toString();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo generar el hash", e);
        }
    }
}
