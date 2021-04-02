package br.com.reschoene.mariobros.collison;

import br.com.reschoene.mariobros.sprites.tileObjects.*;
import br.com.reschoene.mariobros.screens.LevelScreen;
import br.com.reschoene.mariobros.sprites.enemies.Enemy;
import br.com.reschoene.mariobros.sprites.items.Item;
import br.com.reschoene.mariobros.sprites.Mario;
import br.com.reschoene.mariobros.util.GameState;
import com.badlogic.gdx.maps.MapProperties;
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
        else if ((cDef == combine( MARIO_BIT, COIN_BIT)) || (cDef == combine( MARIO_HEAD_BIT, COIN_BIT))){
            objs = getObjsByFilterType(fixA, fixB, COIN_BIT);
            if ((objs.get(0) instanceof Hittable) && (cDef == combine( MARIO_BIT, COIN_BIT)))
                ((Hittable) objs.get(0)).onHit((Mario) objs.get(1));
            else if ((objs.get(0) instanceof  HeadHittable) && (cDef == combine( MARIO_HEAD_BIT, COIN_BIT)))
                ((HeadHittable) objs.get(0)).onHeadHit((Mario) objs.get(1));
        }
        else if (cDef == combine(MARIO_BIT, ENEMY_HEAD_BIT)){
            objs = getObjsByFilterType(fixA, fixB, ENEMY_HEAD_BIT);
            ((HeadHittable) objs.get(0)).onHeadHit((Mario) objs.get(1));
        }
        else if ((cDef == combine(ENEMY_BIT, OBJECT_BIT))
                || (cDef == combine(ENEMY_BIT, BLOCK_BIT))
                || (cDef == combine(ENEMY_BIT, MARIO_BIT))){
            objs = getObjsByFilterType(fixA, fixB, ENEMY_BIT);
            ((Enemy) objs.get(0)).reverseVelocity(true, false);

            if(objs.get(1) instanceof Mario)
                ((Mario) objs.get(1)).hit((Enemy) objs.get(0));
        }
        else if (cDef == combine(ENEMY_BIT, ENEMY_BIT)){
            ((Enemy)fixA.getUserData()).onEnemyHit((Enemy)fixB.getUserData());
            ((Enemy)fixB.getUserData()).onEnemyHit((Enemy)fixA.getUserData());
        }
        else if (cDef == combine(MARIO_BIT, ENEMY_BIT)){
            objs = getObjsByFilterType(fixA, fixB, MARIO_BIT);
            ((Mario) objs.get(0)).hit((Enemy) objs.get(1));
        }
        else if (cDef == combine(FIREBALL_BIT, ENEMY_BIT)){
            objs = getObjsByFilterType(fixA, fixB, ENEMY_BIT);
            ((Enemy) objs.get(0)).onFireBallHit();
        }
        else if (cDef == combine(FIREBALL_BIT, MARIO_BIT)){
            objs = getObjsByFilterType(fixA, fixB, MARIO_BIT);
            ((Mario) objs.get(0)).onFireBallHit();
        }
        else if ((cDef == combine(ITEM_BIT, OBJECT_BIT)) || (cDef == combine(ITEM_BIT, BLOCK_BIT))) {
            objs = getObjsByFilterType(fixA, fixB, ITEM_BIT);
            ((Item) objs.get(0)).reverseVelocity(true, false);
        }
        else if (cDef == combine(ITEM_BIT, MARIO_BIT)){
            objs = getObjsByFilterType(fixA, fixB, ITEM_BIT);
            ((Item) objs.get(0)).use((Mario) objs.get(1));
        }
        else if (cDef == combine(OBJECT_BIT, MARIO_BIT)){
            objs = getObjsByFilterType(fixA, fixB, OBJECT_BIT);

            if (objs.get(0) instanceof Pipe) {
                MapProperties properties = ((Pipe) objs.get(0)).getMapProperties();
                if (properties.containsKey("GoToPhase")) {
                    LevelScreen screen = ((Mario) objs.get(1)).getScreen();
                    GameState.currentMapFileName = properties.get("GoToPhase", String.class);
                    GameState.currentWorld = properties.get("world", Integer.class);
                    GameState.currentPhase = properties.get("phase", Integer.class);
                    screen.changeMap();
                }
            }
        }
        else if (cDef == combine(MARIO_BIT, BLOCK_BIT)){
            objs = getObjsByFilterType(fixA, fixB, BLOCK_BIT);

            if (objs.get(0) instanceof FlagPole) {
                FlagPole flagPole = (FlagPole) objs.get(0);
                flagPole.flag.goesUp();

                ((Mario)objs.get(1)).animateExitRight();
            }else if (objs.get(0) instanceof Axe){
                Axe axe = (Axe) objs.get(0);
                axe.cutBridge();
            }else if (objs.get(0) instanceof Princess){
                Mario mario = (Mario) objs.get(1);
                mario.onFoundPrincess();
            }
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
