package br.com.reschoene.mariobros.sprites.enemies;

import br.com.reschoene.mariobros.MarioBros;
import br.com.reschoene.mariobros.screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import static br.com.reschoene.mariobros.collison.FixtureFilterBits.*;

public class Goomba extends Enemy {
    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<>();
        for (int i = 0; i < 2; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / MarioBros.PPM, 16 / MarioBros.PPM);
    }

    public void update(float dt) {
        stateTime += dt;

        if (setToDestroy && !destroyed) {
            world.destroyBody(b2Body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
            stateTime = 0;
        } else if (!destroyed) {
            setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
            setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime, true));
            velocity.y = b2Body.getLinearVelocity().y;
            b2Body.setLinearVelocity(velocity);
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.restitution = 0.008f; //a small restitution just to avoid it stuck on a collision
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);

        //categoryBits defines whats fixture is
        //maskBits defines whats this fixture collides with
        fdef.filter.categoryBits = ENEMY_BIT.getValue();
        fdef.filter.maskBits = combine(GROUND_BIT, ENEMY_BIT, BLOCK_BIT, COIN_BIT,
                BRICK_BIT, OBJECT_BIT, MARIO_BIT);

        fdef.shape = shape;
        b2Body.createFixture(fdef).setUserData(this);

        PolygonShape head = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-5, 8).scl(1 / MarioBros.PPM);
        vertices[1] = new Vector2(5, 8).scl(1 / MarioBros.PPM);
        vertices[2] = new Vector2(-3, 3).scl(1 / MarioBros.PPM);
        vertices[3] = new Vector2(3, 3).scl(1 / MarioBros.PPM);
        head.set(vertices);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = ENEMY_HEAD_BIT.getValue();
        b2Body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void draw(Batch batch) {
        if (!destroyed || stateTime < 1)
            super.draw(batch);
    }

    @Override
    public void onHeadHit() {
        setToDestroy = true;
    }
}
