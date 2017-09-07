package ru.igrey.dev.notifyrule;

import lombok.ToString;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@ToString
public class NotifyRule {
    private LocalDate notifyDate;
    private Long IntervalSeconds;
    private LocalTime time; //HH:mm
    private List<DayOfWeek> dayOfWeeks;
    private Boolean isPeriodical;
    private RepeatPeriod period;

    public NotifyRule(LocalDate notifyDate, Long intervalSeconds, LocalTime time, List<DayOfWeek> dayOfWeeks, Boolean isPeriodical, RepeatPeriod period) {
        this.notifyDate = notifyDate;
        IntervalSeconds = intervalSeconds;
        this.time = time;
        this.dayOfWeeks = dayOfWeeks;
        this.isPeriodical = isPeriodical;
        this.period = period;
    }

    public LocalDate getNotifyDate() {
        return notifyDate;
    }

    public void setNotifyDate(LocalDate notifyDate) {
        this.notifyDate = notifyDate;
    }

    public Long getIntervalSeconds() {
        return IntervalSeconds;
    }

    public void setIntervalSeconds(Long intervalSeconds) {
        IntervalSeconds = intervalSeconds;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public List<DayOfWeek> getDayOfWeeks() {
        return dayOfWeeks;
    }

    public void setDayOfWeeks(List<DayOfWeek> dayOfWeeks) {
        this.dayOfWeeks = dayOfWeeks;
    }

    public Boolean getPeriodical() {
        return isPeriodical;
    }

    public void setPeriodical(Boolean periodical) {
        isPeriodical = periodical;
    }

    public RepeatPeriod getPeriod() {
        return period;
    }

    public void setPeriod(RepeatPeriod period) {
        this.period = period;
    }

    public String toView() {
        return "";
    }


}
