package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;

import static com.mygdx.game.HighGame.SCREEN_HEIGHT;
import static com.mygdx.game.HighGame.SCREEN_WIDTH;

public class AsteroidEmitter extends ObjectPool<Asteroid> {
    private float generationTime;
    private float innerTimer;
    private AtlasRegion asteroidTexture;
    private boolean asteroidActivation;

    public AsteroidEmitter(int size, float generationTime, AtlasRegion asteroidTexture) {
        super(size);
        this.asteroidTexture = asteroidTexture;
        for (int i = 0; i < size; i++) {
            freeList.add(newObject());
        }
        this.generationTime = generationTime;
        this.innerTimer = 0.0f;
        this.asteroidActivation = true;
    }

    @Override
    protected Asteroid newObject() {
        return new Asteroid(asteroidTexture);
    }

    public void setAsteroidActivation(boolean asteroidActivation) {
        this.asteroidActivation = asteroidActivation;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Asteroid a = getActiveList().get(i);
            a.render(batch);
        }
    }

    public void update(float dt) {
        if (asteroidActivation){
            innerTimer += dt;
            if (innerTimer > generationTime){
                setup();
                innerTimer -= generationTime;
            }
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
