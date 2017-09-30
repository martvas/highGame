package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Ship {
    private static final float SIZE = 64.0f;
    private static final float HALF_SIZE = SIZE / 2;

    private Vector2 position;
    private Vector2 velocity;
    private AtlasRegion shipTexture;
    private float enginePower;
    private Circle hitArea;
    private float currentFire;
    private float fireRate;
    private HighGame game;
    private int hp;
    private int maxHp;
    private float reddish;
    TextureRegion redHpRegion;
    TextureRegion greenHpRegion;
    private int lives;
    private int score;
    StringBuilder hudStringHelper;

    public static float getHalfSize() {
        return HALF_SIZE;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Ship(HighGame game, TextureAtlas atlas, AtlasRegion textureHP, float x, float y, float vx, float vy, float enginePower){
        this.shipTexture = atlas.findRegion("ship");
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(vx, vy);
        this.enginePower = enginePower;
        this.hitArea = new Circle(position, HALF_SIZE);
        this.game = game;
        this.currentFire = 0.0f;
        this.fireRate = 0.1f;
        this.redHpRegion = new TextureRegion(textureHP, 0, 32, 224, 32);
        this.greenHpRegion = new TextureRegion(textureHP, 0, 0, 224, 32);
        this.maxHp = 100;
        this.hp = maxHp;
        this.lives = 3;
        this.score = 0;
        this.hudStringHelper = new StringBuilder(50);
    }

    public void addScore(int scoreToAdd){
        score += scoreToAdd;
    }

    public void render(SpriteBatch batch){
        if (reddish > 0.01){
            batch.setColor(1.0f, 1.0f - reddish, 1.0f - reddish, 1.0f);
        }
        batch.draw(shipTexture, position.x - HALF_SIZE, position.y - HALF_SIZE, SIZE, SIZE);
        if (reddish > 0.01){
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    public void renderHUD(SpriteBatch batch, float x, float y, BitmapFont font) {
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.draw(redHpRegion, x + (int) (Math.random() * reddish * 10), y + (int) (Math.random() * reddish * 10));
        batch.draw(greenHpRegion, x + (int) (Math.random() * reddish * 10), y + (int) (Math.random() * reddish * 10), (int) ((float) hp / maxHp * 224), 32);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        batch.setColor(1, 1, 0, reddish);
        batch.draw(redHpRegion, x + (int) (Math.random() * reddish * 25), y + (int) (Math.random() * reddish * 25));
        batch.draw(greenHpRegion, x + (int) (Math.random() * reddish * 25), y + (int) (Math.random() * reddish * 25), (int) ((float) hp / maxHp * 224), 32);
        batch.setColor(1, 1, 1, 1);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        hudStringHelper.setLength(0);
        hudStringHelper.append("x").append(lives);
        font.draw(batch, hudStringHelper, x + 224, y + 26);
        hudStringHelper.setLength(0);
        hudStringHelper.append("Score: ").append(score);
        font.draw(batch, hudStringHelper, x, y - 5);

    }

    public void update(float dt){

        if (Gdx.input.isKeyPressed(Input.Keys.W)){
            velocity.y += enginePower;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            velocity.y -= enginePower;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)){
            velocity.x -= enginePower;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)){
            velocity.x += enginePower;
        }
        velocity.scl(0.97f);

        if(Gdx.input.isKeyPressed(Input.Keys.L)){
            currentFire += dt;
            if (currentFire >= fireRate){
                fire();
                currentFire = 0;
            }
        }

        if (position.x > HighGame.SCREEN_WIDTH - HALF_SIZE){
            position.x = HighGame.SCREEN_WIDTH - HALF_SIZE;
            if (velocity.x > 0.0f) velocity.x = 0.0f;
        }
        if (position.x < HALF_SIZE){
            position.x = HALF_SIZE;
            if (velocity.x < 0.0f) velocity.x = 0.0f;
        }
        if (position.y > HighGame.SCREEN_HEIGHT - HALF_SIZE){
            position.y = HighGame.SCREEN_HEIGHT - HALF_SIZE;
            if (velocity.y > 0.0f ) velocity.y = 0.0f;
        }
        if (position.y < HALF_SIZE){
            position.y = HALF_SIZE;
            if (velocity.y < 0.0f) velocity.y = 0.0f;
        }

        reddish -= dt * 4;
        if (reddish <= 0.0f) reddish = 0.0f;

        position.mulAdd(velocity, dt);
        hitArea.set(position, 24);
    }

    public void fire(){
        game.getBulletEmitter().setup(position.x + 24, position.y, 500, 0);
    }

    public boolean takeDamage(int damage){
        hp -= damage;
        reddish += 0.1f * damage;
        if(reddish >= 1.0f) reddish = 1.0f;
        if (hp <= 0){
            hp = maxHp;
            --lives;
            reddish = 0.0f;
        }
        return maxHp <= 0;
    }

}
