package br.com.reschoene.mariobros.sprites.tileObjects;

import br.com.reschoene.mariobros.screens.LevelScreen;
import br.com.reschoene.mariobros.sprites.Mario;
import br.com.reschoene.mariobros.sprites.items.Item;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import static br.com.reschoene.mariobros.collison.FixtureFilterBits.*;

public class Bridge extends Item {
    private final float GRAVITY = 9.8f;
    private boolean cutted;
    private float cuttedEllapsedTime;

    public Bridge(LevelScreen screen, float x, float y, float width, float height) {
        super(screen, x, y, width, height);

        setRegion(new TextureRegion(new Texture("sprites/bridge.png")));
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.KinematicBody;

        b2Body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth(), getHeight());

        FixtureDef fdef = new FixtureDef();
        fdef.filter.categoryBits = BLOCK_BIT.getValue();
        fdef.filter.maskBits = combine(MARIO_BIT, ENEMY_BIT, FIREBALL_BIT, BLOCK_BIT);

        fdef.shape = shape;
        b2Body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void use(Mario mario) {

    }

    public void cut() {
        cutted = true;
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (!destroyed) {
            if (cutted) {
                if (b2Body.getPosition().y < -10)
                    destroy(); //at this moment, it will be not visible at the screen anymore, so destroy it
                else {
                    cuttedEllapsedTime += dt;

                    //start falling slowly with a small fixed velocity then start falling applying gravity
                    if (cuttedEllapsedTime <= 0.5f)
                        b2Body.setLinearVelocity(0, -0.1f);
                    else
                        b2Body.setLinearVelocity(0, -GRAVITY * cuttedEllapsedTime);
                }
            }

            setPosition(b2Body.getPosition().x, b2Body.getPosition().y);
        }
    }
}
