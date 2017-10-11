package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class BulletEmitter extends ObjectPool<Bullet> {
      TextureAtlas atlas;
    public BulletEmitter(TextureAtlas atlas, int size){
        super(size);
        this.atlas = atlas;
    }

    @Override
    public Bullet newObject() {
        return new Bullet();
    }

    public void render(SpriteBatch batch){
        for (int i = 0; i < activeList.size(); i++) {
            batch.draw(activeList.get(i).getBulletTexture(),activeList.get(i).getPosition().x - 16, activeList.get(i).getPosition().y - 16);
        }
    }

    public void update(float dt){
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
    }

    public void setup(boolean isPlayerBullet, WeaponType weaponType, float x, float y){
        Bullet b = getActiveElement();
        b.activate(isPlayerBullet, weaponType, atlas, x, y);
    }

}
