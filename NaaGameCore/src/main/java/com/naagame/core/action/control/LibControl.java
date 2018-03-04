package com.naagame.core.action.control;

import com.naagame.core.action.ActionDefinition;

public class LibControl {
    public static final ActionDefinition<CreateInstance> CREATE_INSTANCE = new CreateInstance.Definition();
    public static final ActionDefinition<DestroyInstance> DESTROY_INSTANCE = new DestroyInstance.Definition();
}
