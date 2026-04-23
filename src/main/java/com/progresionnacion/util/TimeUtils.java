package com.progresionnacion.util;

public class TimeUtils {

    private TimeUtils() {
    }

    public static double parseToSeconds(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("El tiempo no puede estar vacío");
        }

        String cleanText = text.trim().replace(",", ".");

        if (cleanText.contains(":")) {
            String[] parts = cleanText.split(":");

            if (parts.length == 2) {
                int minutes = Integer.parseInt(parts[0].trim());
                double seconds = Double.parseDouble(parts[1].trim());
                return minutes * 60 + seconds;
            }

            if (parts.length == 3) {
                int hours = Integer.parseInt(parts[0].trim());
                int minutes = Integer.parseInt(parts[1].trim());
                double seconds = Double.parseDouble(parts[2].trim());
                return hours * 3600 + minutes * 60 + seconds;
            }
        }

        return Double.parseDouble(cleanText);
    }

    public static String formatSeconds(double totalSeconds) {
        int hours = (int) (totalSeconds / 3600);
        int minutes = (int) ((totalSeconds % 3600) / 60);
        double seconds = totalSeconds % 60;

        if (hours > 0) {
            return String.format("%02d:%02d:%05.2f", hours, minutes, seconds);
        }

        return String.format("%02d:%05.2f", minutes, seconds);
    }
}
