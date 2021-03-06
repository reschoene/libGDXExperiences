package br.com.reschoene.mariobros.sprites.enemies;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.screens.PlayScreen;
import br.com.reschoene.mariobros.sprites.tileObjects.Mario;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Turtle extends DestroyableEnemy {
    private Animation walkAnimation;
    private Array<TextureRegion> frames;

    public enum State{WALKING, SHELL}
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
            case SHELL:
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
    public void update(float delta) {
        setRegion(getFrame(delta));

        stateTime = currentState == previousState ? stateTime + delta : 0;
        previousState = currentState;

        if (!destroyed) {
            if (currentState == State.SHELL && stateTime > 5) {
                currentState = State.WALKING;
                velocity.x = getDefaultXVelocity();
            }

            setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - 8 / MarioGame.PPM);
            b2Body.setLinearVelocity(velocity);
        }
    }

    @Override
    public float getDefaultXVelocity() {
        return 0.5f;
    }

    @Override
    public void onHeadHit(Mario mario) {
        super.onHeadHit(mario);

        if(currentState != State.SHELL){
            currentState = State.SHELL;
            velocity.x = 0;
        }
    }
}
