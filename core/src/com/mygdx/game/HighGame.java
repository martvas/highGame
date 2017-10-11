package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class HighGame extends Game {
    public static final float SCREEN_WIDTH = 1280.0f;
    public static final float SCREEN_HEIGHT = 720.0f;

    private SpriteBatch batch;
    private GameScreen gameScreen;
    private MenuScreen menuScreen;
    private Assets assets;

    public Assets getAssets() {
        return assets;
    }

    public MenuScreen getMenuScreen() {
        return menuScreen;
    }

    private Viewport viewport;
    private Camera camera;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
        viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
        MyInputProcessor mip = new MyInputProcessor(this);
        Gdx.input.setInputProcessor(mip);
        gameScreen = new GameScreen(this, batch);
        menuScreen = new MenuScreen(batch, this, gameScreen);
        assets = new Assets();
        assets.loadAssets(Assets.ScreenType.MENU);
        setScreen(menuScreen);
    }

    public Viewport getViewport(){
        return viewport;
    }

    @Override
    public void resize(int width, int height){
        viewport.update(width, height, true);
        viewport.apply();
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        getScreen().render(dt);
    }


    @Override
    public void dispose() {
        batch.dispose();
     }

}
