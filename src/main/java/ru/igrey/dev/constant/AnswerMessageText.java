package ru.igrey.dev.constant;

import ru.igrey.dev.Localization;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public enum AnswerMessageText {

    ADD_NOTE_AND_PICK_CATEGORY(
            "Отправьте мне заметку и выберите для нее категорию.\n<strong>Используйте команду:</strong> /menu\n",
            "Send me a note and select a category for it.\n<strong>Use command:</strong> /menu\n",
            "یک یادداشت به من بدهید و بخشی را برای آن انتخاب کنید" + "\n/menu <strong>:استفاده از دستور</strong>\n"
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
            "زبان را انتخاب کنید"),

    LANGUAGE(
            "Язык: " + Language.RUSSIAN.title(),
            "Language: " + Language.ENGLISH.title(),
            Language.PERSIAN.title() + "زبان: "),


    LOCAL_TIME(
            "Локальное время: ",
            "Local time: ",
            "زمان محلی: "
    ),


    SET_TIMEZONE(
            "Установите часовой пояс " + Emoji.EARTH_GLOBE_EUROPE_AFRICA + "\n" + "Для этого отправьте мне текущую дату и время" + "\n" + "Формат: <strong>dd.MM HH:mm</strong>" + "\nПример: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM HH:mm")),
            "Set timezone " + Emoji.EARTH_GLOBE_EUROPE_AFRICA + "\n" + "Send me your current time" + "\n" + "Format: <strong>dd.MM HH:mm</strong>\nExample: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM HH:mm")),
            "منطقه زمانی را تنظیم کنید" + "\n" + "زمان فعلی من را به من بدهید" + "\n" + "فرمت: <strong>dd.MM HH:mm</strong>" + "\n" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM HH:mm")) + "فرمت صحیح:"),

    NOTIFICATION_IS_POSTPONED(
            "Напоминание отложено",
            "Reminder postponed",
            "یادآوری تاخیر"),

    WRONG_FORMAT(
            "Неверынй формат даты и времени. Правильный формат: <strong>dd.MM HH:mm</strong> \n" + "Пример: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM HH:mm")),
            "Invalid time format. Correct format: <strong>HH:MM</strong> \n Example:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM HH:mm")),
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM HH:mm")) + "فرمت زمان نامعتبر\nفرمت صحیح: "),
    MENU(
            "Меню",
            "Menu",
            "فهرست"
    ),
    NOTIFICATION_INSTRUCTION(
            Emoji.BELL + " Вы можете указать в заметке дату и время уведомления.\nИспользуйте разделитель /\nПример: Забронировать столик / 23:30 28.02.2018",
            Emoji.BELL + " You can specify date and time of notification in your note.\nUse the separator /\nExample: Book a table / 23:30 02.28.2018",
            Emoji.BELL + "تاریخ و زمان یادداشت خود را برای اطلاع دادن مشخص کنید\nاستفاده از جدا کننده / \nمثال: Book a table / 23:30 02.28.2018"),

    FORMAT(
            "<strong>Формат для нотификации:</strong>\n/ 12.08 18:45\n/ 12.08.2022 15:00\n/ завтра утром\n/ во вторник 20:00 \n/ понедельник среда пятница в обед\n и многое другое...",
            "<strong>Format for notification:</strong>\n/ 12.08 18:45\n/ 12.08.2022 15:00\n/ tomorrow morning\n/ on Tuesday 20:00 \n/ Monday Wednesday Friday at lunch\n and others...",
            "<strong>:فرمت برای اطلاع رسانی</strong>\n/ 12.08 18:45\n/ 12.08.2022 15:00\n/ tomorrow morning\n/ on Tuesday 20:00 \n/ Monday Wednesday Friday at lunch\n...و دیگران"
    ),

    //    COMMON_INSTRUCTION(
//            "COMMON_INSTRUCTION",
//            "COMMON_INSTRUCTION",
//            "COMMON_INSTRUCTION"
//    ),
    DONATE(
            "Поддержите меня небольшим взносом для оплаты серверов.",
            "Donate some money, so i can pay for servers. Thank you.",
            "اهدای مقداری پول برای پرداخت سرور"
    ),

    TIMEZONE_IS_SET(
            Emoji.WHITE_HEAVY_CHECK_MARK + " Часовой пояс установлен",
            Emoji.WHITE_HEAVY_CHECK_MARK + " Time zone is set",
            " منطقه زمانی تنظیم شده است" + Emoji.WHITE_HEAVY_CHECK_MARK
    );


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
