package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import static com.mygdx.game.HighGame.SCREEN_HEIGHT;
import static com.mygdx.game.HighGame.SCREEN_WIDTH;

public class RocketEmitter extends ObjectPool<Rocket> {
    private AtlasRegion rocketTexture;

    private float innerTimer;
    private float generationTime;
    public RocketEmitter(int size, float generationTime, TextureAtlas atlas){
        super(size);
        this.rocketTexture = atlas.findRegion("enemyRocket");
        this.innerTimer = 0.0f;
        this.generationTime = generationTime;
    }

    @Override
    protected Rocket newObject() {
        return new Rocket();
    }

    public void render(SpriteBatch batch){
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt){
        innerTimer += dt;
        if(innerTimer > generationTime){
            setup();
            innerTimer -= generationTime;
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
    }

    public void setup(){
        Rocket rocket = getActiveElement();
        float x = (float) Math.random() * SCREEN_WIDTH + SCREEN_WIDTH;
        float y = (float) Math.random() * SCREEN_HEIGHT;
        int randomLevel = MathUtils.random(1, 4);

        rocket.activate(x, y, randomLevel, rocketTexture);
    }
}
