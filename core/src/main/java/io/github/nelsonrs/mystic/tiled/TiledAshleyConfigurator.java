package io.github.nelsonrs.mystic.tiled;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
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
import io.github.nelsonrs.mystic.component.Physic;
import io.github.nelsonrs.mystic.component.Transform;

public class TiledAshleyConfigurator {
    private static final Vector2 DEFAULT_PHYSIC_SCALING = new Vector2(1f, 1f);

    private final Engine engine;
    private final AssetService assetService;
    private final World physicWorld;

    public TiledAshleyConfigurator(Engine engine, AssetService assetService, World physicWorld) {
        this.engine = engine;
        this.assetService = assetService;
        this.physicWorld = physicWorld;
    }

    public void onLoadTile(TiledMapTile tile, float x, float y) {
        createBody(tile.getObjects(),
            new Vector2(x, y),
            DEFAULT_PHYSIC_SCALING,
            BodyDef.BodyType.StaticBody,
            Vector2.Zero,
            "environment");
    }

    private Body createBody(MapObjects mapObjects, Vector2 position, Vector2 scaling, BodyType bodyType, Vector2 relativeTo,
                            Object userData) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(position);
        bodyDef.fixedRotation = true;

        Body body = physicWorld.createBody(bodyDef);
        body.setUserData(userData);
        for (MapObject object : mapObjects) {
            FixtureDef fixtureDef = TiledPhysics.fixtureDefOf(object, scaling, relativeTo);
            Fixture fixture = body.createFixture(fixtureDef);
            fixture.setUserData(object.getName());
            fixtureDef.shape.dispose();
        }
        return body;
    }

    public void onLoadObject(TiledMapTileMapObject tileMapObject) {
        Entity entity = this.engine.createEntity();
        TiledMapTile tile = tileMapObject.getTile();
        TextureRegion textureRegion = getTextureRegion(tile);
        int z = tile.getProperties().get("z", 1, Integer.class);

        entity.add(new Graphic(Color.WHITE.cpy(), textureRegion));
        addEntityTransform(tileMapObject, z, textureRegion, entity);
        BodyType bodyType = getObjectBodyType(tile);
        addEntityPhysic(
            tile.getObjects(),
            bodyType,
            Vector2.Zero,
            entity);
        addEntityController(tileMapObject, entity);
        addEntityMove(tile, entity);
        addEntityAnimation(tile, entity);
        entity.add(new Facing(FacingDirection.DOWN));
        entity.add(new Fsm(entity));

        this.engine.addEntity(entity);
    }

    private BodyType getObjectBodyType(TiledMapTile tile) {
        String classType = tile.getProperties().get("type", "", String.class);
        if ("Prop".equals(classType)) {
            return BodyType.StaticBody;
        }
        return BodyType.DynamicBody;
    }

    private void addEntityPhysic(MapObjects objects, BodyType bodyType, Vector2 relativeTo, Entity entity) {
        if (objects.getCount() == 0) {
            return;
        }
        Transform transform = Transform.MAPPER.get(entity);
        Body body = createBody(objects, transform.getPosition(), transform.getScaling(), bodyType, relativeTo, entity);
        entity.add(new Physic(body, transform.getPosition().cpy()));
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
