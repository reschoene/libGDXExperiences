package br.com.reschoene.mariobros.tools;

import br.com.reschoene.mariobros.sprites.Brick;
import br.com.reschoene.mariobros.sprites.Coin;
import br.com.reschoene.mariobros.sprites.Ground;
import br.com.reschoene.mariobros.sprites.Pipe;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

enum MapLayer {
    GROUND(2),
    PIPE(3),
    COIN(4),
    BRICK(5);

    private final int idx;

    MapLayer(int idx) {
        this.idx = idx;
    }

    public int getIdx() {
        return this.idx;
    }
}

public class B2WorldCreator {
    private final TiledMap map;
    private final World world;

    public B2WorldCreator(World world, TiledMap map) {
        this.world = world;
        this.map = map;
    }

    public void createMapObjects() {
        for (RectangleMapObject object : getMapObjsByLayer(MapLayer.COIN))
            new Coin(world, map, object.getRectangle());

        for (RectangleMapObject object : getMapObjsByLayer(MapLayer.BRICK))
            new Brick(world, map, object.getRectangle());

        for (RectangleMapObject object : getMapObjsByLayer(MapLayer.PIPE))
            new Pipe(world, map, object.getRectangle());

        for (RectangleMapObject object : getMapObjsByLayer(MapLayer.GROUND))
            new Ground(world, map, object.getRectangle());
    }

    private Array<RectangleMapObject> getMapObjsByLayer(MapLayer mapLayer){
        return map.getLayers().get(mapLayer.getIdx()).getObjects().getByType(RectangleMapObject.class);
    }
}
