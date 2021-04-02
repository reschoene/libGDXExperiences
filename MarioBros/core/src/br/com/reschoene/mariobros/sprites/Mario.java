package br.com.reschoene.mariobros.sprites;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.audio.AudioManager;
import br.com.reschoene.mariobros.collison.FixtureFilterBits;
import br.com.reschoene.mariobros.scenes.Hud;
import br.com.reschoene.mariobros.screens.LevelScreen;
import br.com.reschoene.mariobros.sprites.enemies.Enemy;
import br.com.reschoene.mariobros.sprites.enemies.Turtle;
import br.com.reschoene.mariobros.sprites.items.FirePower;
import br.com.reschoene.mariobros.util.GameState;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

import static br.com.reschoene.mariobros.collison.FixtureFilterBits.*;

public class Mario extends Sprite {
    private boolean timeToDefineBigMario;
    private boolean timeToRedefineMario;
    private boolean timeToExitRight;

    public enum State {FALLING, JUMPING, STANDING, RUNNING, GROWING, FIRING, DEAD}

    public State currentState;
    public State previousState;
    private World world;
    private LevelScreen screen;
    public Body b2Body;
    private TextureRegion marioStand;
    private TextureRegion marioDead;
    private Animation marioRun;
    private TextureRegion marioJump;
    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private TextureRegion fireMarioStand;
    private TextureRegion fireMarioJump;
    private Animation bigMarioRun;
    private Animation fireMarioRun;
    private Animation growMario;
    private Animation firingMario;
    private boolean enabledControls = true;

    private float stateTimer;
    private boolean runningRight;
    private boolean runGrowAnimation;
    private boolean runFireAnimation;
    private boolean marioIsDead;

    private FirePower firePower;

    public Mario(LevelScreen screen) {
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

        for (int i = 3; i < 5; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("mariofire"), (i * 18), 0, 18, 32));
        fireMarioRun = new Animation(0.1f, frames);

        frames.clear();

