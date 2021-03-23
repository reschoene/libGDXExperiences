package br.com.reschoene.mariobros.util;

public class GameState {
    public static int lives = 3;
    public static int score = 0;
    public static boolean isBig = false;
    public static boolean hasFirePower = false;

    public static void reset() {
        lives = 3;
        score = 0;
        isBig = false;
        hasFirePower = false;
    }
}
