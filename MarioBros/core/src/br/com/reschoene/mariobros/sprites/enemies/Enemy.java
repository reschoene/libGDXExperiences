package br.com.reschoene.mariobros.sprites.enemies;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.screens.PlayScreen;
import br.com.reschoene.mariobros.sprites.tileObjects.HeadHittable;
import br.com.reschoene.mariobros.sprites.tileObjects.Mario;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static br.com.reschoene.mariobros.collison.FixtureFilterBits.*;
import static br.com.reschoene.mariobros.collison.FixtureFilterBits.ENEMY_HEAD_BIT;

public abstract class Enemy extends Sprite implements HeadHittable {
    protected float stateTime;
    protected final World world;
    protected final PlayScreen screen;
    public Body b2Body;
    public Vector2 velocity;

    public Enemy(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(getDefaultXVelocity(),0);
        b2Body.setActive(false);
    }

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
        fdef.restitution = getHeadRestitution();
        fdef.filter.categoryBits = ENEMY_HEAD_BIT.getValue();
        b2Body.createFixture(fdef).setUserData(this);
    }

    protected float getHeadRestitution(){
        return 0.5f;
    }

    public void reverseVelocity(boolean x, boolean y){
        if (x)
            velocity.x = -velocity.x;
        if (y)
            velocity.y = -velocity.y;
    }

    public abstract void update(float delta);

    public abstract float getDefaultXVelocity();

    public abstract void onHeadHit(Mario mario);

    public abstract void onEnemyHit(Enemy enemy);
}
