package cz.masterart.glada.planner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * SMALL LOOP PLANNER
 * Plans to perform any different actions depending on the date and time. Each action is identified by an integer value.
 * The planner checks sequentially whether a given date matches each condition line.
 * If all conditions in a line are met, stops the check and returns the integer value specified for this line.
 * if the given date does not match any of the strings, returns the default value.
 * The default value is set in the Planner class constructor
 * Each line contains the following conditions:
 * - from:
 *      the time must be after this value. Default is start of the day (midnight);
 * - to:
 *      the time must be before this value. Default is end of the day;
 * - workdaysOnly:
 *      the date must not be a SUNDAY, SATURDAY or LEGAL HOLIDAY;
 * - days of the Week:
 *      The day of the week of the date must be in the set of days of the week.
 *      The day of the week is defined as an integer 1..7.
 *      The empty set (default) matches any day of the week;
 * - days of the Month:
 *      The day of the month of the date must be in the set of days of the month.
 *      The day of the month is defined as an integer 1..31.
 *      The empty set (default) matches any day of the month;
 * - month:
 *      The month of the date must be in the set of months.
 *      Month is defined as the integer 1..12.
 *      An empty set (default) matches any month;
 *
 * @version 01.00.00
 * @author Alexandr Hladki
 **/
public class Planner {
    public static int NO_ACTION = -1;
    private final List<ConditionLine> conditionLines = new ArrayList<>();
    private final int defaultActionId;
    private Set<LocalDate> holidays = new HashSet<>();
    /**
     * @param defaultActionId set when the date being checked does not match any line of the condition.
     */
    public Planner(int defaultActionId) { this.defaultActionId = defaultActionId; }
    public Planner(int defaultActionId, Set<LocalDate> holidays) {
        this.defaultActionId = defaultActionId;
        if (holidays != null) this.holidays = holidays;
    }

    /**
     * Add the condition line in the list
     * @param line {@link ConditionLine}
     */
    public void addLine(ConditionLine line) {
        line.setHolidays(this.holidays);
        this.conditionLines.add(line);
    }

    /**
     * Checks a date and returns the corresponding integer value specifying the action.
     * @param at the checked date
     * @return action ID. If no condition string matches the date being tested, returns the default value set in the constructor.
     */
    public int getActionID(LocalDateTime at) {
        ConditionLine _line = getCorrespondedLine(at);
        return _line != null ? _line.getDefinedActionId() : this.defaultActionId;
    }
    public ConditionLine getCorrespondedLine(LocalDateTime at) {
        return conditionLines.stream()
                .filter(_line -> _line.getActionID(at) > NO_ACTION)
                .findFirst()
                .orElse(null);
    }
}
