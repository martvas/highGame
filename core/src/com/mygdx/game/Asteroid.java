package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Asteroid implements PoolableMy {
    public static final float MIN_SPEED = 120.0f;
    public static final float MAX_SPEED = 360.0f;
    private static final float SIZE = 64.0f;
    private static final float HALF_SIZE = SIZE / 2;
    private Vector2 position;
    private Vector2 velocity;
    private float scale;
    private float angle;
    private float angularSpeed;
    private int hp;
    private int maxHp;
    private Circle hitArea;
    float reddish;

    private boolean active;

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Asteroid() {
        this.position = new Vector2(0.0f, 0.0f);
        this.velocity = new Vector2(0.0f, 0.0f);
        this.scale = 0.0f;
        this.angle = 0.0f;
        this.angularSpeed = 0.0f;
        this.maxHp = 0;
        this.hp = maxHp;
        this.hitArea = new Circle();
        this.reddish = 0.0f;
        this.active = false;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate(float x, float y, float vx, float vy, int maxHp) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(vx, vy);
        this.scale = MathUtils.random(0.5f, 2.0f);
        this.angle = MathUtils.random(0.0f, 360.0f);
        this.angularSpeed = MathUtils.random(-45.0f, 45.0f);
        this.maxHp = maxHp;
        this.hp = this.maxHp;
        this.hitArea = new Circle(position.x, position.y, 28 * scale);
        this.reddish = 0.0f;
        active = true;
    }

    public void render(SpriteBatch batch, TextureRegion asteroidTexture){
        if (reddish > 0.01f) {
            batch.setColor(1.0f, 1.0f - reddish, 1.0f - reddish, 1.0f);
        }
        batch.draw(asteroidTexture, position.x - HALF_SIZE, position.y - HALF_SIZE, HALF_SIZE, HALF_SIZE, SIZE, SIZE,
                scale, scale, angle, false);
        if (reddish > 0.01f) {
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        angle += angularSpeed * dt;
        if (position.x < -100.0f) {
            deactivate();
        }
        hitArea.setPosition(position);
        reddish -= dt * 2.0f;
        if (reddish < 0.0f) reddish = 0.0f;
    }

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

