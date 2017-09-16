package ru.igrey.dev.parse;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ParsedDate {

    String rule;

    public ParsedDate(String rule) {
        this.rule = rule;
    }

    public List<LocalDate> get() {
        LocalDate date = new DateRecognizer(rule).find();
        if (date != null) {
            return Arrays.asList(date);
        }
        if (rule.contains("завтра")
                || rule.contains("tomorrow")
                ) {
            return Arrays.asList(LocalDate.now().plusDays(1));
        }
        if (rule.contains("послезавтра")
                || rule.contains("after tomorrow")
                ) {
            return Arrays.asList(LocalDate.now().plusDays(2));
        }
        if (rule.startsWith("через")
                || rule.startsWith("in")) {

        }
        List<DayOfWeek> daysOfWeek = new ParsedDaysOfWeek(rule).get();
        if (daysOfWeek.size() > 0) {
            return daysOfWeek.stream()
                    .map(dayOfWeek -> getNearestLocalDateByDayOfWeek(dayOfWeek))
                    .collect(Collectors.toList());
        }
        return Arrays.asList(LocalDate.now());
    }

    private LocalDate getNearestLocalDateByDayOfWeek(DayOfWeek dayOfWeek) {
        for (int i = 0; i < 7; i++) {
            if (LocalDate.now().plusDays(i).getDayOfWeek() == dayOfWeek) {
                return LocalDate.now().plusDays(i);
            }
        }
        throw new RuntimeException("Could not get nearest date DayOfWeek:" + dayOfWeek);
    }


}
