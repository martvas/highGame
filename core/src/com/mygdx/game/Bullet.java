package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class Bullet implements PoolableMy {
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void deactivate(){
        this.active = false;
    }

    public Bullet(){
        position = new Vector2(0.0f, 0.0f);
        velocity = new Vector2(0.0f, 0.0f);
        active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void activate(float x, float y, float vx, float vy){
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
    }

    public void update(float dt){
        position.mulAdd(velocity, dt);
        if (position.x > HighGame.SCREEN_WIDTH) deactivate();
        if (position.x < 0) deactivate();
    }
}
