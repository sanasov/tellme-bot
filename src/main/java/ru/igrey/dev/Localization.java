package ru.igrey.dev;

import ru.igrey.dev.constant.Language;

public class Localization {

    private static Language lang;

    public static Language get() {
        return lang;
    }

    public static void set(Language language) {
        lang = language;
    }

}
