package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;


public class HighGame extends ApplicationAdapter {
    public static final float SCREEN_WIDTH = 1280.0f;
    public static final float SCREEN_HEIGHT = 720.0f;

    private SpriteBatch batch;
    private TextureAtlas atlas;
    private Ship ship;
    private float dt;
    private Background background;
    private BulletEmitter bulletEmitter;
    private BulletEmitter bulletEmitterEnemy;
    private AsteroidEmitter asteroidEmitter;
    private RocketEmitter rocketEmitter;
    private EnemyUfoEmitter enemyUfoEmitter;
    private BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        atlas = new TextureAtlas(Gdx.files.internal("highGameAtlas.atlas"));
        ship = new Ship(this, atlas, atlas.findRegion("hpBar"), 74, 328, 0, 0, 10);
        background = new Background(atlas);
        asteroidEmitter = new AsteroidEmitter(25, 0.5f);
        rocketEmitter = new RocketEmitter(3, 5.0f);
        enemyUfoEmitter = new EnemyUfoEmitter(1, this, 7.0f);
        bulletEmitter = new BulletEmitter(atlas.findRegion("bullet"), 200);
        bulletEmitterEnemy = new BulletEmitter(atlas.findRegion("bulletOther"), 100);
        font = new BitmapFont(Gdx.files.internal("font.fnt"));
    }

    public BulletEmitter getBulletEmitter() {
        return bulletEmitter;
    }

    public BulletEmitter getBulletEmitterEnemy() {
        return bulletEmitterEnemy;
    }

    @Override
    public void render() {
        dt = Gdx.graphics.getDeltaTime();
        update(dt);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.render(batch);
        ship.render(batch);
        rocketEmitter.render(batch, atlas.findRegion("enemyRocket"));
        bulletEmitter.render(batch);
        bulletEmitterEnemy.render(batch);
        asteroidEmitter.render(batch, atlas.findRegion("asteroid"));
        enemyUfoEmitter.render(batch, atlas.findRegion("ufo"));
        ship.renderHUD(batch, 20, 668, font);
        batch.end();
    }

    public void update(float dt) {
        background.update(dt, ship.getVelocity());
        ship.update(dt);
        bulletEmitter.update(dt);
        bulletEmitterEnemy.update(dt);
        asteroidEmitter.update(dt);
        rocketEmitter.update(dt);
        enemyUfoEmitter.update(dt);
        checkCollision();
        rocketEmitter.checkNoActiveElement();
        bulletEmitter.checkNoActiveElement();
        bulletEmitterEnemy.checkNoActiveElement();
        asteroidEmitter.checkNoActiveElement();
        enemyUfoEmitter.checkNoActiveElement();
    }

    @Override
    public void dispose() {
        batch.dispose();
        atlas.dispose();
    }

    private final Vector2 collisionHelper = new Vector2(0, 0);

    public void checkCollision() {

        //Коллизии пули - Нло
        for (int i = 0; i < bulletEmitter.activeList.size(); i++) {
            Bullet b = bulletEmitter.activeList.get(i);
            for (int j = 0; j < enemyUfoEmitter.activeList.size(); j++) {
                EnemyUfo ufo = enemyUfoEmitter.activeList.get(j);
                if (ufo.getHitArea().contains(b.getPosition())) {
                    b.deactivate();
                    ufo.takeDamage(1);
                }
            }
        }

        //Колизии ракеты - корабль
        for (int i = 0; i < rocketEmitter.activeList.size(); i++) {
            Rocket rocket = rocketEmitter.activeList.get(i);
            if (ship.getHitArea().overlaps(rocket.getHitArea())) {
                rocket.deactivate();
                ship.takeDamage(5 * rocket.getLevel());
            }
        }

        //Колизии рокеты - астероиды
        for (int i = 0; i < rocketEmitter.activeList.size(); i++) {
            Rocket rocket = rocketEmitter.activeList.get(i);
            for (int j = 0; j < asteroidEmitter.getActiveList().size(); j++) {
                Asteroid asteroid = asteroidEmitter.activeList.get(j);
                if (rocket.getHitArea().overlaps(asteroid.getHitArea())) {
                    float len = rocket.getPosition().dst(asteroid.getPosition());
                    float interlen = (rocket.getHitArea().radius + asteroid.getHitArea().radius) - len;
                    collisionHelper.set(asteroid.getPosition()).sub(rocket.getPosition()).nor();
                    asteroid.getPosition().mulAdd(collisionHelper, interlen);
                    asteroid.getPosition().mulAdd(collisionHelper, interlen);
                }
            }
        }

        //Колизии пули Врага - корабль
        for (int i = 0; i < bulletEmitterEnemy.getActiveList().size(); i++) {
            Bullet b = bulletEmitterEnemy.getActiveList().get(i);
            if (ship.getHitArea().contains(b.getPosition())) {
                b.deactivate();
                ship.takeDamage(2);
            }
        }

        //Колизии пули - ракеты
        for (int i = 0; i < bulletEmitter.getActiveList().size(); i++) {
            Bullet b = bulletEmitter.getActiveList().get(i);
            for (int j = 0; j < rocketEmitter.getActiveList().size(); j++) {
                Rocket r = rocketEmitter.getActiveList().get(j);
                if (r.getHitArea().contains(b.getPosition())) {
                    b.deactivate();
                    r.takeDamage(1);
                }
            }
        }

        //Колизии пули - астероиды
        for (int i = 0; i < bulletEmitter.getActiveList().size(); i++) {
            Bullet b = bulletEmitter.getActiveList().get(i);
            for (int j = 0; j < asteroidEmitter.getActiveList().size(); j++) {
                Asteroid a = asteroidEmitter.getActiveList().get(j);
                if (a.getHitArea().contains(b.getPosition())) {
                    if (a.takeDamage(1)) {
                        a.deactivate();
                        ship.addScore(10);
                    }
                    b.deactivate();
                }
            }
        }

        //Колизии астероиды - корабль
        for (int j = 0; j < asteroidEmitter.getActiveList().size(); j++) {
            Asteroid a = asteroidEmitter.getActiveList().get(j);
            if (ship.getHitArea().overlaps(a.getHitArea())) {
                float len = ship.getPosition().dst(a.getPosition());
                float interLen = (ship.getHitArea().radius + a.getHitArea().radius) - len;
                collisionHelper.set(a.getPosition()).sub(ship.getPosition()).nor();
                a.getPosition().mulAdd(collisionHelper, interLen);
                ship.getPosition().mulAdd(collisionHelper, -interLen);
                a.getVelocity().mulAdd(collisionHelper, interLen * 4);
                ship.getVelocity().mulAdd(collisionHelper, -interLen * 4);
                a.takeDamage(1);
                ship.takeDamage(1);
            }

        }
    }
}
