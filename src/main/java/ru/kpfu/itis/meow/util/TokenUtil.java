package ru.kpfu.itis.meow.util;

import ru.kpfu.itis.meow.model.entity.Device;

public class TokenUtil {
    private static final String SEPARATOR = ":";

    public static String createToken(Device device) {
        return "device" + SEPARATOR + device.getId() + SEPARATOR + device.getName();
    }

    public static Long getDeviceIdFromToken(String token) {
        if (!isTokenValid(token)) {
            return -1L;
        }
        return Long.valueOf(token.split(SEPARATOR)[1]);
    }

    public static String getDeviceNameFromToken(String token) {
        if (!isTokenValid(token)) {
            return null;
        }
        return token.split(SEPARATOR)[2];
    }

    private static boolean isTokenValid(String token) {
        return token.split(SEPARATOR).length == 3;
    }
}
