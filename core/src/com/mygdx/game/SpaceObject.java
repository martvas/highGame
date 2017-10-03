package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public abstract class SpaceObject {
    protected HighGame game;
    protected Vector2 position;
    protected Vector2 velocity;
    protected int hp;
    protected int maxHp;
    protected Circle hitArea;
    protected float reddish;
    protected boolean active;

    public Vector2 getPosition() {
        return position;
    }
    public Vector2 getVelocity() {
        return velocity;
    }
    public Circle getHitArea() {
        return hitArea;
    }

    public void deactivate() {
        this.active = false;
    }

    public boolean isActive(){
        return active;
    }
    public abstract void render(SpriteBatch batch);

    public abstract void update(float dt);

    public abstract void onDestroy();

    public boolean takeDamage(int dmg) {
        hp -= dmg;
        reddish += 0.2f;
        if (hp <= 0) {
            deactivate();
        }
        if (reddish > 1.0f) reddish = 1.0f;
        return hp <= 0;
    }

}
