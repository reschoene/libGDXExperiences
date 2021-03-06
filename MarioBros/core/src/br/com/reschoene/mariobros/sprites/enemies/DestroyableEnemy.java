package br.com.reschoene.mariobros.sprites.enemies;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.screens.PlayScreen;
import br.com.reschoene.mariobros.sprites.tileObjects.Mario;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import static br.com.reschoene.mariobros.collison.FixtureFilterBits.*;

public abstract class DestroyableEnemy extends Enemy{
    protected float stateTime;
    protected boolean setToDestroy;
    protected boolean destroyed;

    public DestroyableEnemy(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        stateTime = 0;
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
        shape.setRadius(6 / MarioGame.PPM);

        //categoryBits defines whats fixture is
        //maskBits defines whats this fixture collides with
        fdef.filter.categoryBits = ENEMY_BIT.getValue();
        fdef.filter.maskBits = combine(GROUND_BIT, ENEMY_BIT, BLOCK_BIT, COIN_BIT,
                BRICK_BIT, OBJECT_BIT, MARIO_BIT);

        fdef.shape = shape;
        b2Body.createFixture(fdef).setUserData(this);

        PolygonShape head = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-5, 8).scl(1 / MarioGame.PPM);
        vertices[1] = new Vector2(5, 8).scl(1 / MarioGame.PPM);
        vertices[2] = new Vector2(-3, 3).scl(1 / MarioGame.PPM);
        vertices[3] = new Vector2(3, 3).scl(1 / MarioGame.PPM);
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
