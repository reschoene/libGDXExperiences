package br.com.reschoene.mariobros.sprites.enemies;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.screens.PlayScreen;
import br.com.reschoene.mariobros.sprites.tileObjects.Mario;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;

public abstract class DestroyableEnemy extends Enemy{
    protected boolean setToDestroy;
    protected boolean destroyed;

    public DestroyableEnemy(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        stateTime = 0;
    }

    @Override
    public void draw(Batch batch) {
        if (!destroyed || stateTime < 1)
            super.draw(batch);
    }

    @Override
    public void onHeadHit(Mario mario) {
        setToDestroy = true;
        MarioGame.manager.get("audio/sounds/stomp.wav", Sound.class).play();
    }

    @Override
    public void update(float delta) {
        stateTime += delta;

        handleDestroy();
    }

    protected void handleDestroy() {
        if (setToDestroy && !destroyed) {
            world.destroyBody(b2Body);
            destroyed = true;
            stateTime = 0;
        } else if (!destroyed) {
            setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
            velocity.y = b2Body.getLinearVelocity().y;
            b2Body.setLinearVelocity(velocity);
        }
    }
}
