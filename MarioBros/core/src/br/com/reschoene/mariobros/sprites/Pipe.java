package br.com.reschoene.mariobros.sprites;

import br.com.reschoene.mariobros.MarioBros;
import br.com.reschoene.mariobros.screens.PlayScreen;
import br.com.reschoene.mariobros.collison.FixtureFilterBits;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Pipe extends TileObject {
    public Pipe(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds, FixtureFilterBits.OBJECT_BIT.getValue());
    }
}
