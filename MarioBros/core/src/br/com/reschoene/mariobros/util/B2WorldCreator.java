package br.com.reschoene.mariobros.util;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.scenes.MapLayers;
import br.com.reschoene.mariobros.screens.PlayScreen;
import br.com.reschoene.mariobros.sprites.enemies.Goomba;
import br.com.reschoene.mariobros.sprites.tileObjects.*;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class B2WorldCreator {
    private final TiledMap map;
    private final World world;
    private final PlayScreen screen;
    private Array<Goomba> goombas;

    public B2WorldCreator(PlayScreen screen) {
        this.screen = screen;
        this.world = screen.getWorld();
        this.map = screen.getMap();
    }

    public void createMapObjects() {
        for (RectangleMapObject object : getMapObjsByLayer(MapLayers.COIN))
            new Coin(screen, object.getRectangle());

        for (RectangleMapObject object : getMapObjsByLayer(MapLayers.BRICK))
            new Brick(screen, object.getRectangle());

        for (RectangleMapObject object : getMapObjsByLayer(MapLayers.PIPE))
            new Pipe(screen, object.getRectangle());

        for (RectangleMapObject object : getMapObjsByLayer(MapLayers.GROUND))
            new Ground(screen, object.getRectangle());

        for (RectangleMapObject object : getMapObjsByLayer(MapLayers.BLOCK))
            new Block(screen, object.getRectangle());

        //create all gombas
        goombas = new Array<>();
        for (RectangleMapObject object : getMapObjsByLayer(MapLayers.GOOMBA)){
            Rectangle rect = object.getRectangle();
            goombas.add(new Goomba(screen, rect.x / MarioGame.PPM, rect.y / MarioGame.PPM));
        }
    }

    private Array<RectangleMapObject> getMapObjsByLayer(MapLayers mapLayer){
        return map.getLayers().get(mapLayer.getIdx()).getObjects().getByType(RectangleMapObject.class);
    }

    public Array<Goomba> getGoombas(){
        return goombas;
    }
}
