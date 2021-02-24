package br.com.reschoene.mariobros.sprites;

import br.com.reschoene.mariobros.MarioBros;
import br.com.reschoene.mariobros.screens.PlayScreen;
import br.com.reschoene.mariobros.tools.FixtureFilterBits;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;

public class Goomba extends Enemy {
    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<>();
        for(int i=0;i<2;i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i*16, 0, 16, 16));
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / MarioBros.PPM, 16 / MarioBros.PPM);
    }

    public void update(float dt){
        stateTime += dt;
        setPosition(b2Body.getPosition().x - getWidth()/2,b2Body.getPosition().y - getHeight()/2);
        setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime, true));
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / MarioBros.PPM, 32 / MarioBros.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);

        //categoryBits defines whats fixture is
        //maskBits defines whats this fixture collides with
        fdef.filter.categoryBits = FixtureFilterBits.GROUND_BIT.getValue();
        fdef.filter.maskBits = FixtureFilterBits.combine(
                FixtureFilterBits.GROUND_BIT,
                FixtureFilterBits.COIN_BIT,
                FixtureFilterBits.BRICK_BIT,
                FixtureFilterBits.OBJECT_BIT,
                FixtureFilterBits.MARIO_BIT,
                FixtureFilterBits.ENEMY_BIT);

        fdef.shape = shape;
        b2Body.createFixture(fdef);
    }
}
