package com.mygdx.game;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

public class MyInputProcessor implements InputProcessor {


    class TouchInfo {
        int x;
        int y;
        boolean touched;
    }

    private HighGame highGame;
    private HashMap<Integer, TouchInfo> map = new HashMap<Integer, TouchInfo>();
    private Vector2 temp = new Vector2(0.0f, 0.0f);

    public MyInputProcessor(HighGame highGame){
        this.highGame = highGame;
        for (int i = 0; i < 5; i++) {
            map.put(i, new TouchInfo());
        }
    }

    public void clear(){
        for (int i = 0; i < 5; i++) {
            map.put(i, new TouchInfo());
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        temp.set(screenX, 0);
        map.get(pointer).x = (int) highGame.getViewport().unproject(temp).x;
        temp.set(0, screenY);
        map.get(pointer).y = (int) highGame.getViewport().unproject(temp).y;
        map.get(pointer).touched = true;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        map.get(pointer).x = 0;
        map.get(pointer).y = 0;
        map.get(pointer).touched = false;
        return false;
    }

    public boolean isTouched(int pointer){
        return map.get(pointer).touched;
    }

    public int getX(int pointer){
        return map.get(pointer).x;
    }

    public int getY(int pointer){
        return map.get(pointer).y;
    }

    public int isTouchedInArea(Rectangle rectangle){
        return isTouchedInArea((int)rectangle.x, (int)rectangle.y, (int)rectangle.width, (int)rectangle.height);
    }

    //Проверяет нажат ли тач в определенной области
    public int isTouchedInArea(int x, int y, int w, int h){
        for (Map.Entry<Integer, TouchInfo> o : map.entrySet()){
            if (o.getValue().touched){
                int id = o.getKey();
                TouchInfo t = o.getValue();
                if (t.x > x && t.x < x + w && t.y > y && t.y < y + h){
                    return id;
                }
            }
        }
        return -1;
    }

    public boolean isTouchedInAreaBoolean (Rectangle rectangle){
        return isTouchedInArea((int)rectangle.x, (int)rectangle.y, (int)rectangle.width, (int)rectangle.height) > -1;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        temp.set(screenX, 0);
        map.get(pointer).x = (int) highGame.getViewport().unproject(temp).x;
        temp.set(0, screenY);
        map.get(pointer).y = (int) highGame.getViewport().unproject(temp).y;
        map.get(pointer).touched = true;
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
