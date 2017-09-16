package ru.igrey.dev.constant;

public enum Language {

    ENGLISH("en", "English"),
    RUSSIAN("ru", "Русский"),
    PERSIAN("per", "فارسی");

    private String id;
    private String title;

    Language(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String title() {
        return this.title;
    }

    public String id() {
        return this.id;
    }

}
