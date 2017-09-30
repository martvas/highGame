package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class EnemyUfo implements PoolableMy {
    private static final float SIZE = 64.0f;
    private static final float HALF_SIZE = SIZE / 2;

    private Vector2 position;
    private Vector2 velocity;
    private int level;
    private int hp;
    private int maxHp;
    private float reddish;
    private float scale;
    private boolean active;
    private Circle hitArea;

    float innerTimerToMove;
    float randomEnemyMoveTime;
    int randomMove;

    HighGame game;
    float fireTimer;
    private float fireRate;

    public EnemyUfo(){
        position = new Vector2(0.0f, 0.0f);
        velocity = new Vector2(0.0f, 0.0f);
        hp = 0;
        maxHp = 0;
        reddish = 0.0f;
        hitArea = new Circle();
        level = 0;

        innerTimerToMove = 0;
        randomMove = 0;

        fireTimer = 0;
        fireRate = 0;
        active = false;
    }


    public Circle getHitArea() {
        return hitArea;
    }


    @Override
    public boolean isActive() {
        return active;
    }

    public void render(SpriteBatch batch, TextureAtlas.AtlasRegion enemyTexture){
        if (reddish > 0.01){
            batch.setColor(1.0f, 1.0f- reddish, 1.0f - reddish, 1.0f);
        }
        batch.draw(enemyTexture, position.x - HALF_SIZE, position.y - HALF_SIZE, HALF_SIZE, HALF_SIZE, SIZE,
                SIZE, scale, scale, 0);

        if (reddish > 0.1){
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    public void update(float dt) {

        movements(dt);
        fireTimer(dt);

        position.mulAdd(velocity, dt);
        hitArea.setPosition(position);

        reddish -= 2 * dt;
        if (reddish <= 0) reddish = 0;

        if (position.x < -100){
            deactivate();
        }
        if (position.y > HighGame.SCREEN_HEIGHT - HALF_SIZE){
            position.y = HighGame.SCREEN_HEIGHT - HALF_SIZE;
            if (velocity.y > 0.0f ) velocity.y *= -1;
        }
        if (position.y < HALF_SIZE){
            position.y = HALF_SIZE;
            if (velocity.y < 0.0f) velocity.y *= -1;
        }
    }

    public void deactivate(){
        active = false;
    }


    public void activate(float x, float y, int level, HighGame game){
        position.set(x, y);
        this.level = level;
        maxHp = 3 * level;
        hp = maxHp;
        reddish = 0.0f;
        scale = 1.0f + (0.1f * level);
        hitArea = new Circle(position.x, position.y, 28 * scale);

        randomEnemyMoveTime = MathUtils.random(0.01f, 0.5f);

        this.game = game;
        fireRate = 2f + (0.2f * level) + randomEnemyMoveTime;

        active = true;
    }

    public boolean takeDamage(int damage){
        maxHp -= damage;
        reddish += 0.2;
        if (reddish >= 1.0f) reddish = 1.0f;
        if (maxHp <= 0) {
            deactivate();
            return true;
        }
        return false;
    }

    //Мои враги это НЛО поэтому они так передвигаются.
    //Навстречу моему кораблю двигаются на большее растояние, но могут и отступить на расстояние покороче
    public void movements(float dt){
        innerTimerToMove += dt;
        if (randomMove == 0){
            randomMove = MathUtils.random(1 , 4);
        }
        switch (randomMove){
            case (1):
                velocity.x = -MathUtils.random(200.0f, 300.0f);
                velocity.y = MathUtils.random(-200.0f, 0.0f);
                randomMove = 5;
                break;
            case (2):
                velocity.x = -MathUtils.random(200.0f, 300.0f);;
                velocity.y = MathUtils.random(0.0f, 200.0f);;
                randomMove = 5;
                break;
            case (3):
                velocity.x = MathUtils.random(50.0f, 75.0f);
                velocity.y = MathUtils.random(0.0f, 50.0f);
                break;
            case (4):
                velocity.x = MathUtils.random(50.0f, 75.0f);
                velocity.y = MathUtils.random( -50.0f, 0.0f);
                randomMove = 5;
                break;
        }
        //Чтобы все НЛО двигались с разной частотой добавил рандомный коэфициент
        if (innerTimerToMove > 1 + randomEnemyMoveTime) {
            randomMove = 0;
            innerTimerToMove = 0;
        }
    }

    //Стреляют они также с рандомный коэфициэнтом
    public void fireTimer(float dt){
        fireTimer += dt;
        if (fireTimer > fireRate){
            fire();
            fireTimer = 0;
        }
    }

    public void fire(){
        game.getBulletEmitterEnemy().setup(position.x -32, position.y, -300, 0);
    }

}
