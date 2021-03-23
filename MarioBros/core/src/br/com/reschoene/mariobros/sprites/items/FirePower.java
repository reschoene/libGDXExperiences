package br.com.reschoene.mariobros.sprites.items;

import br.com.reschoene.mariobros.screens.PlayScreen;
import br.com.reschoene.mariobros.util.GameState;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

public class FirePower {
    private static final int MAX_FIREBALLS_PER_TRIGGER = 10;
    private static final float TIME_BETWEEN_FIRES = 0.5f;

    private boolean active = false;

    private Array<FireBall> fireballs;
    private float timeSinceLastFire = 0.0f;

    public FirePower(){
        fireballs = new Array<>();
    }

    public void update(float dt) {
        timeSinceLastFire += dt;

        for(FireBall  ball : fireballs) {
            if(!ball.isDestroyed())
                ball.update(dt);
            else
                fireballs.removeValue(ball, true);
        }
    }

    public void fire(PlayScreen screen, Body b2Body, boolean runningRight) {
        if (active){
            if (fireballs.size > MAX_FIREBALLS_PER_TRIGGER) {
                if (timeSinceLastFire > TIME_BETWEEN_FIRES) {
                    fireballs.add(new FireBall(screen, b2Body.getPosition().x, b2Body.getPosition().y, runningRight ? true : false));
                    timeSinceLastFire = 0;
                }
            }else
                fireballs.add(new FireBall(screen, b2Body.getPosition().x, b2Body.getPosition().y, runningRight ? true : false));
        }
    }

    public void draw(Batch batch) {
        for(FireBall ball : fireballs)
            if (!ball.isDestroyed())
                ball.draw(batch);
    }

    public void setActive(boolean active){
        GameState.hasFirePower = active;
        this.active = active;
    }

    public boolean isActive(){
        return this.active;
    }
}
