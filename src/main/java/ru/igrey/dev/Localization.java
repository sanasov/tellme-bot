package ru.igrey.dev;

public class Localization {

    private static String lang;

    public static String get() {
        return lang;
    }

    public static void set(String languageCode) {
        lang = languageCode;
    }

}
