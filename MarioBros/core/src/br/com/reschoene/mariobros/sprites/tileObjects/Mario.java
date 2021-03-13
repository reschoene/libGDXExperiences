package br.com.reschoene.mariobros.sprites.tileObjects;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.collison.FixtureFilterBits;
import br.com.reschoene.mariobros.scenes.Hud;
import br.com.reschoene.mariobros.screens.PlayScreen;
import br.com.reschoene.mariobros.sprites.enemies.Enemy;
import br.com.reschoene.mariobros.sprites.enemies.Turtle;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import static br.com.reschoene.mariobros.collison.FixtureFilterBits.*;

public class Mario extends Sprite {
    private boolean timeToDefineBigMario;
    private boolean timeToRedefineMario;

    public enum State {FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD}

    public State currentState;
    public State previousState;
    private World world;
    private PlayScreen screen;
    public Body b2Body;
    private TextureRegion marioStand;
    private TextureRegion marioDead;
    private Animation marioRun;
    private TextureRegion marioJump;
    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private Animation bigMarioRun;
    private Animation growMario;

    private float stateTimer;
    private boolean runningRight;
    private boolean marioIsBig;
    private boolean runGrowAnimation;
    private boolean marioIsDead;

    public Mario(PlayScreen screen) {
        this.world = screen.getWorld();
        this.screen = screen;

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<>();
        for (int i = 2; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), (i * 16), 0, 16, 16));
        marioRun = new Animation(0.1f, frames);

        frames.clear();

        for (int i = 2; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), (i * 16), 0, 16, 32));
        bigMarioRun = new Animation(0.1f, frames);

        frames.clear();

        //set animation for growing mario
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        growMario = new Animation(0.2f, frames);

        marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);
        bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80, 0, 16, 32);

        marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 0, 0, 16, 16);
        bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32);

        marioDead = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96, 0, 16, 16);

        defineMario(new Vector2(32 / MarioGame.PPM, 32 / MarioGame.PPM));

        setBounds(0, 0, 16 / MarioGame.PPM, 16 / MarioGame.PPM);
        setRegion(marioStand);
    }

    private void defineMario(Vector2 position) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioGame.PPM);

        //categoryBits defines whats fixture is
        //maskBits defines whats this fixture collides with
        fdef.filter.categoryBits = MARIO_BIT.getValue();
        fdef.filter.maskBits = combine(GROUND_BIT, BLOCK_BIT, COIN_BIT, BRICK_BIT,
                ENEMY_BIT, OBJECT_BIT, ENEMY_HEAD_BIT, ITEM_BIT);

        fdef.shape = shape;
        b2Body.createFixture(fdef).setUserData(this);

        if (timeToDefineBigMario) {
            shape.setPosition(new Vector2(0, -14 / MarioGame.PPM));
            b2Body.createFixture(fdef).setUserData(this);
        }

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioGame.PPM, 6 / MarioGame.PPM), new Vector2(2 / MarioGame.PPM, 6 / MarioGame.PPM));
        fdef.shape = head;
        fdef.filter.categoryBits = MARIO_HEAD_BIT.getValue();
        fdef.isSensor = true;

        b2Body.createFixture(fdef).setUserData(this);
    }

    private void shrinkMario() {
        Vector2 currentPosition = b2Body.getPosition();
        world.destroyBody(b2Body);

        defineMario(currentPosition);

        timeToRedefineMario = false;
    }

    private void growMario() {
        Vector2 currentPosition = b2Body.getPosition();
        //destroy litte body for creating the big mario body
        world.destroyBody(b2Body);

        defineMario(currentPosition.add(0, 10 / MarioGame.PPM));

        timeToDefineBigMario = false;
    }

    public void update(float dt) {
        if (marioIsBig)
            setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2 - 6 / MarioGame.PPM);
        else
            setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);

        if (b2Body.getPosition().y < 0)
            killMario(false);

        setRegion(getFrame(dt));

        if (timeToDefineBigMario)
            growMario();
        if (timeToRedefineMario)
            shrinkMario();
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case DEAD:
                region = marioDead;
                break;
            case GROWING:
                region = (TextureRegion) growMario.getKeyFrame(stateTimer);
                if (growMario.isAnimationFinished(stateTimer))
                    runGrowAnimation = false;
                break;
            case JUMPING:
                region = marioIsBig ? bigMarioJump : marioJump;
                break;
            case RUNNING:
                region = marioIsBig ? (TextureRegion) bigMarioRun.getKeyFrame(stateTimer, true) : (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
                ;
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioIsBig ? bigMarioStand : marioStand;
                break;
        }

        if ((b2Body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((b2Body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if (marioIsDead)
            return State.DEAD;
        else if (runGrowAnimation)
            return State.GROWING;
        else if (b2Body.getLinearVelocity().y > 0 || (b2Body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if (b2Body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if (b2Body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public void grow() {
        if (!marioIsBig) {
            runGrowAnimation = true;
            marioIsBig = true;
            timeToDefineBigMario = true;
            setBounds(getX(), getY(), getWidth(), getHeight() * 2);
            MarioGame.manager.get("audio/sounds/powerup.wav", Sound.class).play();
        } else {
            MarioGame.manager.get("audio/sounds/coin.wav", Sound.class).play();
            Hud.addScore(500);
        }
    }

    public boolean isBig() {
        return marioIsBig;
    }

    public void hit(Enemy enemy) {
        if (enemy instanceof Turtle && ((Turtle) enemy).getCurrentState() == Turtle.State.STANDING_SHELL) {
            ((Turtle) enemy).kick(this.getX() <= enemy.getX() ? Turtle.KICK_RIGHT_SPEED : Turtle.KICK_LEFT_SPEED);
        } else {
            if (marioIsBig) {
                marioIsBig = false;
                timeToRedefineMario = true;
                setBounds(getX(), getY(), getWidth(), getHeight() / 2);
                MarioGame.manager.get("audio/sounds/powerdown.wav", Sound.class).play();
            } else {
                killMario(true);
            }
        }
    }

    private void killMario(boolean animate) {
        if (!marioIsDead) {
            MarioGame.manager.get("audio/music/mario_music.ogg", Music.class).stop();
            MarioGame.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
            marioIsDead = true;
            Filter filter = new Filter();
            filter.maskBits = FixtureFilterBits.NOTHING_BIT.getValue();
            for (Fixture fixture : b2Body.getFixtureList())
                fixture.setFilterData(filter);

            if (animate)
                b2Body.applyLinearImpulse(new Vector2(0, 4f), b2Body.getWorldCenter(), true);
        }
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public void jump() {
        if (b2Body.getLinearVelocity().y == 0 && stateTimer > 0.01) //0.01 just to not stuck with head on block
            b2Body.applyLinearImpulse(new Vector2(0, 4f), b2Body.getWorldCenter(), true);
    }

    public void moveRight(){
        if (b2Body.getLinearVelocity().x <= 2)
            b2Body.applyLinearImpulse(new Vector2(0.1f, 0), b2Body.getWorldCenter(), true);
    }

    public void moveLeft(){
        if (b2Body.getLinearVelocity().x >= -2)
            b2Body.applyLinearImpulse(new Vector2(-0.1f, 0), b2Body.getWorldCenter(), true);
    }

    public PlayScreen getScreen(){
        return screen;
    }
}
