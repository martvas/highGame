package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
    AssetManager assetManager;

    public enum ScreenType{
        MENU, GAME
    }

    public Assets(){
        assetManager = new AssetManager();
    }

    public void loadAssets (ScreenType screenType){
        switch (screenType){
            case MENU:
                assetManager.load("highGameAtlas.atlas", TextureAtlas.class);
                assetManager.load("bg2.png", Texture.class);
                assetManager.finishLoading();
                break;
            case GAME:
                assetManager.load("highGameAtlas.atlas", TextureAtlas.class);
                assetManager.load("bg2.png", Texture.class);
                assetManager.load("font.fnt", BitmapFont.class);
                assetManager.load("music.mp3", Music.class);
                assetManager.load("CollapseNorm.wav", Sound.class);
                assetManager.load("laser.wav", Sound.class);
                assetManager.load("rocketWsound.wav", Sound.class);
                assetManager.load("gun.wav", Sound.class);
                assetManager.finishLoading();
                break;
        }
    }
}
