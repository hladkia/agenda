package cz.masterart.glada.planner;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ToString
@EqualsAndHashCode
public class PlanLine {
    private LocalDateTime at;
    private final int action;
    private final LocalTime from;
    private final LocalTime to;
    private final boolean workdaysOnly;
    private final Set<Integer> weekDays;
    private final Set<Integer> monthDays;
    private final Set<Integer> months;
    private PlanLine(LineBuilder builder) {
        this.action = builder.action;
        this.from = builder.from;
        this.to = builder.to;
        this.workdaysOnly = builder.workdaysOnly;
        this.weekDays = builder.weekDays;
        this.monthDays = builder.monthDays;
        this.months = builder.months;
    }
    public int getAction(LocalDateTime at) {
        this.at = at;
        return inInterval() && workdayCondition() && inWeekOfDay() && inDayOfMonth() && inMonth()
            ? this.action
            : Planner.NO_ACTION;
    }
    private boolean inInterval() {
        return this.from.isBefore(LocalTime.from(this.at)) && this.to.isAfter(LocalTime.from(this.at));
    }
    private boolean workdayCondition() {
        return !workdaysOnly || !(this.at.getDayOfWeek() == DayOfWeek.SATURDAY || this.at.getDayOfWeek() == DayOfWeek.SUNDAY);
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
        private final Integer action;
        private LocalTime from;
        private LocalTime to;
        private boolean workdaysOnly = false;
        private final Set<Integer> weekDays = new HashSet<>();
        private final Set<Integer> monthDays = new HashSet<>();
        private final Set<Integer> months = new HashSet<>();
        public LineBuilder(Integer action) { this.action = action; }
        public LineBuilder from(LocalTime from) { this.from = from; return this; }
        public LineBuilder to(LocalTime to) { this.to = to; return  this; }
        public LineBuilder workDaysOnly() { this.workdaysOnly = true; return this; }
        public LineBuilder weekDays(Integer... weekDays) { this.weekDays.addAll(List.of(weekDays)); return this; }
        public LineBuilder monthDays(Integer... monthDays) { this.monthDays.addAll(List.of(monthDays)); return this; }
        public LineBuilder months(Integer... months) { this.months.addAll(List.of(months)); return this; }
        public PlanLine build(){
            return new PlanLine(this);
        }
    }
}
