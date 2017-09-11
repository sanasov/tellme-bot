package ru.igrey.dev.notifyrule.parse;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
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
        List<DayOfWeek> daysOfWeek = retrieveDaysOfWeek();
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

    private List<DayOfWeek> retrieveDaysOfWeek() {
        List<DayOfWeek> daysOfWeek = new ArrayList<>();
        if (onMonday()) {
            daysOfWeek.add(DayOfWeek.MONDAY);
        }
        if (onTuesday()) {
            daysOfWeek.add(DayOfWeek.TUESDAY);
        }
        if (onWednesday()) {
            daysOfWeek.add(DayOfWeek.WEDNESDAY);
        }
        if (onThursday()) {
            daysOfWeek.add(DayOfWeek.THURSDAY);
        }
        if (inFriday()) {
            daysOfWeek.add(DayOfWeek.FRIDAY);
        }
        if (onSaturday()) {
            daysOfWeek.add(DayOfWeek.SATURDAY);
        }
        if (onSunday()) {
            daysOfWeek.add(DayOfWeek.SUNDAY);
        }
        return daysOfWeek;
    }

    private Boolean onMonday() {
        if (rule.contains("понедельник")
                || rule.contains("пн.")
                || rule.contains("monday")
                || rule.contains("mon.")
                ) {
            return true;
        }
        return false;
    }

    private Boolean onTuesday() {
        if (rule.contains("вторник")
                || rule.contains("вт.")
                || rule.contains("tuesday")
                || rule.contains("tues.")
                ) {
            return true;
        }
        return false;
    }

    private Boolean onWednesday() {
        if (rule.contains("сред")
                || rule.contains("ср.")
                || rule.contains("wednesday")
                || rule.contains("wed.")
                ) {
            return true;
        }
        return false;
    }

    private Boolean onThursday() {
        if (rule.contains("четверг")
                || rule.contains("чт.")
                || rule.contains("thursday")
                || rule.contains("thurs.")
                ) {
            return true;
        }
        return false;
    }

    private Boolean inFriday() {
        if (rule.contains("пятниц")
                || rule.contains("пт.")
                || rule.contains("friday")
                || rule.contains("fri.")
                ) {
            return true;
        }
        return false;
    }

    private Boolean onSaturday() {
        if (rule.contains("суббот")
                || rule.contains("сб.")
                || rule.contains("saturday")
                || rule.contains("sat.")
                ) {
            return true;
        }
        return false;
    }

    private Boolean onSunday() {
        if (rule.contains("воскрес")
                || rule.contains("вс.")
                || rule.contains("sunday")
                || rule.contains("sun.")
                ) {
            return true;
        }
        return false;
    }

}
