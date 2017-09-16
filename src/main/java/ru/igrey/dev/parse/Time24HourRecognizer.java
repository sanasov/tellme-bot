package ru.igrey.dev.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Time24HourRecognizer {

    private Pattern pattern;
    private Matcher matcher;

    private static final String TIME24HOURS_PATTERN =
            "([01]?[0-9]|2[0-3]):[0-5][0-9]";

    public Time24HourRecognizer() {
        pattern = Pattern.compile(TIME24HOURS_PATTERN);
    }

    public String find(String notifyRule) {
        matcher = pattern.matcher(notifyRule);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

}