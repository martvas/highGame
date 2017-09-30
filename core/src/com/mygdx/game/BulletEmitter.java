package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class BulletEmitter extends ObjectPool<Bullet> {
    AtlasRegion bulletTexture;
    public BulletEmitter(AtlasRegion bulletTexture, int size){
        super(size);
        this.bulletTexture = bulletTexture;
    }

    @Override
    public Bullet newObject() {
        return new Bullet();
    }

    public void render(SpriteBatch batch){
        for (int i = 0; i < activeList.size(); i++) {
            batch.draw(bulletTexture, activeList.get(i).getPosition().x - 8, activeList.get(i).getPosition().y - 8);
        }
    }

    public void update(float dt){
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
    }

    public void setup(float x, float y, float vx, float vy){
        Bullet b = getActiveElement();
        b.activate(x, y, vx, vy);
    }

}
