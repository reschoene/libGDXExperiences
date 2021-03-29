package br.com.reschoene.mariobros.sprites.tileObjects;

import br.com.reschoene.mariobros.audio.AudioManager;
import br.com.reschoene.mariobros.collison.FixtureFilterBits;
import br.com.reschoene.mariobros.scenes.Hud;
import br.com.reschoene.mariobros.screens.PlayScreen;
import br.com.reschoene.mariobros.sprites.Mario;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.BodyDef;

public class Brick extends TileObject implements HeadHittable {
    public Brick(PlayScreen screen, MapObject mapObject) {
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
