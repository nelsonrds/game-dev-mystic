package io.github.nelsonrs.mystic.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import io.github.nelsonrs.mystic.ai.AnimationState;

public class Fsm implements Component {
    public static final ComponentMapper<Fsm> MAPPER = ComponentMapper.getFor(Fsm.class);

    private final DefaultStateMachine<Entity, AnimationState> animationFsm;

    public Fsm(Entity entity) {
        this.animationFsm = new DefaultStateMachine<>(entity, AnimationState.IDLE);
    }

    public DefaultStateMachine<Entity, AnimationState> getAnimationFsm() {
        return animationFsm;
    }
}