        //set animation for growing mario
        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        }
        growMario = new Animation(0.2f, frames);

        frames.clear();

        //set animation for transforming mario in fire mode
        for (int i = 0; i < 3; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
            frames.add(new TextureRegion(screen.getAtlas().findRegion("mariofire"), 0, 0, 18, 32));
        }

        firingMario = new Animation(0.2f, frames);

        marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);
        bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80, 0, 16, 32);
        fireMarioJump = new TextureRegion(screen.getAtlas().findRegion("mariofire"), 108, 0, 18, 32);

        marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 0, 0, 16, 16);
        bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32);
        fireMarioStand = new TextureRegion(screen.getAtlas().findRegion("mariofire"), 0, 0, 18, 32);

        marioDead = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96, 0, 16, 16);
        //defineMario(new Vector2(32 / MarioGame.PPM, 32 / MarioGame.PPM));

        //debug position (perto bandeira)
        //defineMario(new Vector2(3300 / MarioGame.PPM, 32 / MarioGame.PPM));

        //debug position (terceira fase meio moedas)
        defineMario(new Vector2(2960 / MarioGame.PPM, 200 / MarioGame.PPM));

        setBounds(0, 0, 16 / MarioGame.PPM, 16 / MarioGame.PPM);
        setRegion(marioStand);

        if (GameState.isBig) {
            timeToDefineBigMario = true;
            setBounds(getX(), getY(), getWidth(), getHeight() * 2);
        }

        firePower = new FirePower(this, screen);
        firePower.setActive(GameState.hasFirePower);
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
                ENEMY_BIT, OBJECT_BIT, ENEMY_HEAD_BIT, ITEM_BIT, FIREBALL_BIT);

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

        timeToDefineBigMario = false;
        timeToRedefineMario = false;
    }

    private void shrinkMario() {
        Vector2 currentPosition = b2Body.getPosition();
        world.destroyBody(b2Body);
        b2Body = null;

        defineMario(currentPosition);

        firePower.setActive(true);
    }

    private void growMario() {
        Vector2 currentPosition = b2Body.getPosition();
        //destroy litte body for creating the big mario body
        world.destroyBody(b2Body);
        b2Body = null;

        defineMario(currentPosition.add(0, 10 / MarioGame.PPM));
    }

    public void update(float dt) {
        if (GameState.isBig)
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
        if (timeToExitRight) {
            if (b2Body.getLinearVelocity().y == 0)
                b2Body.setLinearVelocity(1f, 0.0f);
        }

        firePower.update(dt);
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
            case FIRING:
                region = (TextureRegion) firingMario.getKeyFrame(stateTimer);
                if (firingMario.isAnimationFinished(stateTimer))
                    runFireAnimation = false;
                break;
            case JUMPING:
                region = GameState.isBig ? (firePower.isActive() ? fireMarioJump : bigMarioJump) : marioJump;
                break;
            case RUNNING:
                region = GameState.isBig ?
                        (firePower.isActive() ? (TextureRegion) fireMarioRun.getKeyFrame(stateTimer, true) : (TextureRegion) bigMarioRun.getKeyFrame(stateTimer, true)) :
                        (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
                ;
                break;
            case FALLING:
            case STANDING:
            default:
                region = GameState.isBig ? (firePower.isActive() ? fireMarioStand : bigMarioStand) : marioStand;
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
        else if (runFireAnimation)
            return State.FIRING;
        else if (b2Body.getLinearVelocity().y > 0 || (b2Body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if (b2Body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if (b2Body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public static int getLives() {
        return GameState.lives;
    }

    public void grow() {
        if (!GameState.isBig) {
            runGrowAnimation = true;
            GameState.isBig = true;
            timeToDefineBigMario = true;
            setBounds(getX(), getY(), getWidth(), getHeight() * 2);
            Hud.addScore(500);
            AudioManager.getSoundByName("powerUp").play();
        } else {
            AudioManager.playSound("coin");
            Hud.addScore(500);
        }
    }

    public boolean isBig() {
        return GameState.isBig;
    }

    public void hit(Enemy enemy) {
        if (enemy instanceof Turtle && ((Turtle) enemy).getCurrentState() == Turtle.State.STANDING_SHELL) {
            ((Turtle) enemy).kick(this.getX() <= enemy.getX() ? Turtle.KICK_RIGHT_SPEED : Turtle.KICK_LEFT_SPEED);
        } else {
            sufferDamage();
        }
    }

    private void sufferDamage() {
        if (GameState.isBig) {
            GameState.isBig = false;
            timeToRedefineMario = true;
            setBounds(getX(), getY(), getWidth(), getHeight() / 2);
            AudioManager.getSoundByName("powerDown").play();
        } else {
            killMario(true);
        }
    }

    public void onFireBallHit(){
        sufferDamage();
    }

    public void killMario(boolean animate) {
        if (!marioIsDead) {
            marioIsDead = true;

            stateTimer = 0;
            GameState.lives--;
            if (GameState.lives > 0)
                screen.showLiveScreen();

            AudioManager.getCurrentMusic().stop();
            AudioManager.getSoundByName("marioDie").play();

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

    public void fire() {
        if (enabledControls)
            firePower.fire(b2Body.getPosition(), runningRight);
    }

    public void activateFirePower() {
        if (!firePower.isActive()) {
            if (GameState.isBig) {
                AudioManager.getSoundByName("powerUp").play();
                runFireAnimation = true;
                firePower.setActive(true);
            } else {
                grow();
            }
        }else{
            AudioManager.playSound("coin");
            Hud.addScore(500);
        }
    }

    public void jump() {
        if (enabledControls)
            if (b2Body.getLinearVelocity().y == 0 && stateTimer > 0.01) { //0.01 just to not stuck with head on block
                b2Body.applyLinearImpulse(new Vector2(0, 4f), b2Body.getWorldCenter(), true);
                AudioManager.getSoundByName("jump").play();
            }
    }

    public void moveRight() {
        if (enabledControls)
            if (b2Body.getLinearVelocity().x <= 2)
                b2Body.applyLinearImpulse(new Vector2(0.1f, 0), b2Body.getWorldCenter(), true);
    }

    public void moveLeft() {
        if (enabledControls)
            if (b2Body.getLinearVelocity().x >= -2)
                b2Body.applyLinearImpulse(new Vector2(-0.1f, 0), b2Body.getWorldCenter(), true);
    }

    public LevelScreen getScreen() {
        return screen;
    }

    public void onFoundPrincess() {
        b2Body.setLinearVelocity(0f, 0.0f);
        timeToExitRight = false;
        Hud.displayCongratMessage();
    }

    public void animateExitRight() {
        Hud.setActive(false);
        enabledControls = false;
        timeToExitRight = true;
        b2Body.setLinearVelocity(0, 0);
        AudioManager.getCurrentMusic().stop();
        Music music = AudioManager.getMusicByMapName("stageClear");
        music.setLooping(false);
        music.play();
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);

        firePower.draw(batch);
    }
}
