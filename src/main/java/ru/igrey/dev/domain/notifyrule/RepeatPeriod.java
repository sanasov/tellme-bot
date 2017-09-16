package ru.igrey.dev.domain.notifyrule;

class RepeatPeriod {
    private PeriodType periodType;
    private Long periodValue;

    public RepeatPeriod(PeriodType periodType, Long periodValue) {
        this.periodType = periodType;
        this.periodValue = periodValue;
    }

    public PeriodType getPeriodType() {
        return periodType;
    }

    public void setPeriodType(PeriodType periodType) {
        this.periodType = periodType;
    }

    public Long getPeriodValue() {
        return periodValue;
    }

    public void setPeriodValue(Long periodValue) {
        this.periodValue = periodValue;
    }
}