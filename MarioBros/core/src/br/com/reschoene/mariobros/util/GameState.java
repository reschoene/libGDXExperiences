package br.com.reschoene.mariobros.util;

public class GameState {
    public static int lives = 3;
    public static int score = 0;
    public static boolean isBig = false;
    public static boolean hasFirePower = false;
    public static int coins = 0;
    public static String currentMapFileName = "map04.tmx";
    public static int currentWorld = 1;
    public static int currentPhase = 4;

    public static void reset() {
        reset(false);
    }

    public static void reset(boolean preservePhase) {
        lives = 3;
        score = 0;
        coins = 0;
        isBig = false;
        hasFirePower = false;

        if(!preservePhase) {
            currentMapFileName = "map01.tmx";
            currentWorld = 1;
            currentPhase = 1;
        }
    }
}
