package br.com.reschoene.mariobros.sprites.enemies;

import br.com.reschoene.mariobros.screens.GameAtlas;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Bowser extends Actor {
    private TextureRegion textureRegion;

    public Bowser(float x, float y){
        super();

        defineBowser();

        setBounds(x, y, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
    }

    private void defineBowser() {
        textureRegion = new TextureRegion(GameAtlas.getAtlas().findRegion("bowser"), 0, 0, 174, 32);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
