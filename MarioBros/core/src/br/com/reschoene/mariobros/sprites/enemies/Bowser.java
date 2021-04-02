package br.com.reschoene.mariobros.sprites.enemies;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.collison.FixtureFilterBits;
import br.com.reschoene.mariobros.screens.GameAtlas;
import br.com.reschoene.mariobros.screens.LevelScreen;
import br.com.reschoene.mariobros.sprites.Mario;
import br.com.reschoene.mariobros.sprites.enemies.action.ActionManager;
import br.com.reschoene.mariobros.sprites.items.FirePower;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import static br.com.reschoene.mariobros.collison.FixtureFilterBits.*;

public class Bowser extends DestroyableEnemy {
    private Animation runningLeftAnim, runningRightAnim;
    private TextureRegion standingTexture;
    private TextureRegion firingTexture;

    private State currentState;
    private State previousState;
    private boolean isDead;
    private boolean firing;

    public enum State {DEAD, STANDING, RUNNING, FIRING}

    private ActionManager actionManager;

    private FirePower firePower;

    private int bowserLife = 100;

    public Bowser(LevelScreen screen, float x, float y) {
        super(screen, x, y);
        setBounds(getX(), getY(), 35 / MarioGame.PPM, 32 / MarioGame.PPM);
        loadAnimations();
        setActions();
        firePower = new FirePower(this, screen);
    }

    private void setActions() {
        actionManager = new ActionManager();
        actionManager.addAction(1f, () -> applyImpulse( 3, 0));

        addFireActions(5, 1f, 0.05f);

        actionManager.addAction(0.5f, () -> applyImpulse(-3, 0));

        addFireActions(5, 2f, 0.05f);

        actionManager.addAction(0.5f, () -> applyImpulse(3, 0));
        actionManager.addAction(2f, () -> applyImpulse(-2, 4));

        addFireActions(5, 1f, 0.05f);

        actionManager.addAction(0.5f, () -> applyImpulse( 0, 4));

        actionManager.addAction(2f, () -> applyImpulse( 1, 4));

        actionManager.addAction(1f, () -> applyImpulse(-2, 0));
        actionManager.addAction(1f, () -> applyImpulse( 0, 4));
        actionManager.addAction(1f, () -> applyImpulse( 3, 0));
        actionManager.addAction(1f, () -> applyImpulse(-3, 0));
        actionManager.addAction(1f, () -> applyImpulse( 0, 4));
    }

    private void addFireActions(int count, float timeFirst, float timeBetween) {
        for(int i=0; i<count; i++) {
            int idx = i;
            actionManager.addAction((idx == 0? timeFirst:timeBetween), () -> fire(idx, count-1));
        }
    }

    private void fire(int idx, int lastIdx) {
        if (idx == 0)
            firing = true;
        else if (idx == lastIdx)
            firing = false;

        firePower.fire(b2Body.getPosition(), false);
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
        fdef.filter.categoryBits = ENEMY_BIT.getValue();
        fdef.filter.maskBits = combine(GROUND_BIT, BLOCK_BIT, COIN_BIT, BRICK_BIT,
                ENEMY_BIT, OBJECT_BIT, ENEMY_HEAD_BIT, ITEM_BIT, MARIO_BIT, FIREBALL_BIT);

        fdef.shape = shape;
        b2Body.createFixture(fdef).setUserData(this);
    }

    private void loadAnimations() {
        currentState = State.STANDING;
        previousState = State.STANDING;

        Array<TextureRegion> frames = new Array<>();

        for (int i = 0; i < 3; i++)
            frames.add(new TextureRegion(GameAtlas.getAtlas().findRegion("bowser"), (i*35), 0, 35, 32));
        runningLeftAnim = new Animation(0.1f, frames);

        frames.clear();

        for (int i = 2; i >= 0; i--)
            frames.add(new TextureRegion(GameAtlas.getAtlas().findRegion("bowser"), (i*35), 0, 35, 32));
        runningRightAnim = new Animation(0.1f, frames);

        standingTexture = new TextureRegion(GameAtlas.getAtlas().findRegion("bowser"), 0, 0, 35, 32);
        firingTexture = new TextureRegion(GameAtlas.getAtlas().findRegion("bowser"), 35*4, 0, 35, 32);
    }

    @Override
    protected void handleFalling(){
        //do nothing, for avoid problems with this method
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        actionManager.setActive(active);
        firePower.setActive(active);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (!destroyed) {
            setRegion(getFrame(delta));
            setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
            actionManager.update(delta);
            firePower.update(delta);

            if (b2Body.getPosition().y < -10) {
                this.screen.getPlayer().animateExitRight();
                setToDestroy = true;
            }
        }
    }

    @Override
    public float getDefaultXVelocity() {
        return 0;
    }

    @Override
    public void onFireBallHit() {
        bowserLife--;

        if(bowserLife <= 0)
            killBowser(true);
    }

    private void killBowser(boolean animate) {
        if (!isDead) {
            isDead = true;
            Filter filter = new Filter();
            filter.maskBits = FixtureFilterBits.NOTHING_BIT.getValue();
            for (Fixture fixture : b2Body.getFixtureList())
                fixture.setFilterData(filter);

            if (animate)
                applyImpulse(0, 10f);
        }
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
            case FIRING:
                region = firingTexture;
                break;
            case RUNNING:
                if(b2Body.getLinearVelocity().x > 0)
                    region = (TextureRegion) runningRightAnim.getKeyFrame(stateTime, true);
                else
                    region = (TextureRegion) runningLeftAnim.getKeyFrame(stateTime, true);
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
        if (isDead)
            return State.DEAD;
        else if (firing)
            return State.FIRING;
        else if (Math.abs(b2Body.getLinearVelocity().x) == 0)
            return State.STANDING;
        else
            return State.RUNNING;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);

        firePower.draw(batch);
    }
}
