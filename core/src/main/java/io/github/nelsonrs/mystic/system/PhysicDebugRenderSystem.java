package io.github.nelsonrs.mystic.system;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

public class PhysicDebugRenderSystem extends EntitySystem implements Disposable {

    private final World physicWorld;
    private final Box2DDebugRenderer box2DDebugRenderer;
    private final Camera camera;

    public PhysicDebugRenderSystem(World physicWorld, Camera camera) {
        this.physicWorld = physicWorld;
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.camera = camera;
        setProcessing(true); // processes or not can be sent as property value
    }


    @Override
    public void update(float deltaTime) {
        this.box2DDebugRenderer.render(physicWorld, camera.combined);
    }

    @Override
    public void dispose() {
        this.box2DDebugRenderer.dispose();
    }
}
