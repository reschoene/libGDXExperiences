package br.com.reschoene.mariobros.sprites;

import br.com.reschoene.mariobros.MarioBros;
import br.com.reschoene.mariobros.scenes.MapLayers;
import br.com.reschoene.mariobros.screens.PlayScreen;
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

    public TileObject(PlayScreen screen, Rectangle bounds, short categoryBits) {
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;
        this.categoryBits = categoryBits;

        this.createBody();
        this.createFixture();
    }

    public TileObject(PlayScreen screen, Rectangle bounds) {
        this(screen, bounds, (short) 0x0001);
    }

    protected void createFixture() {
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        shape.setAsBox(bounds.getWidth()/2 / MarioBros.PPM, bounds.getHeight()/2 / MarioBros.PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = categoryBits;
        fixture = body.createFixture(fdef);
    }

    protected void createBody() {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth()/2) / MarioBros.PPM, (bounds.getY() + bounds.getHeight()/2) / MarioBros.PPM);
        body = world.createBody(bdef);
    }

    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(MapLayers.GRAPHIC.getIdx());

        int x = (int) (body.getPosition().x * MarioBros.PPM / 16);
        int y = (int) (body.getPosition().y * MarioBros.PPM / 16);

        return layer.getCell(x,y);
    }
}
