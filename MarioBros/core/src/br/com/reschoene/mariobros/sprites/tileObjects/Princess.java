package br.com.reschoene.mariobros.sprites.tileObjects;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.screens.LevelScreen;
import br.com.reschoene.mariobros.sprites.Mario;
import br.com.reschoene.mariobros.sprites.items.Item;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import static br.com.reschoene.mariobros.collison.FixtureFilterBits.*;

public class Princess extends Item {
    public Princess(LevelScreen screen, float x, float y, float width, float height) {
        super(screen, x, y, 19/ MarioGame.PPM, 32/ MarioGame.PPM);

        setRegion(screen.getAtlas().findRegion("princess"), 0, 0, 19, 32);
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2Body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth(), getHeight());

        FixtureDef fdef = new FixtureDef();
        fdef.filter.categoryBits = BLOCK_BIT.getValue();
        fdef.filter.maskBits = combine(MARIO_BIT, BLOCK_BIT);

        fdef.shape = shape;
        b2Body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void use(Mario mario) {

    }
}
