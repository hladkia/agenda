package cz.masterart.glada.planner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <h2>The helper for loop planning</h2>
 * Plans to perform any different actions depending on the date and time. Each action is identified by an integer value.<br/>
 * The planner checks sequentially whether a given date matches each condition line.<br/>
 * If all conditions in the line are met, stops the check and returns the integer value specified for this line.<br/>
 * if the given date does not match any of the strings, returns the default value.<br/>
 * The default value is set in the Planner class constructor<br/>
 * Each line contains the following conditions:<br/>
 * <ul>
 *  <li><b>from: </b>
 *      the time must be after this value. Default is start of the day (midnight);<br/>
 *  </li>
 *  <li><b>to: </b>
 *      the time must be before this value. Default is end of the day;<br/>
 *  </li>
 *  <li><b>workdaysOnly: </b>
 *      the date must not be a SUNDAY, SATURDAY or LEGAL HOLIDAY;<br/>
 *  </li>
 *  <li><b>days of the Week: </b><br/>
 *      The day of the week of the date must be in the set of days of the week.<br/>
 *      The day of the week is defined as an integer 1..7.<br/>
 *      The empty set (default) matches any day of the week;<br/>
 *  </li>
 *  <li><b>days of the Month: </b><br/>
 *      The day of the month of the date must be in the set of days of the month.<br/>
 *      The day of the month is defined as an integer 1..31.<br/>
 *      The empty set (default) matches any day of the month;<br/>
 *  </li>
 *  <li><b>month: </b><br/>
 *      The month of the date must be in the set of months.<br/>
 *      Month is defined as the integer 1..12.<br/>
 *      An empty set (default) matches any month;<br/>
 *</li>
 *</ul>
 * @version 01.00.00
 * @author Alexandr Hladki
 **/
public class Planner {
    /**
     * -1 is reserved for NO_ACTION value
     */
    public static int NO_ACTION = -1;
    private final List<ConditionLine> conditionLines = new ArrayList<>();
    private final int defaultActionId;
    private Set<LocalDate> holidays = new HashSet<>();
    /**
     * Planner class constructor. Legal holidays will not be set.
     * @param defaultActionId sets when the date being checked does not match any line of the condition.
     */
    public Planner(int defaultActionId) { this.defaultActionId = defaultActionId; }

    /**
     * Planner class constructor with set of legal holidays
     * @param defaultActionId sets when the date being checked does not match any line of the condition.
     * @param holidays Set of legal holidays days. When the WorkDaysOnly condition is set, the ConditionLine was not met on legal holidays.
     */
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

    /**
     * Return Condition Line witch meets the conditions
     * @param at checked data
     * @return Condition line
     */
    public ConditionLine getCorrespondedLine(LocalDateTime at) {
        return conditionLines.stream()
                .filter(_line -> _line.getActionID(at) > NO_ACTION)
                .findFirst()
                .orElse(null);
    }
}
