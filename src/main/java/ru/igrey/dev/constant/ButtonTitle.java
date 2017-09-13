package ru.igrey.dev.constant;

import ru.igrey.dev.Localization;

public enum ButtonTitle {

    REMOVE_FILE(
            "Удалить",
            "Delete"),

    VIEW_CATEGORY(
            "Просмотр категории",
            "View category"),

    REMOVE_MODE(
            "Процесс удаления",
            "Delete mode"),

    BACK_TO_CATEGORY_VIEW(
            Emoji.LEFT_ARROW + " Назад к категориям",
            Emoji.LEFT_ARROW + " Back to categories"),

    DISABLE_NOTIFICATION(
            Emoji.BELL_CANCEL + " Отключить нотификацию",
            Emoji.BELL_CANCEL + " Disable notification"),
    REMOVE_NOTIFICATION(
            Emoji.TRASH + " Удалить запись",
            Emoji.TRASH + " Delete note"),
    CREATE_CATEGORY(
            "Создать категорию",
            "Create category");

    ButtonTitle(String ru, String en) {
        this.ru = ru;
        this.en = en;
    }

    String ru;
    String en;

    public String text() {
        if (Localization.get().startsWith("ru")) {
            return ru;
        }
        return en;
    }
}
