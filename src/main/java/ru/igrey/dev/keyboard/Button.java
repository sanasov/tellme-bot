package ru.igrey.dev.keyboard;

import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class Button {
    public static InlineKeyboardButton create(String buttonId, String label) {
        InlineKeyboardButton btn = new InlineKeyboardButton();
        btn.setText(label);
        btn.setSwitchInlineQuery("setSwitchInlineQuery");
        btn.setSwitchInlineQueryCurrentChat("setSwitchInlineQueryCurrentChat");
        btn.setCallbackData(buttonId);
        return btn;
    }

    public static InlineKeyboardButton createLink(String buttonId, String label, String url) {
        return create(buttonId, label).setUrl(url);
    }
}
