package br.com.reschoene.mariobros.sprites.enemies;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.screens.PlayScreen;
import br.com.reschoene.mariobros.sprites.tileObjects.Mario;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Turtle extends Enemy {
    private Animation walkAnimation;
    private Array<TextureRegion> frames;

    public static final float KICK_LEFT_SPEED  = -2f;
    public static final float KICK_RIGHT_SPEED = 2f;

    public enum State{WALKING, STANDING_SHELL, MOVING_SHELL}
    public State currentState;
    public State previousState;
    private TextureRegion shell;

    public Turtle(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        frames = new Array<>();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 0, 0, 16, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 16, 0, 16, 24));
        shell = new TextureRegion(screen.getAtlas().findRegion("turtle"), 64, 0, 16, 24);
        walkAnimation = new Animation(0.2f, frames);
        currentState = previousState = State.WALKING;
        setBounds(getX(), getY(), 16/ MarioGame.PPM, 24 / MarioGame.PPM);
    }

    public TextureRegion getFrame(float dt){
        TextureRegion region;

        switch (currentState){
            case STANDING_SHELL:
            case MOVING_SHELL:
                region = shell;
                break;
            case WALKING:
            default:
                region = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);
                break;
        }

        if(velocity.x > 0 && !region.isFlipX())
            region.flip(true, false);
        else if(velocity.x < 0 && region.isFlipX())
            region.flip(true, false);

        return region;
    }

    @Override
    protected float getHeadRestitution() {
        return 1;
    }

    @Override
    public void update(float delta) {
        setRegion(getFrame(delta));

        stateTime = currentState == previousState ? stateTime + delta : 0;
        previousState = currentState;

        if (currentState == State.STANDING_SHELL && stateTime > 5) {
            currentState = State.WALKING;
            velocity.x = getDefaultXVelocity();
        }

        setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - 8 / MarioGame.PPM);
        b2Body.setLinearVelocity(velocity);
    }

    @Override
    public float getDefaultXVelocity() {
        return 0.5f;
    }

    @Override
    public void onHeadHit(Mario mario) {
        if(currentState != State.STANDING_SHELL){
            currentState = State.STANDING_SHELL;
            velocity.x = 0;
        }else{
            kick(mario.getX() <= this.getX()? KICK_RIGHT_SPEED: KICK_LEFT_SPEED);
        }
    }

    public void kick(float speed){
        velocity.x = speed;
        currentState = State.MOVING_SHELL;
    }

    public State getCurrentState(){
        return currentState;
    }
}
