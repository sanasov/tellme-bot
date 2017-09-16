package ru.igrey.dev.parse;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class ParsedDaysOfWeek {

    String rule;

    public ParsedDaysOfWeek(String rule) {
        this.rule = rule;
    }

    public List<DayOfWeek> get() {
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
        if (onFriday()) {
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

    private Boolean onFriday() {
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
