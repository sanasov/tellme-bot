package ru.igrey.dev.notifyrule.parse;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateRecognizer {

    private List<String> delimiters = Arrays.asList("/", "-", "\\.", " ");
    private String notifyRule;

    public DateRecognizer(String notifyRule) {
        this.notifyRule = notifyRule;
    }

    public LocalDate find() {
        for (String delimiter : delimiters) {
            String date = find(notifyRule, fullDatePattern(delimiter));
            if (StringUtils.isNotBlank(date)) {
                return toLocalDate(date, delimiter);
            }
            date = find(notifyRule, dayMonthPattern(delimiter));
            if (StringUtils.isNotBlank(date)) {
                return toLocalDate(date, delimiter);
            }
        }
        return null;
    }

    private String find(String notifyRule, String datePattern) {
        Pattern pattern = Pattern.compile(datePattern);
        Matcher matcher = pattern.matcher(notifyRule);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

    private LocalDate toLocalDate(String date, String delimiter) {
        String[] dateItems = date.split(delimiter);
        if (dateItems.length == 2) {
            return LocalDate.of(Year.now().getValue(), Integer.valueOf(dateItems[1]), Integer.valueOf(dateItems[0]));
        }
        if (dateItems.length == 3) {
            String year = dateItems[2].length() == 2
                    ? 20 + dateItems[2]
                    : dateItems[2];
            return LocalDate.of(Integer.valueOf(year), Integer.valueOf(dateItems[1]), Integer.valueOf(dateItems[0]));
        }
        throw new RuntimeException("bad parsing date: " + date);
    }


    public static void main(String[] args) {
//        System.out.println(new DateRecognizer("19 12 2010").find());
        System.out.println(new DateRecognizer("19/12/10 8:30").find());
        System.out.println(new DateRecognizer("23.12.2018").find());
        System.out.println(new DateRecognizer("22-12-1999").find());
        System.out.println(new DateRecognizer("19-12-23").find());
        System.out.println(new DateRecognizer("11.01").find());
        System.out.println(new DateRecognizer("12.02").find());
        System.out.println(new DateRecognizer("13.03").find());
        System.out.println(new DateRecognizer("14.04").find());
        System.out.println(new DateRecognizer("15.05").find());
        System.out.println(new DateRecognizer("16.06").find());
        System.out.println(new DateRecognizer("17.08").find());
        System.out.println(new DateRecognizer("18.09").find());
        System.out.println(new DateRecognizer("19.10").find());
        System.out.println(new DateRecognizer("20.11").find());
        System.out.println(new DateRecognizer("21-12").find());
        System.out.println(new DateRecognizer("21-12-2017").find());
    }

    private String fullDatePattern(String delimiter) {
        return "(0?[1-9]|[12][0-9]|3[01])" + delimiter + "(0?[1-9]|1[012])" + delimiter + "((20)?\\d\\d)";
    }

    private String dayMonthPattern(String delimiter) {
        return "(0?[1-9]|[12][0-9]|3[01])" + delimiter + "(1[012]|0?[1-9])";
    }
}