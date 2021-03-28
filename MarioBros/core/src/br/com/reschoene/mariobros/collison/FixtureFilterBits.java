package br.com.reschoene.mariobros.collison;

public enum FixtureFilterBits {
    NOTHING_BIT(0),
    GROUND_BIT(1),
    MARIO_BIT(2),
    BRICK_BIT(4),
    COIN_BIT(8),
    DESTROYED_BIT(16),
    OBJECT_BIT(32),
    ENEMY_BIT(64),
    ENEMY_HEAD_BIT(128),
    BLOCK_BIT(256),
    ITEM_BIT(512),
    MARIO_HEAD_BIT(1024),
    FIREBALL_BIT(2048),
    BOWSER_BIT(4096);

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
