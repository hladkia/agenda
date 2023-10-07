package cz.masterart.glada.planner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Planner {
    public static int NO_ACTION = -1;
    private final List<PlanLine> planLines = new ArrayList<>();
    private final int defaultAction;
    public Planner(int defaultAction) { this.defaultAction = defaultAction; }
    public void addLine(PlanLine line) { this.planLines.add(line); }
    public int getAction(LocalDateTime at) {
        return planLines.stream()
            .map(_line -> _line.getAction(at))
            .filter(_action -> _action > NO_ACTION)
            .findFirst()
            .orElse(defaultAction);
    }
}
