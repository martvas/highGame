package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import static com.mygdx.game.HighGame.SCREEN_HEIGHT;
import static com.mygdx.game.HighGame.SCREEN_WIDTH;

public class EnemyUfoEmitter extends ObjectPool<EnemyUfo> {
    private float innerTimer;
    private float generationTime;
    HighGame game;

    public EnemyUfoEmitter(int size, float generationTime, HighGame game){
        super(size);
        this.generationTime = generationTime;
        this.game = game;
    }

    @Override
    protected EnemyUfo newObject() {
        return new EnemyUfo();
    }

    public void render(SpriteBatch batch){
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt){
        innerTimer += dt;
        if(innerTimer > generationTime){
            setup();
            innerTimer -= generationTime;
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
    }

    public void setup(){
        EnemyUfo enemyUfo = getActiveElement();
        float x = (float) Math.random() * SCREEN_WIDTH + SCREEN_WIDTH;
        float y = (float) Math.random() * SCREEN_HEIGHT;
        int randomLevel = MathUtils.random(1, 4);
        enemyUfo.activate(x, y, randomLevel, game);
    }
}
