package br.com.reschoene.mariobros.sprites.items;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.screens.PlayScreen;
import br.com.reschoene.mariobros.sprites.Mario;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import static br.com.reschoene.mariobros.collison.FixtureFilterBits.*;

public class FireBall extends Sprite {
    private final Sprite owner;
    private PlayScreen screen;
    private World world;
    private Array<TextureRegion> frames;
    private Animation fireAnimation;
    private float stateTime;
    private boolean destroyed;
    private boolean setToDestroy;
    private boolean fireRight;
    private Body b2body;

    public FireBall(PlayScreen screen, float x, float y, boolean fireRight, Sprite owner){
        this.fireRight = fireRight;
        this.screen = screen;
        this.world = screen.getWorld();
        this.owner = owner;

        frames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("fireball"), i * 8, 0, 8, 8));
        }
        fireAnimation = new Animation(0.2f, frames);

        setRegion((TextureRegion) fireAnimation.getKeyFrame(0));
        setBounds(x, y, 6 / MarioGame.PPM, 6 / MarioGame.PPM);
        defineFireBall();
    }

    public void defineFireBall(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(fireRight ? getX() + 12 /MarioGame.PPM : getX() - 12 /MarioGame.PPM, getY());
        bdef.type = BodyDef.BodyType.DynamicBody;

        if(world.isLocked())
            return;

        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3 / MarioGame.PPM);
        fdef.filter.categoryBits = FIREBALL_BIT.getValue();

        if (owner instanceof Mario)
            fdef.filter.maskBits = combine(GROUND_BIT, COIN_BIT, BLOCK_BIT, BRICK_BIT, ENEMY_BIT, OBJECT_BIT);
        else
            fdef.filter.maskBits = combine(GROUND_BIT, COIN_BIT, BLOCK_BIT, BRICK_BIT, OBJECT_BIT, MARIO_BIT);

        fdef.shape = shape;
        fdef.restitution = 1;
        fdef.friction = 0;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setLinearVelocity(new Vector2(fireRight ? 2.5f : -2.5f, 2.5f));
    }

    public void update(float dt){
        if (!destroyed) {
            stateTime += dt;

            setRegion((TextureRegion) fireAnimation.getKeyFrame(stateTime, true));
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

            if((stateTime > 3 || setToDestroy)) {
                world.destroyBody(b2body);
                b2body = null;
                destroyed = true;
            }
        }

        if (!destroyed) {
            if (b2body.getLinearVelocity().y > 2f)
                b2body.setLinearVelocity(b2body.getLinearVelocity().x, 2f);
            if ((fireRight && b2body.getLinearVelocity().x < 0) || (!fireRight && b2body.getLinearVelocity().x > 0))
                setToDestroy();
        }
    }

    public void setToDestroy(){
        setToDestroy = true;
    }

    public boolean isDestroyed(){
        return destroyed;
    }
}
