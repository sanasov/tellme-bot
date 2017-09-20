package ru.igrey.dev.domain.notifyrule;

import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import ru.igrey.dev.parse.NotifyRuleParser;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@ToString
public class NotifyRule {
    private List<LocalDate> notifyDates;
    private Long intervalSeconds;
    private LocalTime time; //HH:mm
    private List<DayOfWeek> dayOfWeeks;
    private Boolean isPeriodical;
    private RepeatPeriod period;
    private String notifyRule;

    public NotifyRule(List<LocalDate> notifyDates, Long intervalSeconds, LocalTime time, List<DayOfWeek> dayOfWeeks, Boolean isPeriodical, RepeatPeriod period) {
        this.notifyDates = notifyDates;
        this.intervalSeconds = intervalSeconds;
        this.time = time;
        this.dayOfWeeks = dayOfWeeks;
        this.isPeriodical = isPeriodical;
        this.period = period;
    }

    public NotifyRule(String notifyRule) {
        this.notifyRule = notifyRule;
    }

    public NotifyRule build() {
        if (StringUtils.isBlank(notifyRule)) {
            return null;
        }
        return new NotifyRuleParser(notifyRule).parse();
    }

    public boolean isValid() {
        if (StringUtils.isBlank(notifyRule)) {
            return false;
        }
        return new NotifyRuleParser(notifyRule).parse() != null;
    }


    public List<LocalDate> getNotifyDates() {
        return notifyDates;
    }

    public void setNotifyDates(List<LocalDate> notifyDates) {
        this.notifyDates = notifyDates;
    }

    public Long getIntervalSeconds() {
        return intervalSeconds;
    }

    public void setIntervalSeconds(Long intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
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
