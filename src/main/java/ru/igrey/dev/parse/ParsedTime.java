package ru.igrey.dev.parse;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ParsedTime {
    private final static LocalTime defaultMorningTime = LocalTime.of(9, 0);
    private final static LocalTime defaultAfternoonTime = LocalTime.of(14, 0);
    private final static LocalTime defaultEveningTime = LocalTime.of(19, 0);
    private final static LocalTime defaultNightTime = LocalTime.of(23, 55);
    String rule;

    public ParsedTime(String rule) {
        this.rule = rule;
    }

    public LocalTime get() {
        String time = new Time24HourRecognizer().find(rule);
        if (time == null) {
            return getDefaultTimeByDayPart();
        }
        return LocalTime.parse(time, DateTimeFormatter.ofPattern("H:mm"));
    }

    private LocalTime getDefaultTimeByDayPart() {
        if (rule.toLowerCase().contains("утр")
                || rule.toLowerCase().contains("завтрак")
                || rule.toLowerCase().contains("morning")
                ) {
            return defaultMorningTime;
        }
        if (rule.toLowerCase().contains("днем")
                || rule.toLowerCase().contains("днём")
                || rule.toLowerCase().contains("день")
                || rule.toLowerCase().contains("обед")
                || rule.toLowerCase().contains("day")
                || rule.toLowerCase().contains("afternoon")
                ) {
            return defaultAfternoonTime;
        }
        if (rule.toLowerCase().contains("вечер")
                || rule.toLowerCase().contains("ужин")
                || rule.toLowerCase().contains("dinner")
                || rule.toLowerCase().contains("evening")) {
            return defaultEveningTime;
        }
        if (rule.toLowerCase().contains("ночь")
                || rule.toLowerCase().contains("сном")
                || rule.toLowerCase().contains("сон")
                || rule.toLowerCase().contains("sleep")
                || rule.toLowerCase().contains("night")

                ) {
            return defaultNightTime;
        }
        return defaultMorningTime;
    }

}
