package br.com.reschoene.mariobros.sprites.items;

import br.com.reschoene.mariobros.audio.AudioManager;
import br.com.reschoene.mariobros.screens.LevelScreen;
import br.com.reschoene.mariobros.util.GameState;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class FirePower {
    private static final int MAX_FIREBALLS_PER_TRIGGER = 10;
    private static final float TIME_BETWEEN_FIRES = 0.5f;
    private final Sprite owner;
    private final LevelScreen screen;

    private boolean active = false;

    private Array<FireBall> fireballs;
    private float timeSinceLastFire = 0.0f;

    public FirePower(Sprite owner, LevelScreen screen){
        this.owner = owner;
        this.screen = screen;
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

    public void fire(Vector2 position, boolean runningRight) {
        if (active){
            if (fireballs.size > MAX_FIREBALLS_PER_TRIGGER) {
                if (timeSinceLastFire > TIME_BETWEEN_FIRES) {
                    AudioManager.playSound("fire");
                    fireballs.add(new FireBall(screen, position.x, position.y, runningRight ? true : false, owner));
                    timeSinceLastFire = 0;
                }
            }else {
                AudioManager.playSound("fire");
                fireballs.add(new FireBall(screen, position.x, position.y, runningRight ? true : false, owner));
            }
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
