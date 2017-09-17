package ru.igrey.dev.parse;

import org.apache.commons.lang3.StringUtils;
import ru.igrey.dev.exception.DateTimeFormatException;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Возвращает разницу по времени в минутах между UTC и часовым поясом пользователя
 */
public class ParsedTimeZone {
    private String currentDateTime;
    private LocalDateTime localDateTime;

    public Integer diffInMinutes() {
        Duration diff = Duration.between(LocalDateTime.now(ZoneOffset.UTC), localDateTime);
        if (diff.toHours() > 14 || diff.toHours() < -11) {
            throw new DateTimeFormatException();
        }
        return (int) Duration.between(LocalDateTime.now(ZoneOffset.UTC), userRealCurrentDateTime()).toMinutes();
    }

    public ParsedTimeZone(String currentDateTime) {
        this.currentDateTime = currentDateTime;
        this.localDateTime = LocalDateTime.of(date(), time());
    }

    private LocalDate date() {
        LocalDate date = new DateRecognizer(currentDateTime).find();
        if (date == null) {
            return LocalDate.now();
        }
        return date;
    }

    private LocalTime time() {
        String timeString = new Time24HourRecognizer(currentDateTime).find();
        if (StringUtils.isBlank(timeString)) {
            throw new DateTimeFormatException();
        }
        LocalTime time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("H:mm"));
        return time;
    }


    private LocalDateTime userRealCurrentDateTime() {
        TimeZone timeZone = Stream.of(TimeZone.getAvailableIDs())
                .map(id -> TimeZone.getTimeZone(id))
                .min(Comparator.comparing(tz -> Math.abs(Duration.between(LocalDateTime.now(tz.toZoneId()), localDateTime).toMillis())))
                .orElse(null);
        return LocalDateTime.now(timeZone.toZoneId());
    }


    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
