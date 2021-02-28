package br.com.reschoene.mariobros.sprites.tileObjects;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.collison.FixtureFilterBits;
import br.com.reschoene.mariobros.scenes.Hud;
import br.com.reschoene.mariobros.screens.PlayScreen;
import br.com.reschoene.mariobros.sprites.items.ItemDef;
import br.com.reschoene.mariobros.sprites.items.Mushroom;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;

public class Coin extends TileObject implements HeadHittable{
    private static TiledMapTileSet tileSet;
    private final int BLACK_COIN = 28;

    public Coin(PlayScreen screen, MapObject mapObject) {
        super(screen, mapObject);
        tileSet = map.getTileSets().getTileSet("NES - Super Mario Bros - Tileset");
        fixture.setUserData(this);
        setCategoryFilter(FixtureFilterBits.COIN_BIT.getValue());
    }

    @Override
    public void onHeadHit(Mario mario) {
        if (getCell().getTile().getId() == BLACK_COIN)
            MarioGame.manager.get("audio/sounds/bump.wav", Sound.class).play();
        else{
            if (mapObject.getProperties().containsKey("mushroom")){
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / MarioGame.PPM), Mushroom.class));
                MarioGame.manager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();
            }else{
                MarioGame.manager.get("audio/sounds/coin.wav", Sound.class).play();
            }
        }

        getCell().setTile(tileSet.getTile(BLACK_COIN));
        Hud.addScore(100);
    }
}
