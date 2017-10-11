package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.awt.*;

public class Joystick {
    private AtlasRegion joystickRegion;
    private TextureRegion back;
    private TextureRegion stick;
    private AtlasRegion fireButton;
    private Rectangle rectangle;
    private float joyCenterX, joyCenterY;
    private int lastId;
    private int fireLastId;
    private Vector2 vs;
    private Vector2 normal;

    private Rectangle fireRectangle;
    private Player player;

    public Vector2 getNormal() {
        return normal;
    }

    public float getPower(){
        return vs.len();
    }

    public Joystick(TextureAtlas atlas, Player player){
        this.joystickRegion = atlas.findRegion("joystick");
        this.back = new TextureRegion(joystickRegion, 0, 0, 200, 200);
        this.stick = new TextureRegion(joystickRegion, 0, 200, 50, 50);
        this.fireButton = atlas.findRegion("fireButton");
        rectangle = new Rectangle(50, 50, 200, 200);
        joyCenterX = rectangle.x + rectangle.width / 2;
        joyCenterY = rectangle.y + rectangle.height / 2;
        vs = new Vector2(0, 0);
        lastId = -1;
        normal = new Vector2(0,0);

        fireRectangle = new Rectangle(1000, 75, 128, 128);
        fireLastId = -1;
        this.player = player;
    }


    public void render(SpriteBatch batch){
        batch.setColor(1.0f, 1.0f,1.0f, 0.3f);
        batch.draw(back, rectangle.x, rectangle.y);
        batch.draw(stick, joyCenterX + vs.x - 25, joyCenterY + vs.y - 25);
        batch.draw(fireButton, fireRectangle.x, fireRectangle.y, 128, 128);
        batch.setColor(1.0f, 1.0f,1.0f, 1.0f);
    }

    public void update(float dt){
        MyInputProcessor mip = (MyInputProcessor) Gdx.input.getInputProcessor();

        if (mip.isTouchedInArea(fireRectangle) > -1){
            player.pressFire(dt, WeaponType.BULLET);
        }

        if(lastId == -1){
            lastId = mip.isTouchedInArea((int)rectangle.x, (int)rectangle.y, (int)rectangle.width, (int)rectangle.height);
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
