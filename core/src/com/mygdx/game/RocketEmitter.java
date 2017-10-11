package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import static com.mygdx.game.HighGame.SCREEN_HEIGHT;
import static com.mygdx.game.HighGame.SCREEN_WIDTH;

public class RocketEmitter extends ObjectPool<Rocket> {
    private float innerTimer;
    private float generationTime;
    GameScreen game;
    private boolean rocketActivation;

    public RocketEmitter(int size, float generationTime, GameScreen game) {
        super(size);
        this.generationTime = generationTime;
        this.game = game;
        this.rocketActivation = true;
    }

    public void setRocketActivation(boolean rocketActivation) {
        this.rocketActivation = rocketActivation;
    }

    @Override
    protected Rocket newObject() {
        return new Rocket();
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        if (rocketActivation){
            innerTimer += dt;
            if (innerTimer > generationTime) {
                setup();
                innerTimer -= generationTime;
            }
        }

        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
    }

    public void setup() {
        Rocket rocket = getActiveElement();
        float x = (float) Math.random() * SCREEN_WIDTH + SCREEN_WIDTH;
        float y = (float) Math.random() * SCREEN_HEIGHT;
        int randomLevel = MathUtils.random(1, 4);
        rocket.activate(x, y, randomLevel, game);
    }
}
