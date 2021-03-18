package br.com.reschoene.mariobros.screens;

import br.com.reschoene.mariobros.MarioGame;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class InfoScreen implements Screen {
    private String mapFileName = "";
    private Viewport viewport;
    private Stage stage;

    private Game game;
    private float screenTime = 0.0f;
    private int lives;

    public InfoScreen(Game game, int lives, String mapFileName){
        this.game = game;
        this.lives = lives;
        this.mapFileName = mapFileName;

        viewport = new FitViewport(MarioGame.V_WIDTH, MarioGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((MarioGame)game).batch);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        TextureRegion textureRegion = new TextureRegion(GameAtlas.getAtlas().findRegion("little_mario"), 0, 0, 16, 16);

        Label worldPhaseLabel = new Label(String.format("WORLD %d - %d", PlayScreen.currentWorld, PlayScreen.currentPhase), font);
        Label livesLabel = new Label(String.valueOf(lives), font);
        Label xLabel = new Label("X", font);

        table.add(worldPhaseLabel).colspan(3);
        table.row();
        table.add(livesLabel).padTop(10f).padRight(10f);
        table.add(xLabel).padTop(10f).padRight(10f);
        table.add(new Image(textureRegion)).padTop(10f);

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        screenTime += delta;

        if(screenTime > 2) {
            game.setScreen(new PlayScreen((MarioGame) game, mapFileName));
            dispose();
        }

        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
        stage.dispose();
    }
}
