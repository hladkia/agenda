package cz.masterart.glada.planner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

class PlannerTest {
    public static void main(String[] args) {

        Set<LocalDate> czechLegalHolidays = Set.of(
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 4, 7),
                LocalDate.of(2020, 4, 10),
                LocalDate.of(2020, 5, 1),
                LocalDate.of(2020, 5, 8),
                LocalDate.of(2020, 7, 5),
                LocalDate.of(2020, 7, 6),
                LocalDate.of(2020, 9, 28),
                LocalDate.of(2020, 10, 28),
                LocalDate.of(2020, 11,17),
                LocalDate.of(2020, 12, 24),
                LocalDate.of(2020, 12, 25),
                LocalDate.of(2020, 12, 26)
        );

        Planner _planner = new Planner(0, czechLegalHolidays);
        _planner.addLine(new ConditionLine.LineBuilder(3).workdaysOnly().build());
        _planner.addLine(new ConditionLine.LineBuilder(1).from(LocalTime.of(11,0)).to(LocalTime.of(13,0)).build());
        _planner.addLine(new ConditionLine.LineBuilder(2).from(LocalTime.of(17,0)).build());
        _planner.addLine(new ConditionLine.LineBuilder(4).weekDays(1,4,5).build());
        _planner.addLine(new ConditionLine.LineBuilder(5).monthDays(6, 8, 10).build());
        _planner.addLine(new ConditionLine.LineBuilder(6).months(7,11).build());

        LocalDateTime _checkedDate;
        _checkedDate = LocalDateTime.parse("2023.05.01 12:00", DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        System.out.println("Checking wekdayOnly (holiday) condition (in):  " + (_planner.getActionID(_checkedDate) == 3 ? "OK" : "Not OK")  + ". " + _planner.getCorrespondedLine(_checkedDate));
        _checkedDate = LocalDateTime.parse("2023.06.01 12:00", DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        System.out.println("Checking wekdayOnly (holiday) condition (out):  " + (_planner.getActionID(_checkedDate) == 1 ? "OK" : "Not OK")  + ". " + _planner.getCorrespondedLine(_checkedDate));
        System.out.println("Checking from-to condition (in):  " + (_planner.getActionID(_checkedDate) == 1 ? "OK" : "Not OK") + ". " + _planner.getCorrespondedLine(_checkedDate));
        _checkedDate = LocalDateTime.parse("2023.06.01 14:00", DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        System.out.println("Checking from-.. condition (out):  " + (_planner.getActionID(_checkedDate) == 4 ? "OK" : "Not OK") + ". " + _planner.getCorrespondedLine(_checkedDate));
        _checkedDate = LocalDateTime.parse("2023.06.01 18:00", DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        System.out.println("Checking from-.. condition (in):  " + (_planner.getActionID(_checkedDate) == 2 ? "OK" : "Not OK")  + ". " + _planner.getCorrespondedLine(_checkedDate));
        _checkedDate = LocalDateTime.parse("2023.10.08 17:00", DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        System.out.println("Checking wekdayOnly (weekend) condition:  " + (_planner.getActionID(_checkedDate) == 3 ? "OK" : "Not OK")  + ". " + _planner.getCorrespondedLine(_checkedDate));
        _checkedDate = LocalDateTime.parse("2023.06.01 14:00", DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        System.out.println("Checking week days condition (in):  " + (_planner.getActionID(_checkedDate) == 4 ? "OK" : "Not OK") + ". " + _planner.getCorrespondedLine(_checkedDate));
        _checkedDate = LocalDateTime.parse("2023.06.06 14:00", DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        System.out.println("Checking week days condition (out):  " + (_planner.getActionID(_checkedDate) == 5 ? "OK" : "Not OK") + ". " + _planner.getCorrespondedLine(_checkedDate));
        System.out.println("Checking months day condition (in):  " + (_planner.getActionID(_checkedDate) == 5 ? "OK" : "Not OK") + ". " + _planner.getCorrespondedLine(_checkedDate));
        _checkedDate = LocalDateTime.parse("2023.07.25 14:00", DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        System.out.println("Checking month condition (in):  " + (_planner.getActionID(_checkedDate) == 6 ? "OK" : "Not OK") + ". " + _planner.getCorrespondedLine(_checkedDate));
    }
}
