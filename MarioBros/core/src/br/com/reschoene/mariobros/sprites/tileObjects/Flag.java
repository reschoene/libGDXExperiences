package br.com.reschoene.mariobros.sprites.tileObjects;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.screens.PlayScreen;
import br.com.reschoene.mariobros.sprites.Mario;
import br.com.reschoene.mariobros.sprites.items.Item;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import static br.com.reschoene.mariobros.collison.FixtureFilterBits.*;
import static br.com.reschoene.mariobros.collison.FixtureFilterBits.BRICK_BIT;

public class Flag extends Item {
    public Flag(PlayScreen screen, Rectangle bounds) {
        super(screen,
                bounds.x / MarioGame.PPM,
                bounds.y / MarioGame.PPM,
                bounds.width / MarioGame.PPM,
                bounds.height / MarioGame.PPM);

        velocity = new Vector2(0, 0);
        setRegion(new Texture("MarioGFX/flag.png"));
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioGame.PPM);
        fdef.filter.categoryBits = ITEM_BIT.getValue();
        fdef.filter.maskBits = BRICK_BIT.getValue();

        fdef.shape = shape;
        b2Body.createFixture(fdef).setUserData(this);
        b2Body.setGravityScale(0);
    }

    @Override
    public void use(Mario mario) {
    }

    public void goesUp(){
        velocity.y = 0.7f;
    }

    @Override
    public void update(float dt) {
        if(getY() > 1.4)
            velocity.y = 0;

        setPosition(b2Body.getPosition().x, b2Body.getPosition().y);
        b2Body.setLinearVelocity(velocity);
    }
}
