package br.com.reschoene.mariobros.screens;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class GameAtlas {
    private static TextureAtlas textureAtlas = null;

    private GameAtlas(){}

    public static TextureAtlas getAtlas(){
        if (textureAtlas == null)
            textureAtlas = new TextureAtlas("MarioSpriteSheet.atlas");
        return textureAtlas;
    }
}
