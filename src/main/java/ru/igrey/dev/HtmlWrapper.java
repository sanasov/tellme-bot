package ru.igrey.dev;

public class HtmlWrapper {
    public static String toInlineFixedWidthCode(String text) {
        return "<code>" + text + "</code>";
    }

    public static String toBold(String text) {
        return "<b>" + text + "</b>";
    }

    public static String htmlSafe(String text) {
        return text.replace(">", "&gt;").replace("<", "&lt;");
    }
}
