package io.github.nelsonrs.mystic.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import io.github.nelsonrs.mystic.Main;
import io.github.nelsonrs.mystic.asset.AssetService;
import io.github.nelsonrs.mystic.asset.AtlasAsset;

public class LoadingScreen extends ScreenAdapter {

    private final Main game;
    private final AssetService assetService;

    public LoadingScreen(Main main, AssetService assetService) {
        this.game = main;
        this.assetService = assetService;
    }

    @Override
    public void show() {
        for (AtlasAsset atlas : AtlasAsset.values()) {
            assetService.queue(atlas);
        }
    }

    @Override
    public void render(float delta) {
        if (this.assetService.update()) {
            Gdx.app.debug("LoadingScreen", "Finish asset loading");
            System.out.println("Loading screen finish asset loading...");
            createScreens();
            this.game.removeScreen(this);
            this.dispose();
            this.game.setScreen(GameScreen.class);
        }
    }

    private void createScreens() {
        this.game.addScreen(new GameScreen(this.game));
    }
}
