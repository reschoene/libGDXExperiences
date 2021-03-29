package br.com.reschoene.mariobros.sprites.tileObjects;

import br.com.reschoene.mariobros.collison.FixtureFilterBits;
import br.com.reschoene.mariobros.screens.PlayScreen;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

public class Axe extends TileObject{
    public Bridge bridge;

    public Axe(PlayScreen screen, MapObject mapObject) {
        super(screen, mapObject, FixtureFilterBits.BLOCK_BIT.getValue());
    }

    public void cutBridge(){
        bridge.cut();
    }
}
