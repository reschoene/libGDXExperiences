package br.com.reschoene.mariobros.sprites;

import br.com.reschoene.mariobros.MarioBros;
import br.com.reschoene.mariobros.screens.PlayScreen;
import br.com.reschoene.mariobros.tools.FixtureFilterBits;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Pipe extends TileObject {
    public Pipe(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
    }

    @Override
    protected void createFixture() {
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        shape.setAsBox(bounds.getWidth()/2 / MarioBros.PPM, bounds.getHeight()/2 / MarioBros.PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = FixtureFilterBits.OBJECT_BIT.getValue();
        fixture = body.createFixture(fdef);
    }
}
