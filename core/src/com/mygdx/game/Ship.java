package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

public abstract class Ship extends SpaceObject {
    protected float enginePower;

    protected float currentFire;
    protected float fireRate;

    protected Vector2 weaponDirection;
    protected boolean isPlayer;

    protected TextureAtlas.AtlasRegion bulletTexture;

    public void pressFire(float dt) {
        currentFire += dt;
        if (currentFire > fireRate) {
            System.out.println(isPlayer + "ataka");
            currentFire -= fireRate;
            fire();
        }
    }

    public void fire() {
        game.getBulletEmitter().setup(isPlayer,position.x + 24.0f, position.y + 0.0f, weaponDirection.x * 640, weaponDirection.y * 640);
    }
}
