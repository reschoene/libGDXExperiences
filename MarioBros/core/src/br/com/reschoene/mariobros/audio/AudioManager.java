package br.com.reschoene.mariobros.audio;

import br.com.reschoene.mariobros.MarioGame;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

public class AudioManager {
    private static Map<String, String> musicByMapName;
    private static Map<String, String> soundsByName;

    private AudioManager(){}

    public static Music getMusicByMapName(String mapName){
        if (musicByMapName == null)
            buildMusicByName();

        String musicFileName = musicByMapName.get(mapName);
        Music music = MarioGame.manager.get(musicFileName, Music.class);

        return music;
    }

    public static Sound getSoundByName(String soundName){
        if (soundsByName == null)
            buildSoundByName();

        String soundFileName = soundsByName.get(soundName);
        Sound sound = MarioGame.manager.get(soundFileName, Sound.class);

        return sound;
    }

    private static void buildSoundByName() {
        soundsByName = new HashMap<>();
        soundsByName.put("coin", "audio/sounds/coin.wav");
        soundsByName.put("bump", "audio/sounds/bump.wav");
        soundsByName.put("breakBrick", "audio/sounds/breakblock.wav");
        soundsByName.put("powerUpSpawn", "audio/sounds/powerup_spawn.wav");
        soundsByName.put("powerUp", "audio/sounds/powerup.wav");
        soundsByName.put("powerDown", "audio/sounds/powerdown.wav");
        soundsByName.put("stomp", "audio/sounds/stomp.wav");
        soundsByName.put("marioDie", "audio/sounds/mariodie.wav");
        soundsByName.put("jump", "audio/sounds/stomp.wav");
    }

    private static void buildMusicByName() {
        musicByMapName = new HashMap<>();
        musicByMapName.put("stageClear", "audio/music/stage_clear.wav");
        musicByMapName.put("map01.tmx", "audio/music/mario_music.ogg");
        musicByMapName.put("map02.tmx", "audio/music/underworld.mp3");
        musicByMapName.put("map03.tmx", "audio/music/underworld.mp3");
        musicByMapName.put("map04.tmx", "audio/music/underworld.mp3");
    }

    public static Map<String, String> getMusicByMapName(){
        if (musicByMapName == null)
            buildMusicByName();
        return musicByMapName;
    }

    public static Map<String, String> getSoundsByName(){
        if (soundsByName == null)
            buildSoundByName();
        return soundsByName;
    }
}
