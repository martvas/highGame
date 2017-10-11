package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class MenuScreen implements Screen {

    private HighGame game;
    private SpriteBatch batch;
    private TextureAtlas atlas;
    private Rectangle playRectangle;
    private Rectangle closeRectangle;
    private GameScreen gameScreen;
    private Background background;
    private MyInputProcessor mip;
    private Vector2 emptyVelocity = new Vector2(0, 0);
    private Texture bgTexture;
    private PowerUpsEmitter powerUpsEmitter;

    public MenuScreen(SpriteBatch batch , HighGame game, GameScreen gameScreen){
        this.batch = batch;
        this.game = game;
        this.gameScreen = gameScreen;
    }

    @Override
    public void show() {
        this.atlas = game.getAssets().assetManager.get("highGameAtlas.atlas");
        this.bgTexture = game.getAssets().assetManager.get("bg2.png");
        this.playRectangle = new Rectangle(280,100,234,258);
        this.closeRectangle = new Rectangle(800,100,236,235);
        mip = (MyInputProcessor) Gdx.input.getInputProcessor();
        background = new Background(atlas, bgTexture);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.render(batch);
        batch.draw(atlas.findRegion("play"), playRectangle.x, playRectangle.y);
        batch.draw(atlas.findRegion("close"), closeRectangle.x, closeRectangle.y);
        batch.end();
    }

    public void update(float dt) {
        background.update(dt, emptyVelocity);
        if (mip.isTouchedInAreaBoolean(playRectangle)){
            game.getAssets().assetManager.clear();
            game.getAssets().loadAssets(Assets.ScreenType.GAME);
            game.setScreen(gameScreen);
        }
        if (mip.isTouchedInAreaBoolean(closeRectangle)){
            game.getAssets().assetManager.clear();
            Gdx.app.exit();
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
        background.dispose();
    }
}
