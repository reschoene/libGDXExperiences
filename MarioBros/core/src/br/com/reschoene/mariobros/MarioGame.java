package br.com.reschoene.mariobros;

import br.com.reschoene.mariobros.audio.AudioManager;
import br.com.reschoene.mariobros.screens.InfoScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Iterator;
import java.util.Map;

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

		loadAssetManager();

		setScreen(new InfoScreen(this));
	}

	private void loadAssetManager() {
		manager = new AssetManager();

		Iterator<Map.Entry<String, String>> musicFileNames = AudioManager.getMusicByMapName().entrySet().iterator();
		while (musicFileNames.hasNext())
			manager.load(musicFileNames.next().getValue(), Music.class);

		Iterator<Map.Entry<String, String>> audioFileNames = AudioManager.getSoundsByName().entrySet().iterator();
		while (audioFileNames.hasNext())
			manager.load(audioFileNames.next().getValue(), Sound.class);

		manager.finishLoading(); //synchronous loading
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
