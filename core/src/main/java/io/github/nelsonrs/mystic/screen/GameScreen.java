package io.github.nelsonrs.mystic.screen;

import java.util.function.Consumer;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;
import io.github.nelsonrs.mystic.Main;
import io.github.nelsonrs.mystic.asset.MapAsset;
import io.github.nelsonrs.mystic.input.GameControllerState;
import io.github.nelsonrs.mystic.input.KeyboardController;
import io.github.nelsonrs.mystic.system.AnimationSystem;
import io.github.nelsonrs.mystic.system.ControllerSystem;
import io.github.nelsonrs.mystic.system.FacingSystem;
import io.github.nelsonrs.mystic.system.FsmSystem;
import io.github.nelsonrs.mystic.system.MoveSystem;
import io.github.nelsonrs.mystic.system.RenderSystem;
import io.github.nelsonrs.mystic.tiled.TiledAshleyConfigurator;
import io.github.nelsonrs.mystic.tiled.TiledService;

public class GameScreen extends ScreenAdapter {
    private final Engine engine;
    private final TiledService tiledService;
    private final TiledAshleyConfigurator tiledAshleyConfigurator;
    private final KeyboardController keyboardController;
    private final Main game;

    public GameScreen(Main game) {
        this.game = game;
        this.tiledService = new TiledService(game.getAssetService());
        this.engine = new Engine();
        this.tiledAshleyConfigurator = new TiledAshleyConfigurator(this.engine, game.getAssetService());
        this.keyboardController = new KeyboardController(GameControllerState.class, engine);

        this.engine.addSystem(new ControllerSystem());
        this.engine.addSystem(new MoveSystem());
        this.engine.addSystem(new FsmSystem());
        this.engine.addSystem(new FacingSystem());
        this.engine.addSystem(new AnimationSystem(game.getAssetService()));
        // Order is important when adding systems to the engine
        this.engine.addSystem(new RenderSystem(game.getBatch(), game.getViewport(), game.getCamera()));
    }

    @Override
    public void show() {
        game.setInputProcessors(keyboardController);
        keyboardController.setActiveState(GameControllerState.class);

        Consumer<TiledMap> renderConsumer = this.engine.getSystem(RenderSystem.class)::setMap;
        this.tiledService.setMapChangeConsumer(renderConsumer);
        this.tiledService.setLoadObjectConsumer(this.tiledAshleyConfigurator::onLoadObject);

        TiledMap tiledMap = this.tiledService.loadMap(MapAsset.MAIN);
        this.tiledService.setMap(tiledMap);
    }

    @Override
    public void hide() {
        this.engine.removeAllEntities();
    }

    @Override
    public void render(float delta) {
        delta = Math.min(delta, 1 / 30f);
        this.engine.update(delta);
    }

    @Override
    public void dispose() {
        for (EntitySystem system : this.engine.getSystems()) {
            if (system instanceof Disposable disposableSystem) {
                disposableSystem.dispose();
            }
        }
    }
}
