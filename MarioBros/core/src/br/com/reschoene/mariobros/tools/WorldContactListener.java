package br.com.reschoene.mariobros.tools;

import br.com.reschoene.mariobros.sprites.HeadHittable;
import com.badlogic.gdx.physics.box2d.*;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if ("head".equals(fixA.getUserData()) || "head".equals(fixB.getUserData())){
            Fixture head = "head".equals(fixA.getUserData()) ? fixA : fixB;
            Fixture object = head.equals(fixA)? fixB : fixA;

            if (object.getUserData() != null && object.getUserData() instanceof HeadHittable){
                ((HeadHittable) object.getUserData()).onHeadHit();
            }
        }
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
