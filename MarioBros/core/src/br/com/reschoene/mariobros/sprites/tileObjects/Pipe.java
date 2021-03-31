package br.com.reschoene.mariobros.sprites.tileObjects;

import br.com.reschoene.mariobros.collison.FixtureFilterBits;
import br.com.reschoene.mariobros.screens.LevelScreen;
import com.badlogic.gdx.maps.MapObject;

public class Pipe extends TileObject {
    public Pipe(LevelScreen screen, MapObject mapObject) {
        super(screen, mapObject, FixtureFilterBits.OBJECT_BIT.getValue());
    }
}
