package ru.igrey.dev.constant;

import ru.igrey.dev.Localization;

import java.time.LocalDateTime;

public enum TimeRemindAgain {

    MINUTES_15("15м", "15m"),
    MINUTES_30("30м", "30m"),
    HOUR("1ч", "1h"),
    HOUR_3("3ч", "3h"),
    DAY("1д", "1d");


    TimeRemindAgain(String ru, String en) {
        this.ru = ru;
        this.en = en;
    }

    String ru;
    String en;

    public String text() {
        if (Localization.get().startsWith("ru")) {
            return ru;
        }
        return en;
    }

    public LocalDateTime notificationDate() {
        switch (this) {
            case MINUTES_15:
                return LocalDateTime.now().plusMinutes(15);
            case MINUTES_30:
                return LocalDateTime.now().plusMinutes(30);
            case HOUR:
                return LocalDateTime.now().plusHours(1);
            case HOUR_3:
                return LocalDateTime.now().plusHours(3);
            case DAY:
                return LocalDateTime.now().plusDays(1);
        }
        throw new RuntimeException("Could not find enum to create notification date: " + this.text());
    }
}
