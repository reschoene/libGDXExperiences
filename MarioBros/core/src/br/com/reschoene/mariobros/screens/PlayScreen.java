package br.com.reschoene.mariobros.screens;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.collison.WorldContactListener;
import br.com.reschoene.mariobros.scenes.Controller;
import br.com.reschoene.mariobros.scenes.Hud;
import br.com.reschoene.mariobros.sprites.enemies.Enemy;
import br.com.reschoene.mariobros.sprites.items.Item;
import br.com.reschoene.mariobros.sprites.items.ItemDef;
import br.com.reschoene.mariobros.sprites.items.Mushroom;
import br.com.reschoene.mariobros.sprites.tileObjects.Mario;
import br.com.reschoene.mariobros.util.B2WorldCreator;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class PlayScreen implements Screen {
    private MarioGame game;
    private TextureAtlas atlas;
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;
    private Controller controller;

    //Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    //sprites
    private Mario player;

    private Music music;

    private B2WorldCreator creator;

    private Array<Item> items;
    private LinkedBlockingDeque<ItemDef> itemsToSpawn;

    //result of number of ground tiles * width of a ground tile = screen pixels
    private static final int DISTANCE_TO_ACTIVATE_ENEMIES = 224;

    public PlayScreen(MarioGame game) {
        atlas = new TextureAtlas("Mario_and_Enemies.atlas");

        this.game = game;
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(MarioGame.V_WIDTH / MarioGame.PPM, MarioGame.V_HEIGHT / MarioGame.PPM, gamecam);
        hud = new Hud(game.batch);
        controller = new Controller(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("map01.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioGame.PPM);

        gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);
        creator.createMapObjects();

        player = new Mario(this);

        world.setContactListener(new WorldContactListener());

        music = MarioGame.manager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.play();

        items = new Array<>();
        itemsToSpawn = new LinkedBlockingDeque<>();

    }

    public void spawnItem(ItemDef itemDef){
        itemsToSpawn.add(itemDef);
    }

    public void handleSpawnItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef itemDef = itemsToSpawn.poll();
            if(itemDef.type == Mushroom.class){
                items.add(new Mushroom(this, itemDef.position.x, itemDef.position.y));
            }
        }
    }

    public TextureAtlas getAtlas(){
        return this.atlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(){
        if(player.currentState != Mario.State.DEAD) {
            if ((controller.isUpPressed() || controller.isDownActionPressed()) && player.b2Body.getLinearVelocity().y == 0)
                player.b2Body.applyLinearImpulse(new Vector2(0, 4f), player.b2Body.getWorldCenter(), true);
            if ((controller.isRightPressed()) && (player.b2Body.getLinearVelocity().x <= 2))
                player.b2Body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2Body.getWorldCenter(), true);
            if ((controller.isLeftPressed()) && (player.b2Body.getLinearVelocity().x >= -2))
                player.b2Body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2Body.getWorldCenter(), true);
        }
    }

    public void update(float delta){
        handleInput();
        handleSpawnItems();

        world.step(1/60f /*60 times per second*/, 6, 2);

        player.update(delta);
        hud.update(delta);

        for(Enemy enemy : creator.getGoombas()){
            enemy.update(delta);
            if (enemy.getX() < player.getX() + (DISTANCE_TO_ACTIVATE_ENEMIES/ MarioGame.PPM))
                enemy.b2Body.setActive(true);
        }

        for(Item item: items)
            item.update(delta);

        if(player.currentState != Mario.State.DEAD)
            gamecam.position.x = player.b2Body.getPosition().x;

        gamecam.update();
        renderer.setView(gamecam);
    }

    public boolean gameOver(){
        return (player.currentState == Mario.State.DEAD && player.getStateTimer() > 3);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        //b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);

        for(Enemy enemy : creator.getGoombas())
            enemy.draw(game.batch);

        for(Item item: items)
            item.draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        if(Gdx.app.getType() == Application.ApplicationType.Android)
            controller.draw();

        if(gameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        controller.resize(width, height);
    }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        controller.dispose();
    }
}
