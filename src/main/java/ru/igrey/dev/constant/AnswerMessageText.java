package ru.igrey.dev.constant;

import ru.igrey.dev.Localization;

public enum AnswerMessageText {

    ADD_NOTE_AND_PICK_CATEGORY(
            "Отправьте мне заметку и выберите для нее категорию",
            "Send me a note and select a category for it",
            "یک یادداشت به من بدهید و بخشی را برای آن انتخاب کنید"
    ),

    CATEGORY_IS_ADDED(
            "Категория добавлена",
            "Category added",
            "دسته اضافه شده است"),
    ADD_CATEGORY(
            "Отправьте мне название категории",
            "Send me a category name",
            "یک نام دسته را به من بفرست"
    ),
    NO_CATEGORIES_NO_NOTES(
            "Нет категорий. Нет записей. \n Отправьте мне запись и выберите для нее категорию " + Emoji.SMILING_FACE_WITH_SMILING_EYES,
            "No categories. No records. Send me your note and pick category for it" + Emoji.SMILING_FACE_WITH_SMILING_EYES,
            "خالی"
    ),
    PICK_CATEGORY_TO_VIEW_NOTES(
            "Выберите категорию для просмотра записей",
            "Select a category to view the notes",
            "یک دسته را انتخاب کنید تا یادداشت ها را مشاهده کنید"
    ),
    PICK_CATEGORY_FOR_YOUR_NOTE(
            "Выберите или создайте категорию для вашей заметки",
            "Pick or create category for your note",
            "دسته بندی را برای یادداشت خود انتخاب کنید یا ایجاد کنید"),
    PICK_NOTES_FOR_DELETE(
            "Выберите записи для удаления",
            "Select notes to delete",
            "یادداشت ها را برای حذف انتخاب کنید"
    ),
    EMPTY(
            "Пусто. Отправьте мне заметку " + Emoji.NOTE_WITH_PEN.toString(),
            "Empty. Send me a note " + Emoji.NOTE_WITH_PEN.toString(),
            " خالی. یک یادداشتی به من بدهید" + Emoji.NOTE_WITH_PEN.toString()),
    NOTE_IS_DELETED(
            "Запись удалена",
            "Note is deleted",
            "توجه حذف شده است"
    ),
    FILE_IS_DELETED(
            "Файл удален",
            "File deleted",
            "پرونده حذف شده است"),
    CATEGORY_IS_DELETED(
            "Категория удалена",
            "Category deleted",
            "رده حذف شده است"
    ),
    NOTE_IS_ADDED_IN_CATEGORY(
            "Запись добавлена в категорию",
            "Note added to category",
            "توجه به رده اضافه شد"),
    BACK_TO_CATEGORY_VIEW(
            "Переход к просмотру записей",
            "Go to the notes view",
            "حالت نمایش"),
    CATEGORY_HAS_BEEN_DELETED(
            "Категория была удалена",
            "Category has been deleted",
            "رده حذف شده است"),
    RATE(
            "Буду счастлив, если вы поставите мне " + Emoji.STAR.toString(5) + "️ тут https://telegram.me/storebot?start=mindkeeperbot \n" +
                    Emoji.SMILING_FACE_WITH_SMILING_EYES.toString() + "Большое спасибо!",
            "I would be very grateful if you could give me " + Emoji.STAR.toString(5) + "️ here https://telegram.me/storebot?start=mindkeeperbot \n" +
                    Emoji.SMILING_FACE_WITH_SMILING_EYES.toString() + "Thank you!",
            "من می توانم بسیار سپاسگزار باشم اگر بتوانید https://telegram.me/storebot?start=mindkeeperbot" + Emoji.STAR.toString(5) + "را اینجا بگذارید " + "\n"
                    + Emoji.SMILING_FACE_WITH_SMILING_EYES.toString() + "متشکرم "),
    NOTIFY_AGAIN_IN(
            "Напомнить снова через:",
            "Remind again in:",
            "دوباره یادآوری کنید:"),
    NOTIFY_AGAIN(
            "Напомнить снова:",
            "Remind again:",
            "دوباره یادآوری کنید:"),

    PICK_LANGUAGE(
            "Выберите язык:",
            "Сhoose language:",
            "زبان خود را انتخاب کنید"),

    SET_TIMEZONE(
            "Установите часовой пояс " + Emoji.EARTH_GLOBE_EUROPE_AFRICA + "\n" + "Для этого отправьте мне текущее время" + "\n" + "Формат: <strong>HH:MM</strong>",
            "Set timezone " + Emoji.EARTH_GLOBE_EUROPE_AFRICA + "\n" + "Send me your current time" + "\n" + "Format: <strong>HH:MM</strong>",
            "منطقه زمانی را تنظیم کنید" + "\n" + "زمان فعلی من را به من بدهید" + "\n" + "فرمت: <strong>HH:MM</strong>"),

    NOTIFICATION_IS_POSTPONED(
            "Напоминание отложено",
            "Reminder postponed",
            "یادآوری تاخیر"),

    WRONG_FORMAT(
            "Неверынй формат времени. Правильный формат: <strong>HH:MM</strong> \n" + "Пример: 18:32",
            "Invalid time format. Correct format: <strong>HH:MM</strong> \n Example: 18:32",
            "فرمت زمان نامعتبر\nفرمت صحیح:<strong>HH:MM</strong>");


    AnswerMessageText(String ru, String eng, String per) {
        this.ru = ru;
        this.en = eng;
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
