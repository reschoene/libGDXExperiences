package br.com.reschoene.mariobros.sprites.tileObjects;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.audio.AudioManager;
import br.com.reschoene.mariobros.collison.FixtureFilterBits;
import br.com.reschoene.mariobros.scenes.Hud;
import br.com.reschoene.mariobros.screens.PlayScreen;
import br.com.reschoene.mariobros.sprites.Mario;
import br.com.reschoene.mariobros.sprites.items.ItemDef;
import br.com.reschoene.mariobros.sprites.items.Mushroom;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;

public class BrickCoin extends TileObject implements HeadHittable{
    private static TiledMapTileSet tileSet;
    private final int BLACK_COIN = 28;

    public BrickCoin(PlayScreen screen, MapObject mapObject) {
        super(screen, mapObject, FixtureFilterBits.COIN_BIT.getValue());
        tileSet = getTileSet();
        fixture.setUserData(this);
    }

    @Override
    public void onHeadHit(Mario mario) {
        if (getCell().getTile().getId() == BLACK_COIN)
            AudioManager.getSoundByName("bump").play();
        else{
            if (mapObject.getProperties().containsKey("mushroom")){
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / MarioGame.PPM), Mushroom.class));
                AudioManager.getSoundByName("powerUpSpawn").play();
                Hud.addScore(500);
            }else{
                Hud.addCoin(1);
                Hud.addScore(100);
            }
        }

        getCell().setTile(tileSet.getTile(BLACK_COIN));
    }
}
