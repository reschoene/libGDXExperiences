package br.com.reschoene.mariobros.sprites.tileObjects;

import br.com.reschoene.mariobros.collison.FixtureFilterBits;
import br.com.reschoene.mariobros.screens.PlayScreen;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class FlagPole extends TileObject {
    public Flag flag;

    public FlagPole(PlayScreen screen, RectangleMapObject mapObject) {
        super(screen, mapObject, FixtureFilterBits.BLOCK_BIT.getValue());
    }

    @Override
    protected FixtureDef createFixtureDef() {
        FixtureDef fixtureDef = super.createFixtureDef();
        fixtureDef.isSensor = true;
        return fixtureDef;
    }
}
