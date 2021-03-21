package br.com.reschoene.mariobros.util;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.sprites.tileObjects.Flag;
import br.com.reschoene.mariobros.sprites.tileObjects.FlagPole;
import br.com.reschoene.mariobros.scenes.MapLayers;
import br.com.reschoene.mariobros.screens.PlayScreen;
import br.com.reschoene.mariobros.sprites.enemies.Enemy;
import br.com.reschoene.mariobros.sprites.enemies.Goomba;
import br.com.reschoene.mariobros.sprites.enemies.Turtle;
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
    private Array<Turtle> turtles;
    private Flag flag;

    public B2WorldCreator(PlayScreen screen) {
        this.screen = screen;
        this.world = screen.getWorld();
        this.map = screen.getMap();
    }

    public void createMapObjects() {
        for (RectangleMapObject object : getMapObjsByLayer(MapLayers.COIN))
            new Coin(screen, object);

        for (RectangleMapObject object : getMapObjsByLayer(MapLayers.BRICK))
            new Brick(screen, object);

        for (RectangleMapObject object : getMapObjsByLayer(MapLayers.PIPE))
            new Pipe(screen, object);

        for (RectangleMapObject object : getMapObjsByLayer(MapLayers.GROUND))
            new Ground(screen, object);

        FlagPole flagPole = null;
        for (RectangleMapObject object : getMapObjsByLayer(MapLayers.BLOCK)) {
            if (object.getProperties().containsKey("bandeira"))
                this.flag = new Flag(screen, object.getRectangle());
            else if (object.getProperties().containsKey("haste"))
                flagPole = new FlagPole(screen, object);
            else
                new Block(screen, object);
        }
        if (this.flag != null && flagPole != null) {
            flagPole.flag = this.flag;
        }

        //create all gombas
        goombas = new Array<>();
        for (RectangleMapObject object : getMapObjsByLayer(MapLayers.GOOMBA)){
            Rectangle rect = object.getRectangle();
            goombas.add(new Goomba(screen, rect.x / MarioGame.PPM, rect.y / MarioGame.PPM));
        }

        //create all turtles
        turtles = new Array<>();
        for (RectangleMapObject object : getMapObjsByLayer(MapLayers.TURTLE)){
            Rectangle rect = object.getRectangle();
            turtles.add(new Turtle(screen, rect.x / MarioGame.PPM, rect.y / MarioGame.PPM, object.getProperties().containsKey("hasWings")));
        }
    }

    private Array<RectangleMapObject> getMapObjsByLayer(MapLayers mapLayer){
        return map.getLayers().get(mapLayer.getIdx()).getObjects().getByType(RectangleMapObject.class);
    }

    public Array<Enemy> getEnemies(){
        Array<Enemy> enemies = new Array<>();
        enemies.addAll(goombas);
        enemies.addAll(turtles);
        return enemies;
    }

    public Flag getFlag() {
        return flag;
    }
}
