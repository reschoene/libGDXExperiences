package br.com.reschoene.mariobros;

import br.com.reschoene.mariobros.screens.InfoScreen;
import br.com.reschoene.mariobros.sprites.Mario;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MarioGame extends Game {
	public static final int V_WIDTH=400;
	public static final int V_HEIGHT=208;
	public static final float PPM = 100; //pixels per meter

	public SpriteBatch batch;

	//WARNING Using AssetManager in a static way can cause issues.
	//We will use it in the static context to save time for now
	public static AssetManager manager;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		manager = new AssetManager();
		manager.load("audio/music/mario_music.ogg", Music.class);
		manager.load("audio/sounds/coin.wav", Sound.class);
		manager.load("audio/sounds/bump.wav", Sound.class);
		manager.load("audio/sounds/breakblock.wav", Sound.class);
		manager.load("audio/sounds/powerup_spawn.wav", Sound.class);
		manager.load("audio/sounds/powerup.wav", Sound.class);
		manager.load("audio/sounds/powerdown.wav", Sound.class);
		manager.load("audio/sounds/stomp.wav", Sound.class);
		manager.load("audio/sounds/mariodie.wav", Sound.class);
		manager.finishLoading(); //synchronous loading

		setScreen(new InfoScreen(this, Mario.getLives(), "map01.tmx"));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		manager.dispose();
		batch.dispose();
	}
}
