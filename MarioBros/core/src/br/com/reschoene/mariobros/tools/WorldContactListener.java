package br.com.reschoene.mariobros.tools;

import br.com.reschoene.mariobros.sprites.Enemy;
import br.com.reschoene.mariobros.sprites.Goomba;
import br.com.reschoene.mariobros.sprites.HeadHittable;
import br.com.reschoene.mariobros.sprites.Pipe;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if ("head".equals(fixA.getUserData()) || "head".equals(fixB.getUserData())) {
            Fixture head = "head".equals(fixA.getUserData()) ? fixA : fixB;
            Fixture object = head.equals(fixA) ? fixB : fixA;

            if (object.getUserData() != null && object.getUserData() instanceof HeadHittable) {
                ((HeadHittable) object.getUserData()).onHeadHit();
            }
        }

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if (cDef == FixtureFilterBits.combine(FixtureFilterBits.MARIO_BIT, FixtureFilterBits.ENEMY_HEAD_BIT))
            getEnemyByFixture(fixA, fixB, FixtureFilterBits.ENEMY_HEAD_BIT).onHeadHit();
        else if (cDef == FixtureFilterBits.combine(FixtureFilterBits.ENEMY_BIT, FixtureFilterBits.OBJECT_BIT))
            getEnemyByFixture(fixA, fixB, FixtureFilterBits.ENEMY_BIT).reverseVelocity(true, false);
        else if (cDef == FixtureFilterBits.combine(FixtureFilterBits.MARIO_BIT, FixtureFilterBits.ENEMY_BIT))
            Gdx.app.log("Mario", "Died");
    }

    private Enemy getEnemyByFixture(Fixture fixA, Fixture fixB, FixtureFilterBits enemyFilterBit){
        Enemy enemy;
        if (fixA.getFilterData().categoryBits == enemyFilterBit.getValue())
            enemy = (Enemy) fixA.getUserData();
        else
            enemy = (Enemy) fixB.getUserData();

        return enemy;
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
