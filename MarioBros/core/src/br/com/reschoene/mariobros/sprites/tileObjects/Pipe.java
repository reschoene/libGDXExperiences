package br.com.reschoene.mariobros.sprites.tileObjects;

import br.com.reschoene.mariobros.collison.FixtureFilterBits;
import br.com.reschoene.mariobros.screens.PlayScreen;
import com.badlogic.gdx.maps.MapObject;

public class Pipe extends TileObject {
    public Pipe(PlayScreen screen, MapObject mapObject) {
        super(screen, mapObject, FixtureFilterBits.OBJECT_BIT.getValue());
    }
}
