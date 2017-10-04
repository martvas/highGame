package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import static com.mygdx.game.HighGame.SCREEN_HEIGHT;
import static com.mygdx.game.HighGame.SCREEN_WIDTH;

public class EnemyEmitter extends ObjectPool<Enemy> {
    private float innerTimerUfo;
    private float ufoGenerationTime;

    private float innerTimerRocket;
    private float rocketGenerationTime;
    GameScreen game;
    Enemy.EnemyType enemyType;

    public EnemyEmitter(int size,float rocketGenerationTime, float ufoGenerationTime, GameScreen game){
        super(size);
        this.rocketGenerationTime = rocketGenerationTime;
        this.ufoGenerationTime = ufoGenerationTime;
        this.game = game;
    }

    @Override
    protected Enemy newObject() {
        return new Enemy();
    }

    public void render(SpriteBatch batch){
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt){
        innerTimerUfo += dt;
        if(innerTimerUfo > ufoGenerationTime){
            setup(Enemy.EnemyType.UFO);
            innerTimerUfo -= ufoGenerationTime;
        }

        innerTimerRocket += dt;
        if(innerTimerRocket > rocketGenerationTime){
            setup(Enemy.EnemyType.ROCKET);
            innerTimerRocket -= rocketGenerationTime;
        }

        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
    }

    public void setup(Enemy.EnemyType enemyType){
        Enemy enemy = getActiveElement();
        float x = (float) Math.random() * SCREEN_WIDTH + SCREEN_WIDTH;
        float y = (float) Math.random() * SCREEN_HEIGHT;
        int randomLevel = MathUtils.random(1, 4);
        enemy.activate(enemyType, x, y, randomLevel, game);
    }
}
