package br.com.reschoene.mariobros.sprites.tileObjects;

import br.com.reschoene.mariobros.collison.FixtureFilterBits;
import br.com.reschoene.mariobros.screens.PlayScreen;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;

public class Block extends TileObject {
    public Block(PlayScreen screen, MapObject mapObject) {
        super(screen, mapObject, FixtureFilterBits.BLOCK_BIT.getValue());
    }
}
