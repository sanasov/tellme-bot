package ru.igrey.dev.notifyrule.parse;

import java.time.LocalDate;

public class ParsedDate {

    String rule;

    public ParsedDate(String rule) {
        this.rule = rule;
    }

    public LocalDate get() {
        return LocalDate.now();
    }

}
