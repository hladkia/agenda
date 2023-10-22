package cz.masterart.glada.planner;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The planner selects the action ID from the first condition line found in which the entered data matches the conditions.<br/>
 * The condition line sets the next conditions:<br/>
 * <ul>
 * <li>- start (from) and stop (to) hours</li>
 * <li>- work days only (days without sunday, saturday and legal holidays)</li>
 * <li>- days os week (set of 1..7)</li>
 * <li>- days of month (set of 1..31)</li>
 * <li>- months (set of 1..12)</li>
 * </ul>
 */
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
    int getActionID(LocalDateTime at) {
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
    private boolean inDayOfMonth() {
        return monthDays.isEmpty() || monthDays.contains(this.at.getDayOfMonth());
    }
    private boolean inMonth() {
        return months.isEmpty() || months.contains(this.at.getMonthValue());
    }

    /**
     * Builds the ConditionLine class instance
     */
    public static class LineBuilder {
        private final Integer actionId;
        private LocalTime from = LocalTime.MIN;
        private LocalTime to = LocalTime.MAX;
        private boolean workdaysOnly = false;
        private final Set<Integer> weekDays = new HashSet<>();
        private final Set<Integer> monthDays = new HashSet<>();
        private final Set<Integer> months = new HashSet<>();

        /**
         * Set corresponded action id for the condition line
         * @param actionId This value condition line returns when all conditions are met.
         */
        public LineBuilder(Integer actionId) { this.actionId = actionId; }

        /**
         * Set the time of day from which the condition will apply
         * @param from LocalTime (time of day)
         * @return LineBuilder
         */
        public LineBuilder from(LocalTime from) { this.from = from; return this; }

        /**
         * Set the time of day until which the condition will apply
         * @param to LocalTime (time of day)
         * @return LineBuilder
         */
        public LineBuilder to(LocalTime to) { this.to = to; return  this; }

        /**
         * The condition will be met only on working days<br/>
         * (Except sunday, saturday and legal holidays)<br/>
         * Default this condition sets to the false.<br/>
         * Calling the method sets the condition to true.<br/>
         * @return LineBuilder
         */
        public LineBuilder workdaysOnly() { this.workdaysOnly = true; return this; }

        /**
         * Sets all days of the week when the condition is met.
         * @param weekDays comma separated integers as days of week (1..7)
         * @return LineBuilder
         */
        public LineBuilder weekDays(Integer... weekDays) {
            if (Arrays.stream(weekDays).anyMatch(_weekDay -> _weekDay < 1 || _weekDay > 7))
                throw new RuntimeException("The value of month cannot be less than 1 or greater than 7.");
            this.weekDays.addAll(List.of(weekDays));
            return this;
        }

        /**
         * Sets all days of the months when the condition is met.
         * @param monthDays comma separated integers as days of months (1..31)
         * @return LineBuilder
         */
        public LineBuilder monthDays(Integer... monthDays) {
            if (Arrays.stream(monthDays).anyMatch(_monthDay -> _monthDay < 1 || _monthDay > 31))
                throw new RuntimeException("The value of month cannot be less than 1 or greater than 31.");
            this.monthDays.addAll(List.of(monthDays));
            return this;
        }

        /**
         * Sets all days months when the condition is met.
         * @param months comma separated integers as months (1..12)
         * @return LineBuilder
         */
        public LineBuilder months(Integer... months) {
            if (Arrays.stream(months).anyMatch(_month -> _month < 1 || _month > 12))
                throw new RuntimeException("The value of month cannot be less than 1 or greater than 12.");
            this.months.addAll(List.of(months));
            return this;
        }

        /**
         * Builds the line and returns the instance of the ConditionLine class
         * @return instance of the ConditionLine class
         */
        public ConditionLine build(){
            if (from.isAfter(to)) throw new RuntimeException("The 'From' time value cannot be after the 'To' time value.");
            return new ConditionLine(this);
        }
    }
}
