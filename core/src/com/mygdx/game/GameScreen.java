package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

public class GameScreen implements Screen {

    private HighGame game;

    private SpriteBatch batch;
    private TextureAtlas atlas;
    private Player player;
    private float dt;
    private Background background;
    private BulletEmitter bulletEmitter;
    private AsteroidEmitter asteroidEmitter;
    private UfoEmitter ufoEmitter;
    private RocketEmitter rocketEmitter;
    private BoomEmitter boomEmitter;
    private BitmapFont font;
    private Music music;
    private PowerUpsEmitter powerUpsEmitter;
    private BossEmitter bossEmitter;

    public BulletEmitter getBulletEmitter() {
        return bulletEmitter;
    }
    public TextureAtlas getAtlas() {
        return atlas;
    }

    public GameScreen(HighGame game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        atlas = game.getAssets().assetManager.get("highGameAtlas.atlas");
        player = new Player(this, atlas, atlas.findRegion("hpBar"), 74, 328, 0, 0, 10, (Sound) game.getAssets().assetManager.get("laser.wav"),
                (Sound) game.getAssets().assetManager.get("gun.wav"), (Sound) game.getAssets().assetManager.get("rocketWsound.wav"), game);
        background = new Background(atlas, (Texture) game.getAssets().assetManager.get("bg2.png"));
        asteroidEmitter = new AsteroidEmitter(25, 0.5f, atlas.findRegion("asteroid"));
        ufoEmitter = new UfoEmitter(5, 5.0f, this);
        rocketEmitter = new RocketEmitter(5, 3.0f, this);
        boomEmitter = new BoomEmitter(atlas.findRegion("explosion_my"), (Sound) game.getAssets().assetManager.get("CollapseNorm.wav"));
        bulletEmitter = new BulletEmitter(atlas, 200);
        font = game.getAssets().assetManager.get("font.fnt");
        music = game.getAssets().assetManager.get("music.mp3");
        powerUpsEmitter = new PowerUpsEmitter(atlas.findRegion("powerup"));
        bossEmitter = new BossEmitter(atlas.findRegion("redshipr"), atlas.findRegion("hpBar"),this,  asteroidEmitter, ufoEmitter, rocketEmitter);

        music.setLooping(true);
        music.setVolume(0.05f);
        music.play();
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
        boomEmitter.render(batch);
        asteroidEmitter.render(batch);
        ufoEmitter.render(batch);
        rocketEmitter.render(batch);
        powerUpsEmitter.render(batch);
        bossEmitter.render(batch);
        player.renderHUD(batch, 20, 668, font);
        batch.end();
    }


    public void update(float dt) {
        background.update(dt, player.getVelocity());
        player.update(dt);
        bulletEmitter.update(dt);
        asteroidEmitter.update(dt);
        ufoEmitter.update(dt);
        rocketEmitter.update(dt);
        bossEmitter.update(dt);
        powerUpsEmitter.update(dt);
        checkCollision();
        boomEmitter.update(dt);
        bulletEmitter.checkNoActiveElement();
        asteroidEmitter.checkNoActiveElement();
        ufoEmitter.checkNoActiveElement();
        rocketEmitter.checkNoActiveElement();

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            goToMenu();
        }
    }


    private final Vector2 collisionHelper = new Vector2(0, 0);

    public void checkCollision() {
        for (int i = 0; i < bulletEmitter.activeList.size(); i++) {
            Bullet b = bulletEmitter.activeList.get(i);

            //Колизии пули - астероиды
            for (int l = 0; l < asteroidEmitter.getActiveList().size(); l++) {
                Asteroid a = asteroidEmitter.getActiveList().get(l);
                if (a.getHitArea().contains(b.getPosition())) {
                    if (a.takeDamage(b.getWeaponType().getDamage())) {
                        a.deactivate();
                        player.addScore(10);
                    }
                    b.deactivate();
                }
            }
            if (b.isPlayerBullet()) {
                //Коллизии пули - враги
                for (int j = 0; j < ufoEmitter.activeList.size(); j++) {
                    Ufo ufo = ufoEmitter.activeList.get(j);
                    if (ufo.getHitArea().contains(b.getPosition())) {
                        b.deactivate();
                        if(ufo.takeDamage(b.getWeaponType().getDamage())){
                            boomEmitter.setup(ufo.getPosition());
                            powerUpsEmitter.makePower(ufo.getPosition().x, ufo.getPosition().y);
                            player.addScore(10* ufo.getLevel());
                        }
                    }
                }
                for (int j = 0; j < rocketEmitter.activeList.size(); j++) {
                    Rocket rocket = rocketEmitter.activeList.get(j);
                    if (rocket.getHitArea().contains(b.getPosition())) {
                        b.deactivate();
                        if(rocket.takeDamage(b.getWeaponType().getDamage())){
                            boomEmitter.setup(rocket.getPosition());
                            powerUpsEmitter.makePower(rocket.getPosition().x, rocket.getPosition().y);
                            player.addScore(10* rocket.getLevel());

                        }
                    }
                }
                Boss boss = bossEmitter.getBoss();
                if (boss.isActive() && boss.getRectHitArea().contains(b.getPosition())){
                    b.deactivate();
                    if (boss.takeDamage(b.getWeaponType().getDamage())){
                        boomEmitter.setup(boss.getPosition());
                        for (int j = 0; j < 5; j++) {
                            boomEmitter.setup(new Vector2(boss.getPosition().x + (float)Math.random() * 128, boss.getPosition().y + (float)Math.random() * 64));
                        }

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

        //Колизии рокеты - игрок / астреоиды
        for (int i = 0; i < rocketEmitter.activeList.size(); i++) {
            Rocket rocket = rocketEmitter.activeList.get(i);
            if (player.getHitArea().overlaps(rocket.getHitArea())) {
                rocket.deactivate();
                boomEmitter.setup(rocket.getPosition());
                player.takeDamage(5 * rocket.getLevel());
            }

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

        //Колизии НЛО - игрок / астреоиды
        for (int i = 0; i < ufoEmitter.activeList.size(); i++) {
            Ufo ufo = ufoEmitter.activeList.get(i);
            if (player.getHitArea().overlaps(ufo.getHitArea())) {
                ufo.deactivate();
                boomEmitter.setup(ufo.getPosition());
                player.takeDamage(5 * ufo.getLevel());
            }

            for (int j = 0; j < asteroidEmitter.getActiveList().size(); j++) {
                Asteroid asteroid = asteroidEmitter.activeList.get(j);
                if (ufo.getHitArea().overlaps(asteroid.getHitArea())) {
                    float len = ufo.getPosition().dst(asteroid.getPosition());
                    float interlen = (ufo.getHitArea().radius + asteroid.getHitArea().radius) - len;
                    collisionHelper.set(asteroid.getPosition()).sub(ufo.getPosition()).nor();
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

        for (int i = 0; i < powerUpsEmitter.getPowerUps().length; i++) {
            PowerUp p = powerUpsEmitter.getPowerUps()[i];
            if (p.isActive()) {
                if (player.getHitArea().contains(p.getPosition())) {
                    p.use(player);
                    p.deactivate();
                }
            }
        }

        if (player.getLives() < 0){
            goToMenu();
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
        music.dispose();
        player.dispose();
        font.dispose();
        background.dispose();
        boomEmitter.dispose();
    }

    public void goToMenu(){
        game.getAssets().assetManager.clear();
        game.getAssets().loadAssets(Assets.ScreenType.MENU);
        game.setScreen(game.getMenuScreen());
    }
}
