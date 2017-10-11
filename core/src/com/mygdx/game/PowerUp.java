package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class PowerUp {
    enum Type {
        MEDKIT(0), BULLET(1), BLASTER(2), ROCKET(3), BOMB(4);

        private int number;

        public int getNumber() {
            return number;
        }

        Type(int number) {
            this.number = number;
        }
    }

    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private float time;
    private float maxTime;
    private Type type;

    public Type getType() {
        return type;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isActive() {
        return active;
    }

    public PowerUp() {
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.active = false;
        this.time = 0.0f;
        this.maxTime = 5.0f;
    }

    public void update(float dt) {
        time += dt;
        position.mulAdd(velocity, dt);
        if(time > maxTime) {
            deactivate();
        }
    }

    public void activate(float x, float y, Type type) {
        this.position.set(x, y);
        this.velocity.set(MathUtils.random(-50.0f, 50.0f), MathUtils.random(-50.0f, 50.0f));
        this.type = type;
        this.time = 0.0f;
        this.maxTime = 3.0f;
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void use(Player hero) {
        switch (type) {
            case MEDKIT:
                hero.fullRepair();
                break;
            case BULLET:
                hero.changeWeapon(WeaponType.BULLET);
                break;
            case ROCKET:
                hero.changeWeapon(WeaponType.ROCKET);
                break;
            case BLASTER:
                hero.changeWeapon(WeaponType.BLASTER);
                break;
            case BOMB:
//
        }
    }
}
