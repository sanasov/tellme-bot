package ru.igrey.dev.constant;

import ru.igrey.dev.Localization;

public enum AnswerMessageText {

    ADD_NOTE_AND_PICK_CATEGORY(
            "Отправьте мне заметку и выберите для нее категорию",
            "Send me a note and select a category for it"),

    CATEGORY_IS_ADDED(
            "Категория добавлена",
            "Category added"),
    ADD_CATEGORY(
            "Отправьте мне название категории",
            "Send me a category name"),
    NO_CATEGORIES_NO_NOTES(
            "Нет категорий. Нет записей. \n Отправьте мне запись и выберите для нее категорию " + Emoji.SMILING_FACE_WITH_SMILING_EYES,
            "No categories. No records. Send me your note and pick category for it" + Emoji.SMILING_FACE_WITH_SMILING_EYES),
    IN_WHICH_CATEGORY(
            "Выберите категорию для просмотра записей",
            "Select a category to view the notes"),
    PICK_CATEGORY_FOR_YOUR_NOTE(
            "Выберите или создайте категорию для вашей заметки",
            "Pick or create category for your note"),
    PICK_NOTES_FOR_DELETE(
            "Выберите записи для удаления",
            "Select a category for your note"),
    EMPTY(
            "Нет записей. Добавьте " + Emoji.SMILING_FACE_WITH_SMILING_EYES.toString(),
            "No records. Add" + Emoji.SMILING_FACE_WITH_SMILING_EYES.toString()),
    NOTE_IS_DELETED(
            "Запись удалена",
            "Note deleted"),
    FILE_IS_DELETED(
            "Файл удален",
            "File deleted"),
    CATEGORY_IS_DELETED(
            "Категория удалена",
            "Category deleted"),
    NOTE_IS_ADDED_IN_CATEGORY(
            "Запись добавлена в категорию",
            "Note added to category"),
    BACK_TO_CATEGORY_VIEW(
            "Переход к просмотру записей",
            "Go to the notes view"),
    CATEGORY_HAS_BEEN_DELETED(
            "Категория была удалена",
            "Category has been deleted"),
    RATE(
            "Буду счастлив, если вы поставите мне " + Emoji.STAR.toString(5) + "️ тут https://telegram.me/storebot?start=mindkeeperbot \n" +
                    Emoji.SMILING_FACE_WITH_SMILING_EYES.toString() + "Большое спасибо!",
            "I would be very grateful if you could give me " + Emoji.STAR.toString(5) + "️ here https://telegram.me/storebot?start=mindkeeperbot \n" +
                    Emoji.SMILING_FACE_WITH_SMILING_EYES.toString() + "Thank you!"),
    NOTIFY_AGAIN_IN(
            "Напомнить снова через:",
            "Remind again in:"),
    NOTIFY_AGAIN(
            "Напомнить снова:",
            "Remind again:"),
    NOTIFICATION_IS_POSTPONED(
            "Напоминание отложено",
            "Reminder postponed");


    AnswerMessageText(String ru, String eng) {
        this.ru = ru;
        this.en = eng;
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
