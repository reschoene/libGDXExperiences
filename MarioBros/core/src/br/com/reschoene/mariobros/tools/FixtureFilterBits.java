package br.com.reschoene.mariobros.tools;

public enum FixtureFilterBits {
    GROUND_BIT(1),
    MARIO_BIT(2),
    BRICK_BIT(4),
    COIN_BIT(8),
    DESTROYED_BIT(16),
    OBJECT_BIT(32),
    ENEMY_BIT(64),
    ENEMY_HEAD_BIT(128);

    private final short value;

    FixtureFilterBits(int value){
        this.value = (short)value;
    }

    public short getValue(){
        return value;
    }

    public static short combine(FixtureFilterBits... filterBits){
        short res = 0;
        for(FixtureFilterBits v: filterBits)
            res |= v.getValue();
        return res;
    }
}
