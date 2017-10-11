package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;


public class BossEmitter {
    private Boss boss;
    private float innerTimer;

    public BossEmitter(AtlasRegion bossTexture, AtlasRegion textureHP, GameScreen game, AsteroidEmitter asteroidEmitter, UfoEmitter ufoEmitter, RocketEmitter rocketEmitter){
        this.boss = new Boss(bossTexture, game, textureHP, asteroidEmitter, ufoEmitter, rocketEmitter);
    }

    public Boss getBoss() {
        return boss;
    }

    public void update(float dt) {
        if (!boss.isActive()){
            innerTimer += dt;
        }
        if (innerTimer > 30.0f){
            setup();
            innerTimer = 0.0f;
        }
        if (boss.isActive()){
            boss.update(dt);
        }
    }

    public void render(SpriteBatch batch) {
        if (boss.isActive()){
            boss.render(batch);
            boss.renderHUD(batch, 1036, 668);
        }
    }

    public void setup() {
        if (!boss.isActive()){
            boss.activate();
        }
    }
}
