package com.naagame.core.action.control;

import com.naagame.core.action.ActionDefinition;
import com.naagame.core.action.ActionTarget;
import com.naagame.core.action.ArgumentType;

public class DestroyInstance {
    private ActionTarget target;

    public ActionTarget getTarget() {
        return target;
    }

    public void setTarget(ActionTarget target) {
        this.target = target;
    }

    static class Definition extends ActionDefinition<DestroyInstance> {
        Definition() {
            super("control_destroy_instance", "Destroys an entity instance. Can be the self or other.");

            addArgument(ArgumentType.TARGET,
                    DestroyInstance::getTarget,
                    (o, v) -> o.setTarget((ActionTarget) v));
        }
    }
}
