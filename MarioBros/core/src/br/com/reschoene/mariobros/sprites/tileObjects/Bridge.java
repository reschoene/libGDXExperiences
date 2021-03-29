package br.com.reschoene.mariobros.sprites.tileObjects;

import br.com.reschoene.mariobros.screens.PlayScreen;
import br.com.reschoene.mariobros.sprites.Mario;
import br.com.reschoene.mariobros.sprites.items.Item;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import static br.com.reschoene.mariobros.collison.FixtureFilterBits.*;

public class Bridge extends Item {
    public Bridge(PlayScreen screen, float x, float y, float width, float height) {
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
        b2Body.setLinearVelocity(0, -1f);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        setPosition(b2Body.getPosition().x, b2Body.getPosition().y);
    }
}
