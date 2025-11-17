package io.github.nelsonrs.mystic.component;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import io.github.nelsonrs.mystic.input.Command;

public class Controller implements Component {
    public static final ComponentMapper<Controller> MAPPER = ComponentMapper.getFor(Controller.class);

    private final List<Command> pressedCommands;
    private final List<Command> releasedCommands;

    public Controller() {
        this.pressedCommands = new ArrayList<>();
        this.releasedCommands = new ArrayList<>();
    }

    public List<Command> getPressedCommands() {
        return pressedCommands;
    }

    public List<Command> getReleasedCommands() {
        return releasedCommands;
    }
}
