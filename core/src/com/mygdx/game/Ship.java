package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

public abstract class Ship extends SpaceObject {
    protected float enginePower;

    protected float currentFire;
    protected float fireRate;

    protected Vector2 weaponDirection;
    protected boolean isPlayer;

    public void pressFire(float dt, WeaponType weaponType) {
        currentFire += dt;
        if (currentFire > fireRate + weaponType.getFireRate()) {
            currentFire -= fireRate + weaponType.getFireRate();
            fire(weaponType);
        }
    }

    public void fire(WeaponType weaponType) {
        game.getBulletEmitter().setup(isPlayer, weaponType,position.x + 24.0f, position.y + 0.0f);
    }
}
