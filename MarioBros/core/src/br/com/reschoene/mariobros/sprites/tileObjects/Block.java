package br.com.reschoene.mariobros.sprites.tileObjects;

import br.com.reschoene.mariobros.collison.FixtureFilterBits;
import br.com.reschoene.mariobros.screens.PlayScreen;
import com.badlogic.gdx.math.Rectangle;

public class Block extends TileObject {
    public Block(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds, FixtureFilterBits.BLOCK_BIT.getValue());
    }
}
