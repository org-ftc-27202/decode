package org.firstinspires.ftc.teamcode.stellarstructure.triggers;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.stellarstructure.actions.Action;
import org.firstinspires.ftc.teamcode.stellarstructure.conditions.Condition;

public class ActionTrigger extends Trigger {
    private final Condition condition;
    private final Action action;

    public ActionTrigger(@NonNull Condition condition, @NonNull Action action) {
        this.condition = condition;
        this.action = action;
    }

    @Override
    public void update() {
        if (condition.evaluate()) {
            action.run();
        }
    }
}