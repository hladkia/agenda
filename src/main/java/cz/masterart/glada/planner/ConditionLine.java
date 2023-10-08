package cz.masterart.glada.planner;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ToString
@EqualsAndHashCode
public class ConditionLine {
    private LocalDateTime at;
    @Getter(AccessLevel.PACKAGE) private final int definedActionId;
    private final LocalTime from;
    private final LocalTime to;
    private final boolean workdaysOnly;
    private final Set<Integer> weekDays;
    private final Set<Integer> monthDays;
    private final Set<Integer> months;
    @Setter(AccessLevel.PACKAGE) private Set<LocalDate> holidays = new HashSet<>();
    private ConditionLine(LineBuilder builder) {
        this.definedActionId = builder.actionId;
        this.from = builder.from;
        this.to = builder.to;
        this.workdaysOnly = builder.workdaysOnly;
        this.weekDays = builder.weekDays;
        this.monthDays = builder.monthDays;
        this.months = builder.months;
    }
    public int getActionID(LocalDateTime at) {
        this.at = at;
        return inInterval() && workdayCondition() && inWeekOfDay() && inDayOfMonth() && inMonth()
            ? this.definedActionId
            : Planner.NO_ACTION;
    }
    private boolean inInterval() {
        return this.from.isBefore(LocalTime.from(this.at)) && this.to.isAfter(LocalTime.from(this.at));
    }
    private boolean workdayCondition() {
        return !workdaysOnly ||
            (
                this.at.getDayOfWeek() == DayOfWeek.SATURDAY ||
                this.at.getDayOfWeek() == DayOfWeek.SUNDAY ||
                this.holidays.stream().anyMatch(_date -> _date.getDayOfMonth() == this.at.getDayOfMonth() && _date.getMonth() == this.at.getMonth())
            );
    }
    private boolean inWeekOfDay() {
        return weekDays.isEmpty() || weekDays.contains(this.at.getDayOfWeek().getValue());
    }
    public boolean inDayOfMonth() {
        return monthDays.isEmpty() || monthDays.contains(this.at.getDayOfMonth());
    }
    public boolean inMonth() {
        return months.isEmpty() || months.contains(this.at.getMonthValue());
    }
    public static class LineBuilder {
        private final Integer actionId;
        private LocalTime from = LocalTime.MIN;
        private LocalTime to = LocalTime.MAX;
        private boolean workdaysOnly = false;
        private final Set<Integer> weekDays = new HashSet<>();
        private final Set<Integer> monthDays = new HashSet<>();
        private final Set<Integer> months = new HashSet<>();
        public LineBuilder(Integer actionId) { this.actionId = actionId; }
        public LineBuilder from(LocalTime from) { this.from = from; return this; }
        public LineBuilder to(LocalTime to) { this.to = to; return  this; }
        public LineBuilder workdaysOnly() { this.workdaysOnly = true; return this; }
        public LineBuilder weekDays(Integer... weekDays) { this.weekDays.addAll(List.of(weekDays)); return this; }
        public LineBuilder monthDays(Integer... monthDays) { this.monthDays.addAll(List.of(monthDays)); return this; }
        public LineBuilder months(Integer... months) { this.months.addAll(List.of(months)); return this; }
        public ConditionLine build(){ return new ConditionLine(this); }
    }
}
