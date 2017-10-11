package com.mygdx.game;

public enum WeaponType {

    BULLET(1, 0.05f), ROCKET(3, 0.12f), BLASTER(5, 0.08f);

    private int damage;
    private float fireRate;

    public int getDamage() {
        return damage;
    }

    public float getFireRate() {
        return fireRate;
    }

    WeaponType(int damage, float fireRate) {
        this.damage = damage;
        this.fireRate = fireRate;
    }

}
