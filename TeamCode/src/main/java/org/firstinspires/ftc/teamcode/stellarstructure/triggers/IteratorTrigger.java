package org.firstinspires.ftc.teamcode.stellarstructure.triggers;

import org.firstinspires.ftc.teamcode.stellarstructure.actions.Action;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.Condition;

public class IteratorTrigger extends Trigger {
    private final Condition condition;
    private final Action[] actions;
    private int index = 0;

    public IteratorTrigger(Condition condition, Action... actions) {
        this.condition = condition;
        this.actions = actions;
    }

    @Override
    public void update() {
        if (condition.evaluate()) {
            index++;
            if (index >= actions.length) {
                index = 0;
            }
            actions[index].run();
        }
    }
}
