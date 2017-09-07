package ru.igrey.dev.handler;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import ru.igrey.dev.constant.ButtonCommandName;
import ru.igrey.dev.dao.repository.CategoryRepository;
import ru.igrey.dev.dao.repository.NoteRepository;
import ru.igrey.dev.domain.TelegramUser;
import ru.igrey.dev.service.TelegramUserService;

import static ru.igrey.dev.constant.Delimiter.DELIMITER;

@Slf4j
public class ButtonHandlerFactory {

    TelegramUserService telegramUserService;
    CategoryRepository categoryRepository;
    NoteRepository noteRepository;


    public ButtonHandlerFactory(TelegramUserService telegramUserService, CategoryRepository categoryRepository, NoteRepository noteRepository) {
        this.telegramUserService = telegramUserService;
        this.categoryRepository = categoryRepository;
        this.noteRepository = noteRepository;
    }

    public ButtonHandler create(TelegramUser telegramUser, CallbackQuery query) {
        String buttonCommand = query.getData().split(DELIMITER)[0];
        log.info("Button command: " + buttonCommand);
        switch (buttonCommand) {
            case ButtonCommandName.CREATE_CATEGORY:
                return new CreateCategoryHandler(query, telegramUserService, telegramUser);
            case ButtonCommandName.NOTE_DELETE:
                return new DeleteNoteHandler(query, noteRepository, categoryRepository);
            case ButtonCommandName.PICK_CATEGORY_FOR_ADDED_NOTE:
                return new PickCategoryForNewNoteHandler(query, noteRepository, categoryRepository);
            case ButtonCommandName.BACK_TO_CATEGORY_VIEW:
                return new BackToCategoryViewHandler(query, telegramUser);
            case ButtonCommandName.CATEGORY_DELETE:
                return new DeleteCategoryHandler(query, categoryRepository, telegramUser, telegramUserService);
            case ButtonCommandName.CANCEL:
                return new BackToViewCategoriesHandler(query, telegramUserService);
            case ButtonCommandName.PICK_CATEGORY_TO_VIEW_NOTES:
                return new PickCategoryToViewNotesHandler(query, categoryRepository);
            case ButtonCommandName.REMOVE_MODE:
                return new PickRemoveModeHandler(query, noteRepository, categoryRepository);
        }
        throw new RuntimeException("There is no button handler for command: " + buttonCommand);
    }

}