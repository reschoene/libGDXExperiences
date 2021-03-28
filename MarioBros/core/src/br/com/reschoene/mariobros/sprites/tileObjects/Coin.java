package br.com.reschoene.mariobros.sprites.tileObjects;

import br.com.reschoene.mariobros.collison.FixtureFilterBits;
import br.com.reschoene.mariobros.scenes.Hud;
import br.com.reschoene.mariobros.screens.PlayScreen;
import br.com.reschoene.mariobros.sprites.Mario;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Coin extends TileObject implements Hittable, HeadHittable{
    public Coin(PlayScreen screen, MapObject mapObject) {
        super(screen, mapObject, FixtureFilterBits.COIN_BIT.getValue());
    }

    @Override
    public void onHit(Mario mario) {
        getCell().setTile(null);

        Filter filter = new Filter();
        filter.maskBits = FixtureFilterBits.NOTHING_BIT.getValue();
        fixture.setFilterData(filter);

        Hud.addScore(100);
        Hud.addCoin(1);
    }

    @Override
    protected FixtureDef createFixtureDef() {
        FixtureDef fdef =  super.createFixtureDef();
        fdef.isSensor = true;
        return fdef;
    }

    @Override
    public void onHeadHit(Mario mario) {
        onHit(mario);
    }
}
