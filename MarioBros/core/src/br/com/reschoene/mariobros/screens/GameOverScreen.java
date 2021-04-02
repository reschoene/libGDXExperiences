package br.com.reschoene.mariobros.screens;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.scenes.Controller;
import br.com.reschoene.mariobros.util.GameState;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameOverScreen implements Screen {
    private Viewport viewport;
    private Stage stage;

    private Game game;
    private Controller controller;
    private final float SUBTITLE_SCALE = 0.85f;

    public GameOverScreen(Game game){
        this.game = game;
        viewport = new FitViewport(MarioGame.V_WIDTH, MarioGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((MarioGame)game).batch);
        controller = new Controller(((MarioGame) game).batch);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOverLabel = new Label("GAME OVER", font);
        Label scoreLabel = new Label(String.format("Score: %06d", GameState.score), font);
        Label levelLabel = new Label(String.format("World: %d-%d", GameState.currentWorld, GameState.currentPhase), font);
        Label upToContinueLabel = new Label("Press UP to continue", font);
        Label orLabel = new Label("or", font);
        Label downToGiveUpLabel = new Label("Press DOWN to give up", font);

        scoreLabel.setFontScale(SUBTITLE_SCALE);
        levelLabel.setFontScale(SUBTITLE_SCALE);
        upToContinueLabel.setFontScale(SUBTITLE_SCALE);
        orLabel.setFontScale(SUBTITLE_SCALE);
        downToGiveUpLabel.setFontScale(SUBTITLE_SCALE);

        table.add(gameOverLabel).colspan(2).expandX().padBottom(20);
        table.row();
        table.add(scoreLabel).right().padRight(20);
        table.add(levelLabel).left();
        table.row();
        table.add(upToContinueLabel).colspan(2).expandX().padTop(25);
        table.row();
        table.add(orLabel).colspan(2).expandX().padBottom(5);
        table.row();
        table.add(downToGiveUpLabel).colspan(2).expandX();

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    private void handleInput(){
        if (controller.isUpPressed())
            continueGame();
        else if (controller.isDownPressed()){
            dispose();
            Gdx.app.exit();
        }
    }

    private void continueGame(){
        GameState.reset(true);
        game.setScreen(new InfoScreen(game));
        dispose();
    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();

        if(Gdx.app.getType() == Application.ApplicationType.Android)
            controller.draw();
    }

    @Override
    public void resize(int width, int height) {
        controller.resize(width, height);
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
        controller.dispose();
    }
}
