package io.github.nelsonrs.mystic.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import io.github.nelsonrs.mystic.component.Move;
import io.github.nelsonrs.mystic.component.Transform;

public class MoveSystem extends IteratingSystem {

    private final Vector2 normalizedDirection = new Vector2();

    public MoveSystem() {
        super(Family.all(Move.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Move move = Move.MAPPER.get(entity);
        if (move.isRooted() || move.getDirection().isZero()) {
            return;
        }

        normalizedDirection.set(move.getDirection()).nor();
        Transform transform = Transform.MAPPER.get(entity);
        Vector2 position = transform.getPosition();
        transform.getPosition().set(
            position.x + move.getMaxSpeed() * normalizedDirection.x * deltaTime,
            position.y + move.getMaxSpeed() * normalizedDirection.y * deltaTime);

    }
}
