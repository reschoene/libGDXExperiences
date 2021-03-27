package br.com.reschoene.mariobros.util;

public class GameState {
    public static int lives = 3;
    public static int score = 0;
    public static boolean isBig = false;
    public static boolean hasFirePower = false;
    public static int coins = 0;

    public static void reset() {
        lives = 3;
        score = 0;
        coins = 0;
        isBig = false;
        hasFirePower = false;
    }
}
