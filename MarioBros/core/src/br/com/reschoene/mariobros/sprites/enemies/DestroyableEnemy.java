package br.com.reschoene.mariobros.sprites.enemies;

import br.com.reschoene.mariobros.audio.AudioManager;
import br.com.reschoene.mariobros.screens.LevelScreen;
import br.com.reschoene.mariobros.sprites.Mario;
import com.badlogic.gdx.graphics.g2d.Batch;

public abstract class DestroyableEnemy extends Enemy{
    protected boolean setToDestroy;

    public DestroyableEnemy(LevelScreen screen, float x, float y) {
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
        AudioManager.getSoundByName("stomp").play();
    }

    @Override
    public void update(float delta) {
        stateTime += delta;

        handleDestroy();
    }

    protected void handleDestroy() {
        if (setToDestroy && !destroyed) {
            world.destroyBody(b2Body);
            b2Body = null;
            destroyed = true;
            System.out.println("enemy destroyed");
            stateTime = 0;
        } else if (!destroyed) {
            handleFalling();
        }
    }
}
