package ru.igrey.dev;

import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.igrey.dev.constant.ButtonCommandName;
import ru.igrey.dev.constant.KeyboardCommand;
import ru.igrey.dev.domain.Category;
import ru.igrey.dev.domain.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanasov on 04.04.2017.
 */
public class ReplyKeyboard {

    public static ReplyKeyboardMarkup getKeyboardOnUserStart() {
        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add(KeyboardCommand.SHOW_NOTES);
        keyboardFirstRow.add(KeyboardCommand.DELETE_NOTE);

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public static InlineKeyboardMarkup buttonsForPickingCategoryAfterCreateNote(List<Category> categories, Long noteId) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (Category category : categories) {
            List<InlineKeyboardButton> buttonRow = new ArrayList<>();
            buttonRow.add(createInlineKeyboardButton(ButtonCommandName.PICK_CATEGORY_FOR_ADDED_NOTE + "#" + category.getId() + "#" + noteId, category.getTitle()));
            keyboard.add(buttonRow);
        }
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        buttonRow.add(createInlineKeyboardButton(ButtonCommandName.CREATE_CATEGORY, KeyboardCommand.CREATE_CATEGORY));
        keyboard.add(buttonRow);
        markup.setKeyboard(keyboard);
        return markup;
    }

    public static InlineKeyboardMarkup buttonsForPickingCategoryForViewNotes(List<Category> categories) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (Category category : categories) {
            List<InlineKeyboardButton> buttonRow = new ArrayList<>();
            buttonRow.add(createInlineKeyboardButton(ButtonCommandName.PICK_CATEGORY_FOR_VIEW_NOTES + "#" + category.getId().toString(), category.getTitle()));
            keyboard.add(buttonRow);
        }
        markup.setKeyboard(keyboard);
        return markup;
    }

    public static InlineKeyboardMarkup buttonsForPickingCategoryForDeleteNotes(List<Category> categories) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> firstButtonRow = new ArrayList<>();
        firstButtonRow.add(createInlineKeyboardButton(ButtonCommandName.CANCEL, "Завершить удаление"));
        keyboard.add(firstButtonRow);
        for (Category category : categories) {
            List<InlineKeyboardButton> buttonRow = new ArrayList<>();
            buttonRow.add(createInlineKeyboardButton(ButtonCommandName.CATEGORY_DELETE_NOTES + "#" + category.getId().toString(), category.getTitle()));
            keyboard.add(buttonRow);
        }
        markup.setKeyboard(keyboard);
        return markup;
    }


    public static InlineKeyboardMarkup buttonsForPickingNotesForDelete(List<Note> notes, Long categoryId, String categoryName) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> firstButtonRow = new ArrayList<>();
        firstButtonRow.add(createInlineKeyboardButton(ButtonCommandName.CANCEL, "Завершить удаление"));
        List<InlineKeyboardButton> secondButtonRow = new ArrayList<>();
        secondButtonRow.add(createInlineKeyboardButton(ButtonCommandName.CATEGORY_DELETE + "#" + categoryId, "DELETE CATEGORY: " + categoryName));
        keyboard.add(firstButtonRow);
        keyboard.add(secondButtonRow);
        for (Note note : notes) {
            List<InlineKeyboardButton> buttonRow = new ArrayList<>();
            buttonRow.add(createInlineKeyboardButton(ButtonCommandName.NOTE_DELETE + "#" + categoryId + "#" + note.getId().toString(), "DELETE: " + note.getText()));
            keyboard.add(buttonRow);
        }
        markup.setKeyboard(keyboard);
        return markup;
    }


    public static InlineKeyboardMarkup buttonBackToCategoryView() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        buttonRow.add(createInlineKeyboardButton(ButtonCommandName.BACK_TO_CATEGORY_VIEW, "Назад к категориям"));
        keyboard.add(buttonRow);
        markup.setKeyboard(keyboard);
        return markup;
    }


    private static InlineKeyboardButton createInlineKeyboardButton(String buttonId, String label) {
        InlineKeyboardButton btn = new InlineKeyboardButton();
        btn.setText(label);
        btn.setSwitchInlineQuery("setSwitchInlineQuery");
        btn.setSwitchInlineQueryCurrentChat("setSwitchInlineQueryCurrentChat");
        btn.setCallbackData(buttonId);
        return btn;
    }

}
