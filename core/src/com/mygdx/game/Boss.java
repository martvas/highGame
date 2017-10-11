package com.mygdx.game;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Boss extends Ship {
    private static final float WIDTH = 256.0f;
    private static final float HEIGHT = 128.0f;
    private static final float WIDTH_HALF_SIZE = WIDTH / 2;
    private static final float HEIGHT_HALF_SIZE = HEIGHT / 2;

    private Rectangle rectHitArea;
    private AtlasRegion bossTexture;

    private WeaponType weaponType;

    private float innerTimerToMove;
    private float randomEnemyMoveTime;
    private int randomMove;

    private TextureRegion redHpRegion;
    private TextureRegion greenHpRegion;

    private AsteroidEmitter asteroidEmitter;
    private UfoEmitter ufoEmitter;
    private RocketEmitter rocketEmitter;

    private int movementsCount;

    public Rectangle getRectHitArea() {
        return rectHitArea;
    }


    public Boss(AtlasRegion bossTexture, GameScreen game, AtlasRegion textureHP, AsteroidEmitter asteroidEmitter, UfoEmitter ufoEmitter, RocketEmitter rocketEmitter) {
        this.position = new Vector2(0.0f, 0.0f);
        this.velocity = new Vector2(0.0f, 0.0f);
        this.hp = 0;
        this.maxHp = 0;
        this.reddish = 0.0f;
        this.isPlayer = false;
        this.bossTexture = bossTexture;
        this.game = game;
        this.innerTimerToMove = 0;
        this.randomMove = 0;
        this.isPlayer = false;
        this.weaponType = WeaponType.ROCKET;
        this.rectHitArea = new Rectangle();
        this.redHpRegion = new TextureRegion(textureHP, 0, 32, 224, 32);
        this.greenHpRegion = new TextureRegion(textureHP, 0, 0, 224, 32);
        this.asteroidEmitter = asteroidEmitter;
        this.ufoEmitter = ufoEmitter;
        this.rocketEmitter = rocketEmitter;
        this.active = false;
    }

    public void renderHUD(SpriteBatch batch, float x, float y) {
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.draw(redHpRegion, x + (int) (Math.random() * reddish * 10), y + (int) (Math.random() * reddish * 10));
        batch.draw(greenHpRegion, x + (int) (Math.random() * reddish * 10), y + (int) (Math.random() * reddish * 10), (int) ((float) hp / maxHp * 224), 32);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        batch.setColor(1, 1, 0, reddish);
        batch.draw(redHpRegion, x + (int) (Math.random() * reddish * 25), y + (int) (Math.random() * reddish * 25));
        batch.draw(greenHpRegion, x + (int) (Math.random() * reddish * 25), y + (int) (Math.random() * reddish * 25), (int) ((float) hp / maxHp * 224), 32);
        batch.setColor(1, 1, 1, 1);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void activate() {
        position.set(1200, 360);
        randomEnemyMoveTime = MathUtils.random(0.01f, 0.5f);
        rectHitArea.set(position.x, position.y, WIDTH, HEIGHT);
        fireRate = 0.5f;
        maxHp = 150;
        hp = maxHp;
        reddish = 0.0f;
        active = true;
        asteroidEmitter.setAsteroidActivation(false);
        ufoEmitter.setUfoActivation(false);
        rocketEmitter.setRocketActivation(false);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (reddish > 0.01) {
            batch.setColor(1.0f, 1.0f - reddish, 1.0f - reddish, 1.0f);
        }

        batch.draw(bossTexture, position.x - WIDTH_HALF_SIZE, position.y - HEIGHT_HALF_SIZE, WIDTH_HALF_SIZE, HEIGHT_HALF_SIZE,
                WIDTH, HEIGHT, 1, 1, 0);

        if (reddish > 0.1) {
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    @Override
    public void update(float dt) {

        bossMovements(dt);
        pressFire(dt, weaponType);

        position.mulAdd(velocity, dt);
        rectHitArea.setPosition(position);

        reddish -= 2 * dt;
        if (reddish <= 0) reddish = 0;

        if (position.x < 640) {
            if (velocity.x < 0.0f){
                velocity.x *= -1;
            }
        }

        if (position.x > HighGame.SCREEN_WIDTH - WIDTH_HALF_SIZE) {
            position.x = HighGame.SCREEN_WIDTH - WIDTH_HALF_SIZE;
            if (velocity.x > 0.0f) velocity.x *= -1;
        }
        if (position.y > HighGame.SCREEN_HEIGHT - HEIGHT_HALF_SIZE) {
            position.y = HighGame.SCREEN_HEIGHT - HEIGHT_HALF_SIZE;
            if (velocity.y > 0.0f) {
                velocity.y *= -1;
            }
        }
        if (position.y < HEIGHT_HALF_SIZE) {
            position.y = HEIGHT_HALF_SIZE;
            if (velocity.y < 0.0f) {
                velocity.y *= -1;
            }
        }
    }

    @Override
    public void onDestroy() {
        deactivate();
        asteroidEmitter.setAsteroidActivation(true);
        ufoEmitter.setUfoActivation(true);
        rocketEmitter.setRocketActivation(true);
    }

    public void bossMovements(float dt) {

        if (movementsCount == 0 && position.x > 1100) {
            velocity.set((float) Math.sin(velocity.x) * 50, (float) Math.cos(velocity.x) * 200);
            movementsCount = 1;
        }

        innerTimerToMove += dt;
        if (randomMove == 0) {
            randomMove = MathUtils.random(1, 4);
        }
        switch (randomMove) {
            case (1):
                velocity.x = -MathUtils.random(200.0f, 300.0f);
                velocity.y = MathUtils.random(-200.0f, 0.0f);
                randomMove = 5;
                break;
            case (2):
                velocity.x = -MathUtils.random(200.0f, 300.0f);
                velocity.y = MathUtils.random(0.0f, 200.0f);
                randomMove = 5;
                break;
            case (3):
                velocity.x = MathUtils.random(-300.0f, -200.0f);
                velocity.y = MathUtils.random(0.0f, 200.0f);
                randomMove = 5;
                break;
            case (4):
                velocity.x = MathUtils.random(-300.0f, -200.0f);
                velocity.y = MathUtils.random(-200.0f, 0.0f);
                randomMove = 5;
                break;
        }
        if (innerTimerToMove > 2 + randomEnemyMoveTime) {
            randomMove = 0;
            innerTimerToMove = 0;
            randomEnemyMoveTime = MathUtils.random(0.01f, 1.0f);
        }
    }
}
