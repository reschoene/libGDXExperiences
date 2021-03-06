package br.com.reschoene.mariobros.sprites.tileObjects;

import br.com.reschoene.mariobros.audio.AudioManager;
import br.com.reschoene.mariobros.collison.FixtureFilterBits;
import br.com.reschoene.mariobros.scenes.Hud;
import br.com.reschoene.mariobros.screens.LevelScreen;
import br.com.reschoene.mariobros.sprites.Mario;
import com.badlogic.gdx.maps.MapObject;

public class Brick extends TileObject implements HeadHittable {
    public Brick(LevelScreen screen, MapObject mapObject) {
        super(screen, mapObject);
        fixture.setUserData(this);
        setCategoryFilter(FixtureFilterBits.BRICK_BIT.getValue());
    }

    @Override
    public void onHeadHit(Mario mario) {
        if(mario.isBig()) {
            setCategoryFilter(FixtureFilterBits.DESTROYED_BIT.getValue());
            getCell().setTile(null);
            Hud.addScore(200);

            AudioManager.getSoundByName("breakBrick").play();
        }
        else
            AudioManager.getSoundByName("bump").play();
    }
}
