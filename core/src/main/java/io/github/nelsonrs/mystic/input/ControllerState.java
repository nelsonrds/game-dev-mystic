package io.github.nelsonrs.mystic.input;

public interface ControllerState {

    void keyDown(Command command);

    default void keyUp(Command command) {
    }
}
