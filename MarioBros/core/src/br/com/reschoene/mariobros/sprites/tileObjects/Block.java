package br.com.reschoene.mariobros.sprites.tileObjects;

import br.com.reschoene.mariobros.collison.FixtureFilterBits;
import br.com.reschoene.mariobros.screens.LevelScreen;
import com.badlogic.gdx.maps.MapObject;

public class Block extends TileObject {
    public Block(LevelScreen screen, MapObject mapObject) {
        super(screen, mapObject, FixtureFilterBits.BLOCK_BIT.getValue());
    }
}
