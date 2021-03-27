package br.com.reschoene.mariobros.scenes;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.audio.AudioManager;
import br.com.reschoene.mariobros.screens.GameAtlas;
import br.com.reschoene.mariobros.screens.PlayScreen;
import br.com.reschoene.mariobros.util.GameState;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private float timeCount;

    private final float FONT_SCALE = 0.8f;

    Label countDownLabel;
    static Label scoreLabel;
    Label timeLabel;
    Label levelLabel;
    Label worldLabel;
    Label scoreTitleLabel;
    Label coinTitleLabel;
    static Label coinLabel;

    public Hud(SpriteBatch sb){
        worldTimer = 300;
        timeCount = 0;

        viewport = new FitViewport(MarioGame.V_WIDTH, MarioGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        Label.LabelStyle whiteStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        countDownLabel = new Label(String.format("%03d", worldTimer), whiteStyle);
        scoreLabel = new Label(String.format("%06d", GameState.score), whiteStyle);
        levelLabel = new Label(String.format("%d-%d", PlayScreen.currentWorld, PlayScreen.currentPhase), whiteStyle);
        coinLabel = new Label(String.format("%02d", GameState.coins), whiteStyle);

        timeLabel = new Label("TIME", whiteStyle);
        worldLabel = new Label("WORLD", whiteStyle);
        scoreTitleLabel = new Label("SCORE", whiteStyle);
        coinTitleLabel = new Label("COINS", whiteStyle);

        countDownLabel.setFontScale(FONT_SCALE);
        scoreLabel.setFontScale(FONT_SCALE);
        levelLabel.setFontScale(FONT_SCALE);
        coinLabel.setFontScale(FONT_SCALE);
        timeLabel.setFontScale(FONT_SCALE);
        worldLabel.setFontScale(FONT_SCALE);
        scoreTitleLabel.setFontScale(FONT_SCALE);
        coinTitleLabel.setFontScale(FONT_SCALE);

        table.add(scoreTitleLabel).expandX().padTop(10);
        table.add(coinTitleLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(coinLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countDownLabel).expandX();

        stage.addActor(table);
    }

    public void update(float dt){
        timeCount += dt;
        if (timeCount >= 1){
            worldTimer--;
            countDownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
    }

    public static void addScore(int value){
        GameState.score += value;
        scoreLabel.setText(String.format("%06d", GameState.score));
    }

    public static void addCoin(int value){
        GameState.coins += value;

        if (GameState.coins >= 100){
            GameState.coins = 0;
            GameState.lives++;
            AudioManager.getSoundByName("powerUp").play();
        }else
            AudioManager.getSoundByName("coin").play();

        coinLabel.setText(String.format("%02d", GameState.coins));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
