package ru.igrey.dev.notifyrule.parse;

import java.time.LocalDate;

public class ParsedDate {

    String rule;

    public ParsedDate(String rule) {
        this.rule = rule;
    }

    public LocalDate get() {
        LocalDate date = new DateRecognizer(rule).find();
        if (date != null) {
            return date;
        }
        if (rule.contains("завтра")
                || rule.contains("tomorrow")
                ) {
            return LocalDate.now().plusDays(1);
        }
        return null;
    }

}
