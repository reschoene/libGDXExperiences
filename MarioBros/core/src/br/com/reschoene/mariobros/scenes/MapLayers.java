package br.com.reschoene.mariobros.scenes;

public enum MapLayers {
    BACKGROUND(0),
    GRAPHIC(1),
    GROUND(2),
    PIPE(3),
    COIN(4),
    BRICK(5),
    BLOCK(6),
    GOOMBA(7),
    TURTLE(8);

    private final int idx;

    MapLayers(int idx) {
        this.idx = idx;
    }

    public int getIdx() {
        return this.idx;
    }
}
