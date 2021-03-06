package br.com.reschoene.mariobros.collison;

import br.com.reschoene.mariobros.sprites.enemies.Enemy;
import br.com.reschoene.mariobros.sprites.items.Item;
import br.com.reschoene.mariobros.sprites.tileObjects.HeadHittable;
import br.com.reschoene.mariobros.sprites.tileObjects.Mario;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;

import java.util.LinkedList;
import java.util.List;

import static br.com.reschoene.mariobros.collison.FixtureFilterBits.*;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        List<Object> objs = null;

        if (cDef == combine(MARIO_HEAD_BIT, BRICK_BIT)){
            objs = getObjsByFilterType(fixA, fixB, BRICK_BIT);
            ((HeadHittable) objs.get(0)).onHeadHit((Mario) objs.get(1));
        }
        else if (cDef == combine(MARIO_HEAD_BIT, COIN_BIT)){
            objs = getObjsByFilterType(fixA, fixB, COIN_BIT);
            ((HeadHittable) objs.get(0)).onHeadHit((Mario) objs.get(1));
        }
        else if (cDef == combine(MARIO_BIT, ENEMY_HEAD_BIT)){
            objs = getObjsByFilterType(fixA, fixB, ENEMY_HEAD_BIT);
            ((HeadHittable) objs.get(0)).onHeadHit((Mario) objs.get(1));
        }
        else if ((cDef == combine(ENEMY_BIT, OBJECT_BIT)) || (cDef == combine(ENEMY_BIT, BLOCK_BIT))){
            objs = getObjsByFilterType(fixA, fixB, ENEMY_BIT);
            ((Enemy) objs.get(0)).reverseVelocity(true, false);
        }
        else if (cDef == combine(ENEMY_BIT, ENEMY_BIT)){
            ((Enemy)fixA.getUserData()).onEnemyHit((Enemy)fixB.getUserData());
            ((Enemy)fixB.getUserData()).onEnemyHit((Enemy)fixA.getUserData());
        }
        else if (cDef == combine(MARIO_BIT, ENEMY_BIT)){
            objs = getObjsByFilterType(fixA, fixB, MARIO_BIT);
            ((Mario) objs.get(0)).hit((Enemy) objs.get(1));
        }
        else if ((cDef == combine(ITEM_BIT, OBJECT_BIT)) || (cDef == combine(ITEM_BIT, BLOCK_BIT))) {
            objs = getObjsByFilterType(fixA, fixB, ITEM_BIT);
            ((Item) objs.get(0)).reverseVelocity(true, false);
        }
        else if (cDef == combine(ITEM_BIT, MARIO_BIT)){
            objs = getObjsByFilterType(fixA, fixB, ITEM_BIT);
            ((Item) objs.get(0)).use((Mario) objs.get(1));
        }
    }

    /**
     * Given two fixtures, returns a list containing the first element as the fixture
     * with filterBit equals to the given filterBit parameter, and the second element as the other fixture
     */
    private List<Object> getObjsByFilterType(Fixture fixA, Fixture fixB, FixtureFilterBits filterBit){
        List<Object> objs = new LinkedList<>();

        if (fixA.getFilterData().categoryBits == filterBit.getValue()){
            objs.add(fixA.getUserData());
            objs.add(fixB.getUserData());
        }
        else{
            objs.add(fixB.getUserData());
            objs.add(fixA.getUserData());
        }

        return objs;
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
