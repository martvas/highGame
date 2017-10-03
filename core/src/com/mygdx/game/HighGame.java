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
    private Player player;
    private float dt;
    private Background background;
    private BulletEmitter bulletEmitter;
    private AsteroidEmitter asteroidEmitter;
    private RocketEmitter rocketEmitter;
    private EnemyUfoEmitter enemyUfoEmitter;
    private BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        atlas = new TextureAtlas(Gdx.files.internal("highGameAtlas.atlas"));
        player = new Player(this, atlas, atlas.findRegion("hpBar"), 74, 328, 0, 0, 10);
        background = new Background(atlas);
        asteroidEmitter = new AsteroidEmitter(25, 0.5f, atlas.findRegion("asteroid"));
        rocketEmitter = new RocketEmitter(3, 5.0f);
        enemyUfoEmitter = new EnemyUfoEmitter(5, 7.0f, this);
        bulletEmitter = new BulletEmitter(atlas, 200);
        font = new BitmapFont(Gdx.files.internal("font.fnt"));
    }

    public BulletEmitter getBulletEmitter() {
        return bulletEmitter;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void render() {
        dt = Gdx.graphics.getDeltaTime();
        update(dt);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.render(batch);
        player.render(batch);
        rocketEmitter.render(batch, atlas.findRegion("enemyRocket"));
        bulletEmitter.render(batch);
        asteroidEmitter.render(batch);
        enemyUfoEmitter.render(batch);
        player.renderHUD(batch, 20, 668, font);
        batch.end();
    }

    public void update(float dt) {
        background.update(dt, player.getVelocity());
        player.update(dt);
        bulletEmitter.update(dt);
        asteroidEmitter.update(dt);
        rocketEmitter.update(dt);
        enemyUfoEmitter.update(dt);
        checkCollision();
        rocketEmitter.checkNoActiveElement();
        bulletEmitter.checkNoActiveElement();
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


        for (int i = 0; i < bulletEmitter.activeList.size(); i++) {
            Bullet b = bulletEmitter.activeList.get(i);
            if (b.isPlayerBullet()){
                //Коллизии пули - Нло
                for (int j = 0; j < enemyUfoEmitter.activeList.size(); j++) {
                    EnemyUfo ufo = enemyUfoEmitter.activeList.get(j);
                    if (ufo.getHitArea().contains(b.getPosition())) {
                        b.deactivate();
                        ufo.takeDamage(1);
                    }

                    //Колизии пули - ракеты
                    for (int k = 0; k < rocketEmitter.getActiveList().size(); k++) {
                        Rocket r = rocketEmitter.getActiveList().get(k);
                        if (r.getHitArea().contains(b.getPosition())) {
                            b.deactivate();
                            r.takeDamage(1);
                        }
                    }

                    //Колизии пули - астероиды
                    for (int l = 0; l < asteroidEmitter.getActiveList().size(); l++) {
                        Asteroid a = asteroidEmitter.getActiveList().get(l);
                        if (a.getHitArea().contains(b.getPosition())) {
                            if (a.takeDamage(1)) {
                                a.deactivate();
                                player.addScore(10);
                            }
                            b.deactivate();
                        }
                    }
                }
            }
            else {

            }
        }

        //Колизии ракеты - корабль
        for (int i = 0; i < rocketEmitter.activeList.size(); i++) {
            Rocket rocket = rocketEmitter.activeList.get(i);
            if (player.getHitArea().overlaps(rocket.getHitArea())) {
                rocket.deactivate();
                player.takeDamage(5 * rocket.getLevel());
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
        for (int i = 0; i < bulletEmitter.getActiveList().size(); i++) {
            Bullet b = bulletEmitter.getActiveList().get(i);
            if (player.getHitArea().contains(b.getPosition())) {
                b.deactivate();
                player.takeDamage(2);
            }
        }


        for (int i = 0; i < bulletEmitter.getActiveList().size(); i++) {
            Bullet b = bulletEmitter.getActiveList().get(i);

        }

        //Колизии астероиды - корабль
        for (int j = 0; j < asteroidEmitter.getActiveList().size(); j++) {
            Asteroid a = asteroidEmitter.getActiveList().get(j);
            if (player.getHitArea().overlaps(a.getHitArea())) {
                float len = player.getPosition().dst(a.getPosition());
                float interLen = (player.getHitArea().radius + a.getHitArea().radius) - len;
                collisionHelper.set(a.getPosition()).sub(player.getPosition()).nor();
                a.getPosition().mulAdd(collisionHelper, interLen);
                player.getPosition().mulAdd(collisionHelper, -interLen);
                a.getVelocity().mulAdd(collisionHelper, interLen * 4);
                player.getVelocity().mulAdd(collisionHelper, -interLen * 4);
                a.takeDamage(1);
                player.takeDamage(1);
            }

        }
    }
}
