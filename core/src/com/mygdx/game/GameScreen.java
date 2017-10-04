package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

public class GameScreen implements Screen {

    private Game game;

    private SpriteBatch batch;
    private TextureAtlas atlas;
    private Player player;
    private float dt;
    private Background background;
    private BulletEmitter bulletEmitter;
    private AsteroidEmitter asteroidEmitter;
    private EnemyEmitter enemyEmitter;
    private BitmapFont font;


    public BulletEmitter getBulletEmitter() {
        return bulletEmitter;
    }
    public TextureAtlas getAtlas() {
        return atlas;
    }

    public GameScreen(Game game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        atlas = new TextureAtlas(Gdx.files.internal("highGameAtlas.atlas"));
        player = new Player(this, atlas, atlas.findRegion("hpBar"), 74, 328, 0, 0, 10);
        background = new Background(atlas);
        asteroidEmitter = new AsteroidEmitter(25, 0.5f, atlas.findRegion("asteroid"));
        enemyEmitter = new EnemyEmitter(5, 3.0f, 5.0f, this);
        bulletEmitter = new BulletEmitter(atlas, 200);
        font = new BitmapFont(Gdx.files.internal("font.fnt"));
    }

    @Override
    public void render(float delta) {
        dt = delta;
        update(dt);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.render(batch);
        player.render(batch);
        bulletEmitter.render(batch);
        asteroidEmitter.render(batch);
        enemyEmitter.render(batch);
        player.renderHUD(batch, 20, 668, font);
        batch.end();

    }


    public void update(float dt) {
        background.update(dt, player.getVelocity());
        player.update(dt);
        bulletEmitter.update(dt);
        asteroidEmitter.update(dt);
        enemyEmitter.update(dt);
        checkCollision();
        bulletEmitter.checkNoActiveElement();
        asteroidEmitter.checkNoActiveElement();
        enemyEmitter.checkNoActiveElement();
    }


    private final Vector2 collisionHelper = new Vector2(0, 0);

    public void checkCollision() {


        for (int i = 0; i < bulletEmitter.activeList.size(); i++) {
            Bullet b = bulletEmitter.activeList.get(i);
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
            if (b.isPlayerBullet()) {
                //Коллизии пули - Нло
                for (int j = 0; j < enemyEmitter.activeList.size(); j++) {
                    Enemy enemy = enemyEmitter.activeList.get(j);
                    if (enemy.getHitArea().contains(b.getPosition())) {
                        b.deactivate();
                        enemy.takeDamage(1);
                    }
                }
            } else {
                //Колизии пули Врага - корабль
                if (player.getHitArea().contains(b.getPosition())) {
                    b.deactivate();
                    player.takeDamage(2);
                }
            }
        }

        //Колизии враги - игрок / астреоиды
        for (int i = 0; i < enemyEmitter.activeList.size(); i++) {
            Enemy enemy = enemyEmitter.activeList.get(i);
            if (player.getHitArea().overlaps(enemy.getHitArea())) {
                enemy.deactivate();
                player.takeDamage(5 * enemy.getLevel());
            }

            for (int j = 0; j < asteroidEmitter.getActiveList().size(); j++) {
                Asteroid asteroid = asteroidEmitter.activeList.get(j);
                if (enemy.getHitArea().overlaps(asteroid.getHitArea())) {
                    float len = enemy.getPosition().dst(asteroid.getPosition());
                    float interlen = (enemy.getHitArea().radius + asteroid.getHitArea().radius) - len;
                    collisionHelper.set(asteroid.getPosition()).sub(enemy.getPosition()).nor();
                    asteroid.getPosition().mulAdd(collisionHelper, interlen);
                    asteroid.getPosition().mulAdd(collisionHelper, interlen);
                }
            }
        }

        //Колизии астероиды - игрок
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

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        atlas.dispose();
    }
}
