package br.com.reschoene.mariobros.sprites.items;

import br.com.reschoene.mariobros.MarioGame;
import br.com.reschoene.mariobros.screens.LevelScreen;
import br.com.reschoene.mariobros.sprites.Mario;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Item extends Sprite {
    protected LevelScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean setToDestroy;
    protected boolean destroyed;
    protected Body b2Body;

    public Item(LevelScreen screen, float x, float y){
        this(screen, x, y, 16/ MarioGame.PPM, 16/ MarioGame.PPM);
    }

    public Item(LevelScreen screen, float x, float y, float width, float height){
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x, y);
        setBounds(getX(), getY(), width, height);
        defineItem();
    }

    public abstract void defineItem();
    public abstract void use(Mario mario);

    public void update(float dt){
        if(setToDestroy && !destroyed){
            world.destroyBody(b2Body);
            b2Body = null;
            destroyed = true;
            System.out.println("destroida");
        }
    }

    @Override
    public void draw(Batch batch) {
        if(!destroyed)
            super.draw(batch);
    }

    public void destroy(){
        setToDestroy = true;
    }

    public void reverseVelocity(boolean x, boolean y){
        if (x)
            velocity.x = -velocity.x;
        if (y)
            velocity.y = -velocity.y;
    }
}
