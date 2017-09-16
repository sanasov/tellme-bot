package ru.igrey.dev.constant;

import ru.igrey.dev.Localization;

public enum ButtonTitle {

    REMOVE_FILE(
            "Удалить",
            "Delete",
            "حذف"),

    VIEW_CATEGORY(
            "Просмотр категории",
            "View category",
            "مشاهده دسته"),

    REMOVE_MODE(
            "Процесс удаления",
            "Delete mode",
            "حالت حذف"),

    BACK_TO_CATEGORY_VIEW(
            Emoji.LEFT_ARROW + " Назад к категориям",
            Emoji.LEFT_ARROW + " Back to categories",
            Emoji.LEFT_ARROW + "بازگشت به دسته ها "),

    DISABLE_NOTIFICATION(
            Emoji.BELL_CANCEL + " Отключить нотификацию",
            Emoji.BELL_CANCEL + " Disable notification",
            Emoji.BELL_CANCEL + "غیر فعال کردن اعلان "),
    REMOVE_NOTIFICATION(
            Emoji.TRASH + " Удалить запись",
            Emoji.TRASH + " Delete note",
            Emoji.TRASH + "حذف یادداشت "
    ),
    CREATE_CATEGORY(
            "Создать категорию",
            "Create category",
            "ایجاد دسته");

    ButtonTitle(String ru, String en, String per) {
        this.ru = ru;
        this.en = en;
        this.per = per;
    }

    String ru;
    String en;
    String per;

    public String text() {
        switch (Localization.get()) {
            case RUSSIAN:
                return ru;
            case ENGLISH:
                return en;
            case PERSIAN:
                return per;
            default:
                return en;
        }
    }
}
