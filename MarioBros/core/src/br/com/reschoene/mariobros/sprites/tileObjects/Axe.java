package br.com.reschoene.mariobros.sprites.tileObjects;

import br.com.reschoene.mariobros.collison.FixtureFilterBits;
import br.com.reschoene.mariobros.screens.LevelScreen;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Filter;

public class Axe extends TileObject{
    public Bridge bridge;

    public Axe(LevelScreen screen, MapObject mapObject) {
        super(screen, mapObject, FixtureFilterBits.BLOCK_BIT.getValue());
    }

    public void cutBridge(){
        getCell().setTile(null);

        Filter filter = new Filter();
        filter.maskBits = FixtureFilterBits.NOTHING_BIT.getValue();
        fixture.setFilterData(filter);
        bridge.cut();
    }
}
