package br.com.reschoene.mariobros.sprites.enemies;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Goomba extends DestroyableEnemy {
    private Animation walkAnimation;
    private Array<TextureRegion> frames;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        frames = new Array<>();
        for (int i = 0; i < 2; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
        walkAnimation = new Animation(0.4f, frames);

        setBounds(getX(), getY(), 16 / MarioGame.PPM, 16 / MarioGame.PPM);
    }

    public void update(float dt) {
        super.update(dt);

        if (destroyed)
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
        else
            setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime, true));
    }

    @Override
    public float getDefaultXVelocity() {
        return 0.4f;
    }
}
