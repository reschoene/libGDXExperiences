package br.com.reschoene.mariobros.audio;

import br.com.reschoene.mariobros.MarioGame;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AudioManager {
    private static Map<String, AudioConfig> musicByMapName;
    private static Map<String, AudioConfig> soundsByName;

    private static Music currentMusic;

    private AudioManager(){}

    public static Music getMusicByMapName(String mapName){
        if (musicByMapName == null)
            buildMusicByName();

        AudioConfig config = musicByMapName.get(mapName);
        currentMusic = MarioGame.manager.get(config.audioFileName, Music.class);

        currentMusic.setVolume(config.volume);
        currentMusic.setLooping(true);

        return currentMusic;
    }

    public static Music getCurrentMusic(){
        return currentMusic;
    }

    private static AudioConfig getAudioConfigByName(String soundName){
        if (soundsByName == null)
            buildSoundByName();

        return soundsByName.get(soundName);
    }

    public static Sound getSoundByName(String soundName){
        String soundFileName = getAudioConfigByName(soundName).audioFileName;
        Sound sound = MarioGame.manager.get(soundFileName, Sound.class);

        return sound;
    }

    public static void playSound(String soundName){
        AudioConfig config = getAudioConfigByName(soundName);
        Sound sound = MarioGame.manager.get(config.audioFileName, Sound.class);
        sound.play(config.volume);
    }

    private static void buildSoundByName() {
        soundsByName = new HashMap<>();
        soundsByName.put("coin", new AudioConfig("audio/sounds/coin.wav", 0.5f));
        soundsByName.put("bump", new AudioConfig("audio/sounds/bump.wav"));
        soundsByName.put("breakBrick", new AudioConfig("audio/sounds/breakblock.wav"));
        soundsByName.put("powerUpSpawn", new AudioConfig("audio/sounds/powerup_spawn.wav"));
        soundsByName.put("powerUp", new AudioConfig("audio/sounds/powerup.wav"));
        soundsByName.put("powerDown", new AudioConfig("audio/sounds/powerdown.wav"));
        soundsByName.put("stomp", new AudioConfig("audio/sounds/stomp.wav"));
        soundsByName.put("marioDie", new AudioConfig("audio/sounds/mariodie.wav"));
        soundsByName.put("jump", new AudioConfig("audio/sounds/jump.wav"));
        soundsByName.put("bowserFalls", new AudioConfig("audio/sounds/bowser_falls.wav"));
        soundsByName.put("gainLife", new AudioConfig("audio/sounds/gain_life.mp3"));
        soundsByName.put("hurry", new AudioConfig("audio/sounds/hurry.mp3"));
        soundsByName.put("win", new AudioConfig("audio/sounds/win.mp3"));
        soundsByName.put("gameover", new AudioConfig("audio/sounds/gameover.mp3"));
    }

    private static void buildMusicByName() {
        musicByMapName = new HashMap<>();
        musicByMapName.put("stageClear", new AudioConfig("audio/music/stage_clear.mp3", 0.9f));
        musicByMapName.put("map01.tmx", new AudioConfig("audio/music/main_theme.ogg", 0.7f));
        musicByMapName.put("map01.tmx_fast", new AudioConfig("audio/music/main_theme_fast.mp3", 0.8f));
        musicByMapName.put("map02.tmx", new AudioConfig("audio/music/underworld.mp3", 0.7f));
        musicByMapName.put("map02.tmx_fast", new AudioConfig("audio/music/underworld_fast.mp3", 0.8f));
        musicByMapName.put("map03.tmx", new AudioConfig("audio/music/overworld.mp3", 0.7f));
        musicByMapName.put("map03.tmx_fast", new AudioConfig("audio/music/overworld_fast.mp3", 0.8f));
        musicByMapName.put("map04.tmx", new AudioConfig("audio/music/bowser_castle.mp3", 0.5f));
        musicByMapName.put("map04.tmx_fast", new AudioConfig("audio/music/bowser_castle_fast.mp3", 0.6f));
    }

    public static Map<String, AudioConfig> getMusicByMapName(){
        if (musicByMapName == null)
            buildMusicByName();
        return musicByMapName;
    }

    public static Map<String, AudioConfig> getSoundsByName(){
        if (soundsByName == null)
            buildSoundByName();
        return soundsByName;
    }

    public static AssetManager loadAssetManager() {
        AssetManager manager = new AssetManager();

        Iterator<Map.Entry<String, AudioConfig>> musicFileNames = AudioManager.getMusicByMapName().entrySet().iterator();
        while (musicFileNames.hasNext())
            manager.load(musicFileNames.next().getValue().audioFileName, Music.class);

        Iterator<Map.Entry<String, AudioConfig>> audioFileNames = AudioManager.getSoundsByName().entrySet().iterator();
        while (audioFileNames.hasNext())
            manager.load(audioFileNames.next().getValue().audioFileName, Sound.class);

        manager.finishLoading(); //synchronous loading

        return manager;
    }

    private static class AudioConfig{
        public final String audioFileName;
        public final float volume;

        private AudioConfig(String audioFileName) {
            this(audioFileName, 1);
        }

        private AudioConfig(String audioFileName, float volume) {
            this.audioFileName = audioFileName;
            this.volume = volume;
        }
    }
}
