package br.com.reschoene.mariobros.util;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.scenes.MapLayers;
import br.com.reschoene.mariobros.screens.LevelScreen;
import br.com.reschoene.mariobros.sprites.enemies.Bowser;
import br.com.reschoene.mariobros.sprites.enemies.Enemy;
import br.com.reschoene.mariobros.sprites.enemies.Goomba;
import br.com.reschoene.mariobros.sprites.enemies.Turtle;
import br.com.reschoene.mariobros.sprites.tileObjects.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class B2WorldCreator {
    private final TiledMap map;
    private final World world;
    private final LevelScreen screen;
    private Array<Goomba> goombas;
    private Array<Turtle> turtles;
    private Flag flag;
    private Bowser bowser;
    private Bridge bridge;
    private Axe axe;
    private Princess princess;

    //result of number of ground tiles * width of a ground tile = screen pixels
    private static final int DISTANCE_TO_ACTIVATE_ENEMIES = 224;

    public B2WorldCreator(LevelScreen screen) {
        this.screen = screen;
        this.world = screen.getWorld();
        this.map = screen.getMap();
    }

    public void createMapObjects() {
        for (RectangleMapObject object : getMapObjsByLayer(MapLayers.BRICK_COIN))
            new BrickCoin(screen, object);

        for (RectangleMapObject object : getMapObjsByLayer(MapLayers.BRICK))
            new Brick(screen, object);

        for (RectangleMapObject object : getMapObjsByLayer(MapLayers.PIPE))
            new Pipe(screen, object);

        for (RectangleMapObject object : getMapObjsByLayer(MapLayers.GROUND))
            new Ground(screen, object);

        FlagPole flagPole = null;
        for (RectangleMapObject object : getMapObjsByLayer(MapLayers.BLOCK)) {
            Rectangle rect = object.getRectangle();

            if (object.getProperties().containsKey("bandeira"))
                this.flag = new Flag(screen, object.getRectangle());
            else if (object.getProperties().containsKey("haste"))
                flagPole = new FlagPole(screen, object);
            else if (object.getProperties().containsKey("bowser"))
                this.bowser = new Bowser(screen, rect.x / MarioGame.PPM, rect.y / MarioGame.PPM);
            else if (object.getProperties().containsKey("bridge"))
                this.bridge = new Bridge(screen, rect.x / MarioGame.PPM, rect.y / MarioGame.PPM, rect.width/ MarioGame.PPM, rect.height/ MarioGame.PPM);
            else if (object.getProperties().containsKey("axe"))
                this.axe = new Axe(screen, object);
            else if (object.getProperties().containsKey("princess"))
                this.princess = new Princess(screen, rect.x / MarioGame.PPM, rect.y / MarioGame.PPM, rect.width / MarioGame.PPM, rect.height / MarioGame.PPM);
            else
                new Block(screen, object);
        }
        if (this.flag != null && flagPole != null) {
            flagPole.flag = this.flag;
        }

        if(this.bridge != null && this.axe != null){
            this.axe.bridge = this.bridge;
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

        //create all coins
        for (RectangleMapObject object : getMapObjsByLayer(MapLayers.COIN))
            new Coin(screen, object);
    }

    private Array<RectangleMapObject> getMapObjsByLayer(MapLayers mapLayer){
        return map.getLayers().get(mapLayer.getIdx()).getObjects().getByType(RectangleMapObject.class);
    }

    public Array<Enemy> getEnemies(){
        Array<Enemy> enemies = new Array<>();
        enemies.addAll(goombas);
        enemies.addAll(turtles);

        if(bowser != null)
            enemies.add(bowser);

        return enemies;
    }

    public void draw(SpriteBatch batch) {
        for(Enemy enemy : getEnemies())
            enemy.draw(batch);

        if(flag != null)
            flag.draw(batch);

        if(bridge != null)
            bridge.draw(batch);

        if(princess != null)
            princess.draw(batch);
    }

    public void update(float dt) {
        for(Enemy enemy : getEnemies()){
            enemy.update(dt);

            if(!enemy.destroyed)
                if (enemy.getX() < screen.getPlayer().getX() + (DISTANCE_TO_ACTIVATE_ENEMIES/ MarioGame.PPM)) {
                    enemy.setActive(true);
                }
        }

        if(flag != null)
            flag.update(dt);

        if(bridge != null)
            bridge.update(dt);

        if(princess != null)
            princess.update(dt);
    }
}
