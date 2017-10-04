package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.awt.*;

public class Joystick {
    private TextureAtlas.AtlasRegion atlasRegion;
    private TextureRegion back;
    private TextureRegion stick;
    private Rectangle rectangle;
    private float joyCenterX, joyCenterY;
    private int lastId;
    private Vector2 vs;
    private Vector2 normal;

    public Vector2 getNormal() {
        return normal;
    }

    public float getPower(){
        return vs.len();
    }

    public Joystick(TextureAtlas.AtlasRegion atlasRegion){
        this.atlasRegion = atlasRegion;
        this.back = new TextureRegion(atlasRegion, 0, 0, 200, 200);
        this.stick = new TextureRegion(atlasRegion, 0, 200, 50, 50);
        rectangle = new Rectangle(50, 50, 200, 200);
        joyCenterX = rectangle.x + rectangle.width / 2;
        joyCenterY = rectangle.y + rectangle.height / 2;
        vs = new Vector2(0, 0);
        lastId = -1;
        normal = new Vector2(0,0);
    }

    public void render(SpriteBatch batch){
        batch.setColor(1.0f, 1.0f,1.0f, 0.3f);
        batch.draw(back, rectangle.x, rectangle.y);
        batch.draw(stick, joyCenterX + vs.x - 25, joyCenterY + vs.y - 25);
        batch.setColor(1.0f, 1.0f,1.0f, 1.0f);
    }

    public void update(){
        MyInputProcessor mip = (MyInputProcessor) Gdx.input.getInputProcessor();
        if(lastId == -1){
            lastId = mip.isTouchedInArea(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
        if (lastId > -1){
             float touchX = mip.getX(lastId);
            float touchY = mip.getY(lastId);
            vs.x = touchX - joyCenterX;
            vs.y = touchY - joyCenterY;
            if (vs.len() > 75){
                vs.nor().scl(75);
            }
        }

        if (lastId > -1 && !mip.isTouched(lastId)){
            lastId = -1;
            vs.x = 0;
            vs.y = 0;
        }
        normal.set(vs);
        normal.nor();
    }
}
