package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import static com.mygdx.game.HighGame.SCREEN_HEIGHT;
import static com.mygdx.game.HighGame.SCREEN_WIDTH;

public class UfoEmitter extends ObjectPool<Ufo> {
    private float innerTimer;
    private float generationTime;
    GameScreen game;
    private boolean ufoActivation;

    public UfoEmitter(int size, float generationTime, GameScreen game) {
        super(size);
        this.generationTime = generationTime;
        this.game = game;
        this.ufoActivation = true;
    }

    @Override
    protected Ufo newObject() {
        return new Ufo();
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void setUfoActivation(boolean ufoActivation) {
        this.ufoActivation = ufoActivation;
    }

    public void update(float dt) {
        if (ufoActivation){
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
        Ufo ufo = getActiveElement();
        float x = (float) Math.random() * SCREEN_WIDTH + SCREEN_WIDTH;
        float y = (float) Math.random() * SCREEN_HEIGHT;
        int randomLevel = MathUtils.random(1, 4);
        ufo.activate(x, y, randomLevel, game);
    }
}
