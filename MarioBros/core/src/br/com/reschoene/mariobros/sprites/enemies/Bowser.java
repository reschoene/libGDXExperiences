package br.com.reschoene.mariobros.sprites.enemies;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.screens.GameAtlas;
import br.com.reschoene.mariobros.screens.PlayScreen;
import br.com.reschoene.mariobros.sprites.Mario;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

import static br.com.reschoene.mariobros.collison.FixtureFilterBits.*;

public class Bowser extends DestroyableEnemy {
    private Animation runningAnimation;
    private TextureRegion standingTexture;

    private State currentState;
    private State previousState;

    public enum State {STANDING, RUNNING}

    private List<Vector2> movements;
    private int iMov = 0;

    public Bowser(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setBounds(getX(), getY(), 35 / MarioGame.PPM, 32 / MarioGame.PPM);
        loadAnimations();
        b2Body.setActive(true);
        loadMovements();
    }

    private void loadMovements() {
        movements = new ArrayList<>();
        movements.add(new Vector2(-2, 0));
        movements.add(new Vector2(2, 0));
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX()+(35/2)/MarioGame.PPM, getY()+(32/2)/MarioGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(14 / MarioGame.PPM);

        //categoryBits defines whats fixture is
        //maskBits defines whats this fixture collides with
        fdef.filter.categoryBits = BOWSER_BIT.getValue();
        fdef.filter.maskBits = combine(GROUND_BIT, BLOCK_BIT, COIN_BIT, BRICK_BIT,
                ENEMY_BIT, OBJECT_BIT, ENEMY_HEAD_BIT, ITEM_BIT, MARIO_BIT);

        fdef.shape = shape;
        b2Body.createFixture(fdef).setUserData(this);
    }

    private void loadAnimations() {
        currentState = State.STANDING;
        previousState = State.STANDING;

        Array<TextureRegion> frames = new Array<>();

        for (int i = 0; i < 3; i++)
            frames.add(new TextureRegion(GameAtlas.getAtlas().findRegion("bowser"), (i*35), 0, 35, 32));
        runningAnimation = new Animation(0.1f, frames);

        standingTexture = new TextureRegion(GameAtlas.getAtlas().findRegion("bowser"), 35*4, 0, 35, 32);
    }

    public void update(float delta) {
        setRegion(getFrame(delta));
        setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight()/2);

        if (b2Body.getLinearVelocity().x == 0){
            iMov = iMov % movements.size();
            b2Body.applyLinearImpulse(movements.get(iMov), b2Body.getWorldCenter(), true);
            iMov++;
        }
    }

    @Override
    public float getDefaultXVelocity() {
        return 0;
    }

    @Override
    public void onFireBallHit() {

    }

    @Override
    public void onEnemyHit(Enemy enemy) {
        //
    }

    @Override
    public void onHeadHit(Mario mario) {
        //
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case STANDING:
                region = standingTexture;
                break;
            case RUNNING:
                region = (TextureRegion) runningAnimation.getKeyFrame(stateTime, true);
                break;
            default:
                region = standingTexture;
                break;
        }

        stateTime = currentState == previousState ? stateTime + dt : 0;
        previousState = currentState;

        return region;
    }

    public State getState() {
        if (Math.abs(b2Body.getLinearVelocity().x) == 0)
            return State.STANDING;
        else
            return State.RUNNING;
    }
}
