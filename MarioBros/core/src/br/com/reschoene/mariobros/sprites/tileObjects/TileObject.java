package br.com.reschoene.mariobros.sprites.tileObjects;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.scenes.MapLayers;
import br.com.reschoene.mariobros.screens.PlayScreen;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

public abstract class TileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;
    protected short categoryBits = 0x0001;
    protected PlayScreen screen;
    protected MapObject mapObject;

    public TileObject(PlayScreen screen, MapObject mapObject, short categoryBits) {
        this.mapObject = mapObject;
        this.screen = screen;
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = ((RectangleMapObject)mapObject).getRectangle();
        this.categoryBits = categoryBits;

        this.createBody();
        this.createFixture();
    }

    public TileObject(PlayScreen screen, MapObject mapObject) {
        this(screen, mapObject, (short) 0x0001);
    }

    protected void createFixture() {
        FixtureDef fdef = createFixtureDef();
        fixture = body.createFixture(fdef);
        fixture.setUserData(this);
    }

    protected FixtureDef createFixtureDef(){
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        shape.setAsBox(bounds.getWidth()/2 / MarioGame.PPM, bounds.getHeight()/2 / MarioGame.PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = categoryBits;
        return fdef;
    }

    protected void createBody() {
        BodyDef bdef = createBodyDef();
        body = world.createBody(bdef);
    }

    protected BodyDef createBodyDef() {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth()/2) / MarioGame.PPM, (bounds.getY() + bounds.getHeight()/2) / MarioGame.PPM);
        return bdef;
    }

    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(MapLayers.GRAPHIC.getIdx());

        int x = (int) (body.getPosition().x * MarioGame.PPM / 16);
        int y = (int) (body.getPosition().y * MarioGame.PPM / 16);

        return layer.getCell(x,y);
    }

    public com.badlogic.gdx.maps.tiled.TiledMapTileSet getTileSet(){
        return map.getTileSets().getTileSet("NES - Super Mario Bros - Tileset");
    }

    public MapProperties getMapProperties(){
        return mapObject.getProperties();
    }
}
