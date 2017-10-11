package com.mygdx.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

public class Bullet implements PoolableMy {
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private boolean isPlayerBullet;
    private TextureAtlas.AtlasRegion bulletTexture;
    private WeaponType weaponType;
    private Sound fireSound;

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public TextureAtlas.AtlasRegion getBulletTexture() {
        return bulletTexture;
    }

    public boolean isPlayerBullet() {
        return isPlayerBullet;
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

    public void activate(boolean isPlayerBullet, WeaponType weaponType, TextureAtlas atlas, float x, float y/*, float vx, float vy*/){
        position.set(x, y);
        /*velocity.set(vx, vy);*/
        this.isPlayerBullet = isPlayerBullet;
        this.weaponType = weaponType;
        if (isPlayerBullet) {
            switch (weaponType){
                case BULLET:
                    bulletTexture = atlas.findRegion("bullet");
                    velocity.set(600, 0);
                    break;
                case ROCKET:
                    bulletTexture = atlas.findRegion("rocketW");
                    velocity.set(400, 0);
                    break;
                case BLASTER:
                    bulletTexture = atlas.findRegion("blasterR");
                    velocity.set(500, 0);
                    break;
            }
        } else {
            switch (weaponType){
                case BULLET:
                    bulletTexture = atlas.findRegion("bulletOther");
                    velocity.set(-500, 0);
                    break;
                case ROCKET:
                    bulletTexture = atlas.findRegion("rocketWother");
                    velocity.set(-300, 0);
                    break;
                case BLASTER:
                    bulletTexture = atlas.findRegion("blasterG");
                    velocity.set(-400, 0);
                    break;
            }
        }
        active = true;
    }

    public void update(float dt){
        position.mulAdd(velocity, dt);
        if (position.x > HighGame.SCREEN_WIDTH) deactivate();
        if (position.x < 0) deactivate();
    }
}
