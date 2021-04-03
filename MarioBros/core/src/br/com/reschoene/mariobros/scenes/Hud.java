package br.com.reschoene.mariobros.scenes;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.audio.AudioManager;
import br.com.reschoene.mariobros.sprites.Mario;
import br.com.reschoene.mariobros.util.GameState;
import br.com.reschoene.mariobros.util.StrFmt;
import com.badlogic.gdx.audio.Sound;
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
    private static boolean active = true;
    public Stage stage;
    private static Table table;
    private Viewport viewport;

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
    private static Sound hurry;
    private static Mario player;

    public Hud(SpriteBatch sb, Mario mario){
        player = mario;
        timeCount = 0;
        active = true;
        GameState.resetWorldTimer();

        viewport = new FitViewport(MarioGame.V_WIDTH, MarioGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        table = new Table();
        table.top();
        table.setFillParent(true);

        Label.LabelStyle whiteStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        countDownLabel = new Label(StrFmt.zeroPad(GameState.worldTimer, 3), whiteStyle);
        scoreLabel = new Label(StrFmt.zeroPad(GameState.score, 6), whiteStyle);
        levelLabel = new Label(StrFmt.format("%d-%d", GameState.currentWorld, GameState.currentPhase), whiteStyle);
        coinLabel = new Label(StrFmt.zeroPad(GameState.coins, 2), whiteStyle);

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

    public static void displayCongratMessage() {
        active = false;

        AudioManager.getSoundByName("win").play();

        Label.LabelStyle yellowStyle = new Label.LabelStyle(new BitmapFont(), Color.YELLOW);
        Label questLabel = new Label("Your quest is completed, you've saved the princess", yellowStyle);
        Label congratLabel = new Label("Congratulations!!", yellowStyle);

        questLabel.setFontScale(0.9f);
        congratLabel.setFontScale(1.1f);

        table.row().colspan(4);
        table.add(questLabel).padTop(40);
        table.row().colspan(4);
        table.add(congratLabel).padTop(10);
    }

    public void update(float dt){
        if (active) {
            timeCount += dt;
            if (timeCount >= 1) {
                tic();

                countDownLabel.setText(StrFmt.zeroPad(GameState.worldTimer, 3));
                timeCount = 0;
            }
        }
    }

    private void tic() {
        GameState.worldTimer--;

        if(GameState.worldTimer == 100){
            AudioManager.getCurrentMusic().stop();
            hurry = AudioManager.getSoundByName("hurry");
            hurry.play();
        } else if(GameState.worldTimer == 97){
            hurry.stop();
        }else if(GameState.worldTimer == 96){
            AudioManager.getMusicByMapName(player.getScreen().mapFileName+"_fast").play();
        }else if(GameState.worldTimer == 0){
            setActive(false);
            player.killMario(true);
        }
    }

    public static void addScore(int value){
        GameState.score += value;
        scoreLabel.setText(StrFmt.zeroPad(GameState.score, 6));
    }

    public static void addCoin(int value){
        GameState.coins += value;

        if (GameState.coins >= 100){
            GameState.coins = 0;
            GameState.lives++;
            AudioManager.getSoundByName("gainLife").play();
        }else
            AudioManager.playSound("coin");

        coinLabel.setText(StrFmt.zeroPad(GameState.coins, 2));
    }

    public static void setActive(boolean active){
        Hud.active = active;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
