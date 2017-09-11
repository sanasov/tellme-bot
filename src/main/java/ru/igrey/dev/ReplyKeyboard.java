package ru.igrey.dev;

import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.igrey.dev.constant.ButtonCommandName;
import ru.igrey.dev.constant.ButtonTitle;
import ru.igrey.dev.constant.Emoji;
import ru.igrey.dev.domain.Category;
import ru.igrey.dev.domain.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.igrey.dev.constant.Delimiter.BUTTON_DELIMITER;

/**
 * Created by sanasov on 04.04.2017.
 */
public class ReplyKeyboard {


    public static InlineKeyboardMarkup buttonsForPickingCategoryAfterCreateNote(List<Category> categories, Long noteId) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<Category> categoriesWithoutFiles = categories.stream()
                .filter(category -> !category.getTitle().equals("Video"))
                .filter(category -> !category.getTitle().equals("Photo"))
                .filter(category -> !category.getTitle().equals("Voice"))
                .filter(category -> !category.getTitle().equals("File document"))
                .collect(Collectors.toList());
        for (Category category : categoriesWithoutFiles) {
            List<InlineKeyboardButton> buttonRow = new ArrayList<>();
            buttonRow.add(createInlineKeyboardButton(ButtonCommandName.PICK_CATEGORY_FOR_ADDED_NOTE + BUTTON_DELIMITER + category.getId() + BUTTON_DELIMITER + noteId, Emoji.FOLDER + " " + category.getTitle()));
            keyboard.add(buttonRow);
        }
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        buttonRow.add(createInlineKeyboardButton(ButtonCommandName.CREATE_CATEGORY, ButtonTitle.CREATE_CATEGORY.text()));
        keyboard.add(buttonRow);
        markup.setKeyboard(keyboard);
        return markup;
    }

    public static InlineKeyboardMarkup buttonsForPickingCategoryForViewNotes(List<Category> categories) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (Category category : categories) {
            List<InlineKeyboardButton> buttonRow = new ArrayList<>();
            if (category.getTitle().equals("Photo")) {
                buttonRow.add(createInlineKeyboardButton(ButtonCommandName.PICK_CATEGORY_TO_VIEW_PHOTOS + BUTTON_DELIMITER + category.getId().toString(), Emoji.PICTURE + " " + category.getTitle()));
            } else if (category.getTitle().equals("Video")) {
                buttonRow.add(createInlineKeyboardButton(ButtonCommandName.PICK_CATEGORY_TO_VIEW_VIDEOS + BUTTON_DELIMITER + category.getId().toString(), Emoji.VIDEO + " " + category.getTitle()));
            } else if (category.getTitle().equals("File document")) {
                buttonRow.add(createInlineKeyboardButton(ButtonCommandName.PICK_CATEGORY_TO_VIEW_DOCUMENTS + BUTTON_DELIMITER + category.getId().toString(), Emoji.DOCUMENT + " " + category.getTitle()));
            } else if (category.getTitle().equals("Voice")) {
                buttonRow.add(createInlineKeyboardButton(ButtonCommandName.PICK_CATEGORY_TO_VIEW_VOICES + BUTTON_DELIMITER + category.getId().toString(), Emoji.MICROPHONE + " " + category.getTitle()));
            } else {
                buttonRow.add(createInlineKeyboardButton(ButtonCommandName.PICK_CATEGORY_TO_VIEW_NOTES + BUTTON_DELIMITER + category.getId().toString(), Emoji.FOLDER + " " + category.getTitle()));
            }
            keyboard.add(buttonRow);
        }
        markup.setKeyboard(keyboard);
        return markup;
    }

    public static InlineKeyboardMarkup buttonsForPickingFileForView(Category category) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (Note note : category.getNotes()) {
            List<InlineKeyboardButton> buttonRow = new ArrayList<>();
            if (category.getTitle().equals("Photo")) {
                buttonRow.add(createInlineKeyboardButton(ButtonCommandName.PICK_PHOTO + BUTTON_DELIMITER + category.getId().toString() + BUTTON_DELIMITER + note.getId().toString(), note.toFileView()));
            } else if (category.getTitle().equals("Video")) {
                buttonRow.add(createInlineKeyboardButton(ButtonCommandName.PICK_VIDEO + BUTTON_DELIMITER + category.getId().toString() + BUTTON_DELIMITER + note.getId().toString(), note.toFileView()));
            } else if (category.getTitle().equals("File document")) {
                buttonRow.add(createInlineKeyboardButton(ButtonCommandName.PICK_DOCUMENT + BUTTON_DELIMITER + category.getId().toString() + BUTTON_DELIMITER + note.getId().toString(), note.toFileView()));
            } else {
                buttonRow.add(createInlineKeyboardButton(ButtonCommandName.PICK_VOICE + BUTTON_DELIMITER + category.getId().toString() + BUTTON_DELIMITER + note.getId().toString(), note.toFileView()));
            }
            keyboard.add(buttonRow);
        }
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        buttonRow.add(createInlineKeyboardButton(ButtonCommandName.BACK_TO_CATEGORY_VIEW, ButtonTitle.BACK_TO_CATEGORY_VIEW.text()));
        buttonRow.add(createInlineKeyboardButton(ButtonCommandName.REMOVE_MODE + BUTTON_DELIMITER + category.getId(), Emoji.TRASH + " " + ButtonTitle.REMOVE_MODE.text()));
        keyboard.add(buttonRow);
        markup.setKeyboard(keyboard);
        return markup;
    }

    public static InlineKeyboardMarkup buttonsForPickingNotesForDelete(List<Note> notes, Long categoryId, String categoryName) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> firstButtonRow = new ArrayList<>();
        firstButtonRow.add(createInlineKeyboardButton(ButtonCommandName.CANCEL, ButtonTitle.BACK_TO_CATEGORY_VIEW.text()));
        List<InlineKeyboardButton> lastButtonRow = new ArrayList<>();
        lastButtonRow.add(createInlineKeyboardButton(ButtonCommandName.CATEGORY_DELETE + BUTTON_DELIMITER + categoryId, Emoji.CROSS_MARK.toString(3) + " CATEGORY: \"" + categoryName + "\""));
        keyboard.add(firstButtonRow);
        for (Note note : notes) {
            List<InlineKeyboardButton> buttonRow = new ArrayList<>();
            String removeView = StringUtils.isNotBlank(note.toFileView()) ? note.toFileView() : note.toView();
            buttonRow.add(createInlineKeyboardButton(ButtonCommandName.NOTE_DELETE + BUTTON_DELIMITER + categoryId + BUTTON_DELIMITER + note.getId().toString(), Emoji.CROSS_MARK + " \"" + removeView + "\""));
            keyboard.add(buttonRow);
        }
        keyboard.add(lastButtonRow);
        markup.setKeyboard(keyboard);
        return markup;
    }

    public static InlineKeyboardMarkup buttonBackToCategoryView(Long categoryId) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        buttonRow.add(createInlineKeyboardButton(ButtonCommandName.BACK_TO_CATEGORY_VIEW, ButtonTitle.BACK_TO_CATEGORY_VIEW.text()));
        buttonRow.add(createInlineKeyboardButton(ButtonCommandName.REMOVE_MODE + BUTTON_DELIMITER + categoryId, Emoji.TRASH + " " + ButtonTitle.REMOVE_MODE.text()));
        keyboard.add(buttonRow);
        markup.setKeyboard(keyboard);
        return markup;
    }

    public static InlineKeyboardMarkup buttonViewFileCategoryDeleteFile(Long categoryId, Long noteId) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        buttonRow.add(createInlineKeyboardButton(ButtonCommandName.VIEW_CATEGORY + BUTTON_DELIMITER + categoryId, ButtonTitle.VIEW_CATEGORY.text()));
        buttonRow.add(createInlineKeyboardButton(ButtonCommandName.REMOVE_FILE + BUTTON_DELIMITER + noteId, Emoji.TRASH + " " + ButtonTitle.REMOVE_FILE.text()));
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
