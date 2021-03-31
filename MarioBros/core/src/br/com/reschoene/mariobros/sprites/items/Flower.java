package br.com.reschoene.mariobros.sprites.items;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.screens.LevelScreen;
import br.com.reschoene.mariobros.sprites.Mario;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import static br.com.reschoene.mariobros.collison.FixtureFilterBits.*;

public class Flower extends Item{
    public Flower(LevelScreen screen, float x, float y) {
        super(screen, x, y);

        setRegion(new TextureRegion(screen.getAtlas().findRegion("fireflower")));

        velocity = new Vector2(0, 0);
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2Body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioGame.PPM);
        fdef.filter.categoryBits = ITEM_BIT.getValue();
        fdef.filter.maskBits = combine(MARIO_BIT);

        fdef.shape = shape;
        b2Body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void use(Mario mario) {
        mario.activateFirePower();
        destroy();
    }
}
