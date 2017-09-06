package ru.igrey.dev;

import org.apache.commons.lang3.StringUtils;

public class Localization {

    private static String lang;

    public static String get() {
        return StringUtils.isNotBlank(lang) ? lang : "en";
    }

    public static void set(String languageCode) {
        lang = languageCode;
    }

}
