package br.com.reschoene.mariobros.sprites;

import br.com.reschoene.mariobros.MarioBros;
import br.com.reschoene.mariobros.scenes.Hud;
import br.com.reschoene.mariobros.screens.PlayScreen;
import br.com.reschoene.mariobros.collison.FixtureFilterBits;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;

public class Brick extends TileObject implements HeadHittable {
    public Brick(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(FixtureFilterBits.BRICK_BIT.getValue());
    }

    @Override
    public void onHeadHit() {
        setCategoryFilter(FixtureFilterBits.DESTROYED_BIT.getValue());
        getCell().setTile(null);
        Hud.addScore(200);

        MarioBros.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
    }
}
