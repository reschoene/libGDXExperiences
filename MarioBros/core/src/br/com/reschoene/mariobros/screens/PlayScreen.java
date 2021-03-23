package br.com.reschoene.mariobros.screens;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.audio.AudioManager;
import br.com.reschoene.mariobros.collison.WorldContactListener;
import br.com.reschoene.mariobros.scenes.Controller;
import br.com.reschoene.mariobros.scenes.Hud;
import br.com.reschoene.mariobros.sprites.enemies.Enemy;
import br.com.reschoene.mariobros.sprites.items.Flower;
import br.com.reschoene.mariobros.sprites.items.Item;
import br.com.reschoene.mariobros.sprites.items.ItemDef;
import br.com.reschoene.mariobros.sprites.items.Mushroom;
import br.com.reschoene.mariobros.sprites.Mario;
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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.concurrent.LinkedBlockingDeque;

public class PlayScreen implements Screen {
    public final String mapFileName;
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
    private String mapToChange = "";
    private boolean changeScreenToLives = false;
    public static int currentWorld = 1;
    public static int currentPhase = 1;

    //result of number of ground tiles * width of a ground tile = screen pixels
    private static final int DISTANCE_TO_ACTIVATE_ENEMIES = 224;

    public PlayScreen(MarioGame game){
        this(game, "map01.tmx");
    }

    public PlayScreen(MarioGame game, String mapFileName) {
        atlas = GameAtlas.getAtlas();

        this.game = game;
        this.mapFileName = mapFileName;
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(MarioGame.V_WIDTH / MarioGame.PPM, MarioGame.V_HEIGHT / MarioGame.PPM, gamecam);
        hud = new Hud(game.batch);
        controller = new Controller(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load(mapFileName);
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioGame.PPM);

        gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);
        creator.createMapObjects();

        player = new Mario(this);

        world.setContactListener(new WorldContactListener());

        music = AudioManager.getMusicByMapName(mapFileName);
        music.setLooping(true);
        music.play();

        items = new Array<>();
        itemsToSpawn = new LinkedBlockingDeque<>();

    }

    public void changeMap(String mapToChange){
        this.mapToChange = mapToChange;
    }

    public void spawnItem(ItemDef itemDef){
        itemsToSpawn.add(itemDef);
    }

    public void handleSpawnItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef itemDef = itemsToSpawn.poll();
            if(itemDef.type == Mushroom.class){
                if (!player.isBig())
                    items.add(new Mushroom(this, itemDef.position.x, itemDef.position.y));
                else
                    items.add(new Flower(this, itemDef.position.x-0.08f, itemDef.position.y-0.08f));
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
            if ((controller.isUpPressed() || controller.isDownActionPressed()))
                player.jump();
            if (controller.isRightPressed())
                player.moveRight();;
            if (controller.isLeftPressed())
                player.moveLeft();
            if (controller.isRightActionPressed())
                player.fire();
        }
    }

    public void update(float delta){
        handleInput();
        handleSpawnItems();

        world.step(1/60f /*60 times per second*/, 6, 2);

        player.update(delta);
        hud.update(delta);

        for(Enemy enemy : creator.getEnemies()){
            enemy.update(delta);

            if(!enemy.destroyed)
                if (enemy.getX() < player.getX() + (DISTANCE_TO_ACTIVATE_ENEMIES/ MarioGame.PPM)) {
                    enemy.b2Body.setActive(true);
                }
        }

        for(Item item: items)
            item.update(delta);

        if(creator.getFlag() != null)
            creator.getFlag().update(delta);

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

        for(Enemy enemy : creator.getEnemies())
            enemy.draw(game.batch);

        for(Item item: items)
            item.draw(game.batch);

        if(creator.getFlag() != null)
            creator.getFlag().draw(game.batch);

        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        if(Gdx.app.getType() == Application.ApplicationType.Android)
            controller.draw();

        if(gameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }

        if(changeScreenToLives && player.getStateTimer() > 2){
            changeScreenToLives = false;
            game.setScreen(new InfoScreen(game, player.getLives(), mapFileName));
            dispose();
        }

        if(!"".equals(mapToChange)){
            game.setScreen(new InfoScreen(game, player.getLives(),  mapToChange));
            dispose();
        }
    }

    public Music getMusic(){
        return music;
    }

    public void showLiveScreen() {
        changeScreenToLives = true;
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

    public MarioGame getGame(){
        return game;
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
