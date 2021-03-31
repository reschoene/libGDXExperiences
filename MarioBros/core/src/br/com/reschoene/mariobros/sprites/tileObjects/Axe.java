package br.com.reschoene.mariobros.sprites.tileObjects;

import br.com.reschoene.mariobros.collison.FixtureFilterBits;
import br.com.reschoene.mariobros.screens.LevelScreen;
import com.badlogic.gdx.maps.MapObject;

public class Axe extends TileObject{
    public Bridge bridge;

    public Axe(LevelScreen screen, MapObject mapObject) {
        super(screen, mapObject, FixtureFilterBits.BLOCK_BIT.getValue());
    }

    public void cutBridge(){
        bridge.cut();
    }
}
