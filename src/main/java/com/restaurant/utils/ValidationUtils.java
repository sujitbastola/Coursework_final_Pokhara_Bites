package com.restaurant.utils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public final class ValidationUtils {
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE = Pattern.compile("^[0-9+()\\-\\s]{7,20}$");

    private ValidationUtils() {}

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static String clean(String value) {
        return value == null ? "" : value.trim();
    }

    public static boolean isValidEmail(String email) {
        return !isBlank(email) && EMAIL.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        return !isBlank(phone) && PHONE.matcher(phone.trim()).matches();
    }

    public static int parsePositiveInt(String value, int fallback) {
        try {
            int parsed = Integer.parseInt(value);
            return parsed > 0 ? parsed : fallback;
        } catch (Exception e) {
            return fallback;
        }
    }

    public static BigDecimal parseMoney(String value) {
        try {
            BigDecimal money = new BigDecimal(value);
            return money.compareTo(BigDecimal.ZERO) > 0 ? money : null;
        } catch (Exception e) {
            return null;
        }
    }
}
