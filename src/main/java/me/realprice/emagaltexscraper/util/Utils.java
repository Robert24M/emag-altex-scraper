package me.realprice.emagaltexscraper.util;

public class Utils {

    public static String getNormalizeKey(String... data) { // todo: may have problems with args order
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : data) {
            stringBuilder.append(getValueOrDefault(value));
        }
        return stringBuilder.toString().replaceAll("\\s+","").toLowerCase();
    }

    private static String getValueOrDefault(String value) {
        return value == null ? "" : value;
    }
}
