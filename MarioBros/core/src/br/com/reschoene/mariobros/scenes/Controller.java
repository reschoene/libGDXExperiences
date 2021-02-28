package br.com.reschoene.mariobros.scenes;

import br.com.reschoene.mariobros.MarioGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Controller implements Disposable {
    private Viewport viewport;
    private Stage stage;

    private static final int SIZE_BTN = 20;
    private static final int BTN_PAD = 2;

    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean upActionPressed;
    private boolean downActionPressed;
    private boolean leftActionPressed;
    private boolean rightActionPressed;

    public Controller(SpriteBatch sb){
        this.viewport = new FitViewport(MarioGame.V_WIDTH, MarioGame.V_HEIGHT, new OrthographicCamera());
        this.stage = new Stage(viewport, sb);

        this.stage.addListener(new InputListener(){

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch(keycode){
                    case Input.Keys.UP:
                        upPressed = true;
                        break;
                    case Input.Keys.DOWN:
                        downPressed = true;
                        break;
                    case Input.Keys.LEFT:
                        leftPressed = true;
                        break;
                    case Input.Keys.RIGHT:
                        rightPressed = true;
                        break;
                    case Input.Keys.W:
                        upActionPressed = true;
                        break;
                    case Input.Keys.S:
                        downActionPressed = true;
                        break;
                    case Input.Keys.A:
                        leftActionPressed = true;
                        break;
                    case Input.Keys.D:
                        rightActionPressed = true;
                        break;
                }
                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                switch(keycode){
                    case Input.Keys.UP:
                        upPressed = false;
                        break;
                    case Input.Keys.DOWN:
                        downPressed = false;
                        break;
                    case Input.Keys.LEFT:
                        leftPressed = false;
                        break;
                    case Input.Keys.RIGHT:
                        rightPressed = false;
                        break;
                    case Input.Keys.W:
                        upActionPressed = false;
                        break;
                    case Input.Keys.S:
                        downActionPressed = false;
                        break;
                    case Input.Keys.A:
                        leftActionPressed = false;
                        break;
                    case Input.Keys.D:
                        rightActionPressed = false;
                        break;
                }
                return true;
            }
        });

        Gdx.input.setInputProcessor(stage);

        Image upImg = new Image(new Texture("upArrow.png"));
        upImg.setSize(SIZE_BTN, SIZE_BTN);
        upImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }
        });

        Image downImg = new Image(new Texture("downArrow.png"));
        downImg.setSize(SIZE_BTN, SIZE_BTN);
        downImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = false;
            }
        });

        Image rightImg = new Image(new Texture("rightArrow.png"));
        rightImg.setSize(SIZE_BTN, SIZE_BTN);
        rightImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });

        Image leftImg = new Image(new Texture("leftArrow.png"));
        leftImg.setSize(SIZE_BTN, SIZE_BTN);
        leftImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });

        Image upActionImg = new Image(new Texture("controlActionLightPurple.png"));
        upActionImg.setSize(SIZE_BTN, SIZE_BTN);
        upActionImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upActionPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upActionPressed = false;
            }
        });

        Image leftActionImg = new Image(new Texture("controlActionLightPurple.png"));
        leftActionImg.setSize(SIZE_BTN, SIZE_BTN);
        leftActionImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftActionPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftActionPressed = false;
            }
        });

        Image rightActionImg = new Image(new Texture("controlActionPurple.png"));
        rightActionImg.setSize(SIZE_BTN, SIZE_BTN);
        rightActionImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightActionPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightActionPressed = false;
            }
        });

        Image downActionImg = new Image(new Texture("controlActionPurple.png"));
        downActionImg.setSize(SIZE_BTN, SIZE_BTN);
        downActionImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                downActionPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                downActionPressed = false;
            }
        });

        //arrow keys
        Table tableArrows = new Table();
        tableArrows.setBounds(0,0,MarioGame.V_WIDTH, MarioGame.V_HEIGHT);
        tableArrows.left().bottom().padLeft(5);

        tableArrows.add();
        tableArrows.add(upImg).size(upImg.getWidth(), upImg.getHeight());
        tableArrows.add();
        tableArrows.row().pad(BTN_PAD, BTN_PAD, BTN_PAD, BTN_PAD);
        tableArrows.add(leftImg).size(leftImg.getWidth(), leftImg.getHeight());
        tableArrows.add();
        tableArrows.add(rightImg).size(rightImg.getWidth(), rightImg.getHeight());
        tableArrows.row().padBottom(BTN_PAD);
        tableArrows.add();
        tableArrows.add(downImg).size(downImg.getWidth(), downImg.getHeight());
        tableArrows.add();

        //action buttons
        Table tableActions = new Table();
        tableActions.setBounds(0,0,MarioGame.V_WIDTH, MarioGame.V_HEIGHT);
        tableActions.bottom().right().padRight(5).padBottom(2);

        tableActions.add();
        tableActions.add(upActionImg).size(upActionImg.getWidth(), upActionImg.getHeight());
        tableActions.add();
        tableActions.row().pad(BTN_PAD, BTN_PAD, BTN_PAD, BTN_PAD);
        tableActions.add(leftActionImg).size(leftActionImg.getWidth(), leftActionImg.getHeight());
        tableActions.add();
        tableActions.add(rightActionImg).size(rightActionImg.getWidth(), rightActionImg.getHeight());
        tableActions.row().padBottom(BTN_PAD);
        tableActions.add();
        tableActions.add(downActionImg).size(downActionImg.getWidth(), downActionImg.getHeight());
        tableActions.add();

        Table tableBackgroundActions = new Table();
        tableBackgroundActions.setBounds(0,0,MarioGame.V_WIDTH, MarioGame.V_HEIGHT);
        tableBackgroundActions.bottom().right().padRight(3).padTop(3);

        Image actionBackgroundImg = new Image(new Texture("actionBackground.png"));
        actionBackgroundImg.setSize(73, 73);
        tableBackgroundActions.add(actionBackgroundImg).size(actionBackgroundImg.getWidth(), actionBackgroundImg.getHeight());;

        stage.addActor(tableBackgroundActions);
        stage.addActor(tableArrows);
        stage.addActor(tableActions);

    }

    public void draw(){
        stage.draw();
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isUpActionPressed() {
        return upActionPressed;
    }

    public boolean isDownActionPressed() {
        return downActionPressed;
    }

    public boolean isLeftActionPressed() {
        return leftActionPressed;
    }

    public boolean isRightActionPressed() {
        return rightActionPressed;
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
