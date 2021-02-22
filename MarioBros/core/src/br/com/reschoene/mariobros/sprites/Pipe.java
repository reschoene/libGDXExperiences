package br.com.reschoene.mariobros.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Pipe extends TileObject {
    public Pipe(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
    }
}
