package io.github.nelsonrs.mystic.tiled;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import io.github.nelsonrs.mystic.Main;
import io.github.nelsonrs.mystic.asset.AssetService;
import io.github.nelsonrs.mystic.asset.AtlasAsset;
import io.github.nelsonrs.mystic.component.Animation2D;
import io.github.nelsonrs.mystic.component.Animation2D.AnimationType;
import io.github.nelsonrs.mystic.component.Controller;
import io.github.nelsonrs.mystic.component.Facing;
import io.github.nelsonrs.mystic.component.Facing.FacingDirection;
import io.github.nelsonrs.mystic.component.Fsm;
import io.github.nelsonrs.mystic.component.Graphic;
import io.github.nelsonrs.mystic.component.Move;
import io.github.nelsonrs.mystic.component.Transform;

public class TiledAshleyConfigurator {
    private final Engine engine;
    private final AssetService assetService;

    public TiledAshleyConfigurator(Engine engine, AssetService assetService) {
        this.engine = engine;
        this.assetService = assetService;
    }

    public void onLoadObject(TiledMapTileMapObject tileMapObject) {
        Entity entity = this.engine.createEntity();
        TiledMapTile tile = tileMapObject.getTile();
        TextureRegion textureRegion = getTextureRegion(tile);
        int z = tile.getProperties().get("z", 1, Integer.class);

        entity.add(new Graphic(Color.WHITE.cpy(), textureRegion));
        addEntityTransform(tileMapObject, z, textureRegion, entity);
        addEntityController(tileMapObject, entity);
        addEntityMove(tile, entity);
        addEntityAnimation(tile, entity);
        entity.add(new Facing(FacingDirection.DOWN));
        entity.add(new Fsm(entity));

        this.engine.addEntity(entity);
    }

    private void addEntityAnimation(TiledMapTile tile, Entity entity) {
        String animationStr = tile.getProperties().get("animation", "", String.class);
        if (animationStr.isBlank()) {
            return;
        }

        AnimationType animationType = AnimationType.valueOf(animationStr);
        String atlasAssetStr = tile.getProperties().get("atlasAsset", "OBJECTS", String.class);
        AtlasAsset atlasAsset = AtlasAsset.valueOf(atlasAssetStr);
        FileTextureData textureData = (FileTextureData) tile.getTextureRegion().getTexture().getTextureData();
        String atlasKey = textureData.getFileHandle().nameWithoutExtension();
        float speed = tile.getProperties().get("animationSpeed", 0f, Float.class);
        entity.add(new Animation2D(atlasAsset, atlasKey, animationType, PlayMode.LOOP, speed));
    }

    private void addEntityMove(TiledMapTile tile, Entity entity) {
        float speed = tile.getProperties().get("speed", 0f, Float.class);
        if (speed == 0f) {
            return;
        }
        entity.add(new Move(speed));
    }

    private void addEntityController(TiledMapTileMapObject tileMapObject, Entity entity) {
        boolean controller = tileMapObject.getProperties().get("controller", false, Boolean.class);
        if (!controller) {
            return;
        }
        entity.add(new Controller());
    }

    private void addEntityTransform(
        TiledMapTileMapObject tileMapObject, int z,
        TextureRegion textureRegion,
        Entity entity
    ) {
        Vector2 position = new Vector2(tileMapObject.getX(), tileMapObject.getY());
        Vector2 size = new Vector2(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        Vector2 scaling = new Vector2(tileMapObject.getScaleX(), tileMapObject.getScaleY());

        position.scl(Main.UNIT_SCALE);
        size.scl(Main.UNIT_SCALE);

        entity.add(new Transform(position, z, size, scaling, 0f));
    }

    private TextureRegion getTextureRegion(TiledMapTile tile) {
        String atlasAssetStr = tile.getProperties().get("atlasAsset", AtlasAsset.OBJECTS.name(), String.class);
        AtlasAsset atlasAsset = AtlasAsset.valueOf(atlasAssetStr);
        TextureAtlas textureAtlas = this.assetService.get(atlasAsset);
        FileTextureData textureData = (FileTextureData) tile.getTextureRegion().getTexture().getTextureData();
        String atlasKey = textureData.getFileHandle().nameWithoutExtension();
        TextureAtlas.AtlasRegion region = textureAtlas.findRegion(atlasKey + "/" + atlasKey);
        if (region != null) {
            return region;
        }

        // Region not found!
        System.out.println("Region not found!");
        return tile.getTextureRegion();
    }
}
