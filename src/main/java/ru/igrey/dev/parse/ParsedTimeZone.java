package ru.igrey.dev.parse;

import java.time.LocalTime;
import java.time.ZoneOffset;

public class ParsedTimeZone {

    String currentTime;
    Boolean hasPm;
    private final static String DELIMITER = ":";

    public ParsedTimeZone(String currentTime) {
        this.currentTime = currentTime.toLowerCase().replaceAll("[^\\d:]", "");
        this.hasPm = currentTime.toLowerCase().contains("pm");
    }

    public Integer totalDiffInMinutes() {
        return sign() * (60 * (hours() - LocalTime.now(ZoneOffset.UTC).getHour()) + minutes() - LocalTime.now(ZoneOffset.UTC).getMinute());
    }


    private Integer hours() {
        String hours = currentTime.split(DELIMITER)[0];
        if (hasPm) {
            return Integer.valueOf(hours) + 12;
        }
        return Integer.valueOf(hours);
    }

    private Integer sign() {
        if (LocalTime.now(ZoneOffset.UTC).plusHours(12).getHour() - hours() > 0) {
            return 1;
        }
        return -1;
    }

    private Integer minutes() {
        Integer minutes = Integer.valueOf(currentTime.split(DELIMITER)[1]);
        return minutes;
    }


    public static void main(String[] args) {
        System.out.println(LocalTime.now(ZoneOffset.UTC).plusHours(4));
    }
}
