package ru.igrey.dev.keyboard;

import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.igrey.dev.constant.*;
import ru.igrey.dev.domain.Category;
import ru.igrey.dev.domain.Note;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.igrey.dev.constant.Delimiter.BUTTON_DELIMITER;

/**
 * Created by sanasov on 04.04.2017.
 */
public class ReplyKeyboard {


    public static InlineKeyboardMarkup buttonsForPickingCategoryAfterCreateNote(List<Category> categories, Long noteId) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<Category> categoriesWithoutFiles = categories.stream()
                .filter(category -> !category.getTitle().equals(NamedCategory.VIDEO))
                .filter(category -> !category.getTitle().equals(NamedCategory.PHOTO))
                .filter(category -> !category.getTitle().equals(NamedCategory.VOICE))
                .filter(category -> !category.getTitle().equals(NamedCategory.FILE_DOCUMENT))
                .collect(Collectors.toList());
        for (Category category : categoriesWithoutFiles) {
            List<InlineKeyboardButton> buttonRow = new ArrayList<>();
            buttonRow.add(Button.create(ButtonCommandName.PICK_CATEGORY_FOR_ADDED_NOTE + BUTTON_DELIMITER + category.getId() + BUTTON_DELIMITER + noteId, Emoji.FOLDER + " " + category.getTitle()));
            keyboard.add(buttonRow);
        }
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        buttonRow.add(Button.create(ButtonCommandName.CREATE_CATEGORY, ButtonTitle.CREATE_CATEGORY.text()));
        keyboard.add(buttonRow);
        markup.setKeyboard(keyboard);
        return markup;
    }

    public static InlineKeyboardMarkup buttonsForPickingCategoryForViewNotes(List<Category> categories) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (Category category : categories) {
            List<InlineKeyboardButton> buttonRow = new ArrayList<>();
            if (category.getTitle().equals(NamedCategory.PHOTO)) {
                buttonRow.add(Button.create(ButtonCommandName.PICK_CATEGORY_TO_VIEW_PHOTOS + BUTTON_DELIMITER + category.getId().toString(), Emoji.PICTURE + " " + category.getTitle()));
            } else if (category.getTitle().equals(NamedCategory.VIDEO)) {
                buttonRow.add(Button.create(ButtonCommandName.PICK_CATEGORY_TO_VIEW_VIDEOS + BUTTON_DELIMITER + category.getId().toString(), Emoji.VIDEO + " " + category.getTitle()));
            } else if (category.getTitle().equals(NamedCategory.FILE_DOCUMENT)) {
                buttonRow.add(Button.create(ButtonCommandName.PICK_CATEGORY_TO_VIEW_DOCUMENTS + BUTTON_DELIMITER + category.getId().toString(), Emoji.DOCUMENT + " " + category.getTitle()));
            } else if (category.getTitle().equals(NamedCategory.VOICE)) {
                buttonRow.add(Button.create(ButtonCommandName.PICK_CATEGORY_TO_VIEW_VOICES + BUTTON_DELIMITER + category.getId().toString(), Emoji.MICROPHONE + " " + category.getTitle()));
            } else {
                buttonRow.add(Button.create(ButtonCommandName.PICK_CATEGORY_TO_VIEW_NOTES + BUTTON_DELIMITER + category.getId().toString(), Emoji.FOLDER + " " + category.getTitle()));
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
            if (category.getTitle().equals(NamedCategory.PHOTO)) {
                buttonRow.add(Button.create(ButtonCommandName.PICK_PHOTO + BUTTON_DELIMITER + category.getId().toString() + BUTTON_DELIMITER + note.getId().toString(), note.toFileView()));
            } else if (category.getTitle().equals(NamedCategory.VIDEO)) {
                buttonRow.add(Button.create(ButtonCommandName.PICK_VIDEO + BUTTON_DELIMITER + category.getId().toString() + BUTTON_DELIMITER + note.getId().toString(), note.toFileView()));
            } else if (category.getTitle().equals(NamedCategory.FILE_DOCUMENT)) {
                buttonRow.add(Button.create(ButtonCommandName.PICK_DOCUMENT + BUTTON_DELIMITER + category.getId().toString() + BUTTON_DELIMITER + note.getId().toString(), note.toFileView()));
            } else {
                buttonRow.add(Button.create(ButtonCommandName.PICK_VOICE + BUTTON_DELIMITER + category.getId().toString() + BUTTON_DELIMITER + note.getId().toString(), note.toFileView()));
            }
            keyboard.add(buttonRow);
        }
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        buttonRow.add(Button.create(ButtonCommandName.BACK_TO_CATEGORY_VIEW, ButtonTitle.BACK_TO_CATEGORY_VIEW.text()));
        buttonRow.add(Button.create(ButtonCommandName.REMOVE_MODE + BUTTON_DELIMITER + category.getId(), Emoji.TRASH + " " + ButtonTitle.REMOVE_MODE.text()));
        keyboard.add(buttonRow);
        markup.setKeyboard(keyboard);
        return markup;
    }

    public static InlineKeyboardMarkup buttonsForPickingNotesForDelete(List<Note> notes, Long categoryId, String categoryName) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> firstButtonRow = new ArrayList<>();
        firstButtonRow.add(Button.create(ButtonCommandName.CANCEL, ButtonTitle.BACK_TO_CATEGORY_VIEW.text()));
        List<InlineKeyboardButton> lastButtonRow = new ArrayList<>();
        lastButtonRow.add(Button.create(ButtonCommandName.CATEGORY_DELETE + BUTTON_DELIMITER + categoryId, Emoji.CROSS_MARK.toString(3) + " CATEGORY: \"" + categoryName + "\""));
        keyboard.add(firstButtonRow);
        for (Note note : notes) {
            List<InlineKeyboardButton> buttonRow = new ArrayList<>();
            String removeView = StringUtils.isNotBlank(note.toFileView()) ? note.toFileView() : note.toViewForDelete();
            buttonRow.add(Button.create(ButtonCommandName.NOTE_DELETE + BUTTON_DELIMITER + categoryId + BUTTON_DELIMITER + note.getId().toString(), Emoji.CROSS_MARK + " \"" + removeView + "\""));
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
        buttonRow.add(Button.create(ButtonCommandName.BACK_TO_CATEGORY_VIEW, ButtonTitle.BACK_TO_CATEGORY_VIEW.text()));
        buttonRow.add(Button.create(ButtonCommandName.REMOVE_MODE + BUTTON_DELIMITER + categoryId, Emoji.TRASH + " " + ButtonTitle.REMOVE_MODE.text()));
        keyboard.add(buttonRow);
        markup.setKeyboard(keyboard);
        return markup;
    }

    public static InlineKeyboardMarkup buttonViewFileCategoryDeleteFile(Long categoryId, Long noteId) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        buttonRow.add(Button.create(ButtonCommandName.VIEW_CATEGORY + BUTTON_DELIMITER + categoryId, ButtonTitle.VIEW_CATEGORY.text()));
        buttonRow.add(Button.create(ButtonCommandName.REMOVE_FILE + BUTTON_DELIMITER + noteId, Emoji.TRASH + " " + ButtonTitle.REMOVE_FILE.text()));
        keyboard.add(buttonRow);
        markup.setKeyboard(keyboard);
        return markup;
    }

    public static InlineKeyboardMarkup buttonsRemindAgainIn(Long notificationId) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow = Stream.of(TimeRemindAgain.values())
                .map(buttonTimeRemindAgain -> Button.create(buttonTimeRemindAgain + BUTTON_DELIMITER + notificationId, buttonTimeRemindAgain.text()))
                .collect(Collectors.toList());
        List<InlineKeyboardButton> deleteNotificationButtons = Arrays.asList(
                Button.create(ButtonCommandName.REMOVE_NOTIFICATION + BUTTON_DELIMITER + notificationId + BUTTON_DELIMITER + notificationId, ButtonTitle.REMOVE_NOTIFICATION.text())
        );
        keyboard.add(buttonRow);
        keyboard.add(deleteNotificationButtons);
        markup.setKeyboard(keyboard);
        return markup;
    }

    public static InlineKeyboardMarkup buttonsForPickingLanguage() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> englishButtonRow = new ArrayList<>();
        List<InlineKeyboardButton> russianButtonRow = new ArrayList<>();
        List<InlineKeyboardButton> persButtonRow = new ArrayList<>();
        englishButtonRow.add(Button.create(ButtonCommandName.PICK_LANGUAGE + BUTTON_DELIMITER + Language.ENGLISH.name(), Language.ENGLISH.title()));
        russianButtonRow.add(Button.create(ButtonCommandName.PICK_LANGUAGE + BUTTON_DELIMITER + Language.RUSSIAN.name(), Language.RUSSIAN.title()));
        persButtonRow.add(Button.create(ButtonCommandName.PICK_LANGUAGE + BUTTON_DELIMITER + Language.PERSIAN.name(), Language.PERSIAN.title()));
        keyboard.add(englishButtonRow);
        keyboard.add(russianButtonRow);
        keyboard.add(persButtonRow);
        markup.setKeyboard(keyboard);
        return markup;
    }

    public static InlineKeyboardMarkup menuButtons() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> categories = new ArrayList<>();
        List<InlineKeyboardButton> settings = new ArrayList<>();
        List<InlineKeyboardButton> help = new ArrayList<>();
        List<InlineKeyboardButton> rate = new ArrayList<>();
        List<InlineKeyboardButton> donate = new ArrayList<>();
        categories.add(Button.create(ButtonCommandName.CATEGORIES, ButtonTitle.VIEW_CATEGORIES.text()));
        settings.add(Button.create(ButtonCommandName.SETTINGS, ButtonTitle.SETTINGS.text()));
        help.add(Button.create(ButtonCommandName.HELP, ButtonTitle.HELP.text()));
        rate.add(Button.create(ButtonCommandName.RATE, ButtonTitle.RATE.text()));
        donate.add(Button.create(ButtonCommandName.DONATE, ButtonTitle.DONATE.text()));
        keyboard.add(categories);
        keyboard.add(settings);
        keyboard.add(help);
        keyboard.add(rate);
        keyboard.add(donate);
        markup.setKeyboard(keyboard);
        return markup;
    }

    public static InlineKeyboardMarkup backToMenuButton() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> categories = new ArrayList<>();
        categories.add(Button.create(ButtonCommandName.BACK_TO_MENU, ButtonTitle.BACK_TO_MENU.text()));
        keyboard.add(categories);
        markup.setKeyboard(keyboard);
        return markup;
    }



}
