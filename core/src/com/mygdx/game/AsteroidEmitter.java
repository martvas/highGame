package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import static com.mygdx.game.HighGame.SCREEN_HEIGHT;
import static com.mygdx.game.HighGame.SCREEN_WIDTH;

public class AsteroidEmitter extends ObjectPool<Asteroid> {
    private float generationTime;
    private float innerTimer;

    public AsteroidEmitter(int size, float generationTime) {
        super(size);
        for (int i = 0; i < size; i++) {
            freeList.add(newObject());
        }
        this.generationTime = generationTime;
        this.innerTimer = 0.0f;
    }

    @Override
    protected Asteroid newObject() {
        return new Asteroid();
    }

    public void render(SpriteBatch batch, TextureRegion asteroidTexture) {
        for (int i = 0; i < activeList.size(); i++) {
            Asteroid a = getActiveList().get(i);
            a.render(batch, asteroidTexture);
        }
    }

    public void update(float dt) {
        innerTimer += dt;
        if (innerTimer > generationTime){
            setup();
            innerTimer -= generationTime;
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
    }

    public void setup() {
        Asteroid a = getActiveElement();
        float x = (float) Math.random() * SCREEN_WIDTH + SCREEN_WIDTH;
        float y = (float) Math.random() * SCREEN_HEIGHT;
        float vx = -MathUtils.random(Asteroid.MIN_SPEED, Asteroid.MAX_SPEED);
        float vy = 0.0f;
        a.activate(x, y, vx, vy, 10);
    }

}
