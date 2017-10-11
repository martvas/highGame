package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Rocket extends Ship implements PoolableMy {

    private static final float SIZE = 64.0f;
    private static final float HALF_SIZE = SIZE / 2;

    private int level;
    private float scale;
    private AtlasRegion enemyTexture;

    private float innerTimerToMove;
    private float randomEnemyMoveTime;
    private int randomMove;

    public int getLevel() {
        return level;
    }

    public Rocket() {
        position = new Vector2(0.0f, 0.0f);
        velocity = new Vector2(0.0f, 0.0f);
        hp = 0;
        maxHp = 0;
        reddish = 0.0f;
        hitArea = new Circle();
        level = 0;
        this.weaponDirection = new Vector2(-1.0f, 0.0f);

        innerTimerToMove = 0;
        randomMove = 0;
        isPlayer = false;
        active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (reddish > 0.01) {
            batch.setColor(1.0f, 1.0f - reddish, 1.0f - reddish, 1.0f);
        }

        batch.draw(enemyTexture, position.x - HALF_SIZE, position.y - HALF_SIZE, HALF_SIZE, HALF_SIZE, SIZE,
                SIZE, scale, scale, 0);

        if (reddish > 0.1) {
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        hitArea.setPosition(position);

        reddish -= 2 * dt;
        if (reddish <= 0) reddish = 0;

        if (position.x < -100) {
            deactivate();
        }
        if (position.y > HighGame.SCREEN_HEIGHT - HALF_SIZE) {
            position.y = HighGame.SCREEN_HEIGHT - HALF_SIZE;
            if (velocity.y > 0.0f){
                velocity.y = 0.0f;
            }
                velocity.y *= -1;
        }
        if (position.y < HALF_SIZE) {
            position.y = HALF_SIZE;
            if (velocity.y < 0.0f) {
                velocity.y = 0.0f;
            }
        }
    }

    @Override
    public void onDestroy() {
        deactivate();
    }

    public void activate(float x, float y, int level, GameScreen game) {
        this.enemyTexture = game.getAtlas().findRegion("enemyRocket");
        velocity.x = -360 - (20 * level);
        position.set(x, y);
        this.level = level;
        maxHp = 3 * level;
        hp = maxHp;
        reddish = 0.0f;
        scale = 1.0f + (0.1f * level);
        hitArea = new Circle(position.x, position.y, 28 * scale);

        this.game = game;
        active = true;
    }
}
