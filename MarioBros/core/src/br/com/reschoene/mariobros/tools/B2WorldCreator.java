package br.com.reschoene.mariobros.tools;

import br.com.reschoene.mariobros.scenes.MapLayers;
import br.com.reschoene.mariobros.sprites.Brick;
import br.com.reschoene.mariobros.sprites.Coin;
import br.com.reschoene.mariobros.sprites.Ground;
import br.com.reschoene.mariobros.sprites.Pipe;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class B2WorldCreator {
    private final TiledMap map;
    private final World world;

    public B2WorldCreator(World world, TiledMap map) {
        this.world = world;
        this.map = map;
    }

    public void createMapObjects() {
        for (RectangleMapObject object : getMapObjsByLayer(MapLayers.COIN))
            new Coin(world, map, object.getRectangle());

        for (RectangleMapObject object : getMapObjsByLayer(MapLayers.BRICK))
            new Brick(world, map, object.getRectangle());

        for (RectangleMapObject object : getMapObjsByLayer(MapLayers.PIPE))
            new Pipe(world, map, object.getRectangle());

        for (RectangleMapObject object : getMapObjsByLayer(MapLayers.GROUND))
            new Ground(world, map, object.getRectangle());
    }

    private Array<RectangleMapObject> getMapObjsByLayer(MapLayers mapLayer){
        return map.getLayers().get(mapLayer.getIdx()).getObjects().getByType(RectangleMapObject.class);
    }
}
