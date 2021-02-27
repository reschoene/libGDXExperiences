package br.com.reschoene.mariobros.collison;

import br.com.reschoene.mariobros.sprites.enemies.Enemy;
import br.com.reschoene.mariobros.sprites.items.Item;
import br.com.reschoene.mariobros.sprites.tileObjects.HeadHittable;
import br.com.reschoene.mariobros.sprites.tileObjects.Mario;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;

import static br.com.reschoene.mariobros.collison.FixtureFilterBits.*;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if (cDef == combine(MARIO_HEAD, BRICK_BIT)){
            Object obj = getObjByFilterType(fixA, fixB, BRICK_BIT);
            ((HeadHittable) obj).onHeadHit();
        }
        else if (cDef == combine(MARIO_HEAD, COIN_BIT)){
            Object obj = getObjByFilterType(fixA, fixB, COIN_BIT);
            ((HeadHittable) obj).onHeadHit();
        }
        else if (cDef == combine(MARIO_BIT, ENEMY_HEAD_BIT)){
            Object obj = getObjByFilterType(fixA, fixB, ENEMY_HEAD_BIT);
            ((Enemy) obj).onHeadHit();
        }
        else if ((cDef == combine(ENEMY_BIT, OBJECT_BIT)) || (cDef == combine(ENEMY_BIT, BLOCK_BIT))){
            Object obj = getObjByFilterType(fixA, fixB, ENEMY_BIT);
            ((Enemy) obj).reverseVelocity(true, false);
        }
        else if (cDef == combine(ENEMY_BIT, ENEMY_BIT)){
            ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
            ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
        }
        else if (cDef == combine(MARIO_BIT, ENEMY_BIT))
            Gdx.app.log("Mario", "Died");
        else if ((cDef == combine(ITEM_BIT, OBJECT_BIT)) || (cDef == combine(ITEM_BIT, BLOCK_BIT))) {
            Object obj = getObjByFilterType(fixA, fixB, ITEM_BIT);
            ((Item) obj).reverseVelocity(true, false);
        }
        else if (cDef == combine(ITEM_BIT, MARIO_BIT)){
            if (fixA.getFilterData().categoryBits == ITEM_BIT.getValue())
                ((Item) fixA.getUserData()).use((Mario) fixB.getUserData());
            else
                ((Item) fixB.getUserData()).use((Mario) fixA.getUserData());
        }
    }

    private Object getObjByFilterType(Fixture fixA, Fixture fixB, FixtureFilterBits filterBit){
        Object obj;
        if (fixA.getFilterData().categoryBits == filterBit.getValue())
            obj = fixA.getUserData();
        else
            obj = fixB.getUserData();

        return obj;
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
