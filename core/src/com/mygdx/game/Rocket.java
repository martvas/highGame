package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Rocket implements PoolableMy {
    private static final float SIZE = 64.0f;
    private static final float HALF_SIZE = SIZE / 2;

    private Vector2 position;
    private Vector2 velocity;

    private int level;
    private int hp;
    private int maxHp;
    private float reddish;
    private float scale;
    private boolean active;
    private Circle hitArea;


    public Rocket(){
        position = new Vector2(0.0f, 0.0f);
        velocity = new Vector2(0.0f, 0.0f);
        hp = 0;
        maxHp = 0;
        reddish = 0.0f;
        hitArea = new Circle();
        active = false;
        level = 0;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void render(SpriteBatch batch, AtlasRegion enemyTexture){
        if (reddish > 0.01){
            batch.setColor(1.0f, 1.0f- reddish, 1.0f - reddish, 1.0f);
        }
        batch.draw(enemyTexture, position.x - HALF_SIZE, position.y - HALF_SIZE, HALF_SIZE, HALF_SIZE, SIZE,
                SIZE, scale, scale, 0);

        if (reddish > 0.1){
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    public void update(float dt){
        position.mulAdd(velocity, dt);
        hitArea.setPosition(position);

        reddish -= 2 * dt;
        if (reddish <= 0) reddish = 0;

        if (position.x < HALF_SIZE){
            deactivate();
        }
        if (position.y > HighGame.SCREEN_HEIGHT - HALF_SIZE){
            position.y = HighGame.SCREEN_HEIGHT - HALF_SIZE;
            if (velocity.y > 0.0f ) velocity.y = 0.0f;
        }
        if (position.y < HALF_SIZE){
            position.y = HALF_SIZE;
            if (velocity.y < 0.0f) velocity.y = 0.0f;
        }
    }

    public void deactivate(){
        active = false;
    }


    public void activate(float x, float y, int level){
        position.set(x, y);
        this.level = level;
        velocity.x = -360 - (20 * level);
        maxHp = 2 * level;
        hp = maxHp;
        reddish = 0.0f;
        scale = 1.0f + (0.1f * level);
        hitArea = new Circle(position.x, position.y, 28 * scale);
        active = true;
    }

    public boolean takeDamage(int damage){
        maxHp -= damage;
        reddish += 0.2;
        if (reddish >= 1.0f) reddish = 1.0f;
        if (maxHp <= 0) {
            deactivate();
            return true;
        }
        return false;
    }
}
