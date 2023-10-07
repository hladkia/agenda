package cz.masterart.glada.planner;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class PlannerTest {
    public static void main(String[] args) {

        Planner _planner = new Planner(0);
        _planner.addLine(
            new PlanLine.LineBuilder(1)
                .from(LocalTime.of(10,0))
                .to(LocalTime.of(12,0))
                .workDaysOnly()
            .build()
        );

        System.out.println(
            "now: -> " + _planner.getAction(LocalDateTime.now()) +
            "\ndate1 -> " + _planner.getAction(LocalDateTime.of(2023, 10, 6, 10, 55))
        );

    }
}
