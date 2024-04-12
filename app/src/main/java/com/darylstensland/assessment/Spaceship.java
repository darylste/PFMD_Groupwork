package com.darylstensland.assessment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class Spaceship {

    // Rectangle for collision detection
    RectF rect;

    // Bitmaps for spaceship visuals
    private Bitmap bitmapup, bitmapleft, bitmapright, bitmapdown, currentBitmap;

    // Spaceship properties
    private float height, length, x, y;
    private int screenX, screenY;
    private int spaceshipSpeed;
    private int health = 100; // Initial health

    // Movement constants
    public static final int STOPPED = 0, LEFT = 1, RIGHT = 2, UP = 3, DOWN = 4;

    // Movement state
    private int spaceshipMoving = STOPPED;

    // Listener for game-over events
    private GameOverListener gameOverListener;

    public interface GameOverListener {
        void onGameOver();
    }

    public Spaceship(Context context, int screenX, int screenY, GameOverListener listener) {
        this.screenX = screenX;
        this.screenY = screenY;
        this.gameOverListener = listener;

        // Initializing the spaceship's rectangle for collision detection
        rect = new RectF();

        // Defining the spaceship's size
        length = screenX / 10;
        height = screenY / 10;


        // Setting initial position
        x = screenX / 2;
        y = screenY / 2;

        x = screenX - screenX/2 - height;
        y = screenY - screenY/2 - length;


        // Setting movement speed
        spaceshipSpeed = 350;

        // Loading and scaling bitmap images for the spaceship
        bitmapup = BitmapFactory.decodeResource(context.getResources(), R.drawable.spaceship_up);
        bitmapright = BitmapFactory.decodeResource(context.getResources(), R.drawable.spaceship_right);
        bitmapleft = BitmapFactory.decodeResource(context.getResources(), R.drawable.spaceship_left);
        bitmapdown = BitmapFactory.decodeResource(context.getResources(), R.drawable.spaceship_down);


        bitmapup = Bitmap.createScaledBitmap(bitmapup, (int) (length), (int) (height), false);
        bitmapright = Bitmap.createScaledBitmap(bitmapright, (int) (length), (int) (height), false);
        bitmapleft = Bitmap.createScaledBitmap(bitmapleft, (int) (length), (int) (height), false);
        bitmapdown = Bitmap.createScaledBitmap(bitmapdown, (int) (length), (int) (height), false);

        // Setting the initial bitmap
        currentBitmap = bitmapup;

        currentBitmap = bitmapright;
        this.screenX = screenX;
        this.screenY = screenY;

    }

    public void setMovementState(int state) {
        spaceshipMoving = state;
    }

    public void update(long fps) {
        // Movement logic
        switch (spaceshipMoving) {
            case LEFT:
                x -= spaceshipSpeed / fps;
                currentBitmap = bitmapleft;
                break;
            case RIGHT:
                x += spaceshipSpeed / fps;
                currentBitmap = bitmapright;
                break;
            case UP:
                y -= spaceshipSpeed / fps;
                currentBitmap = bitmapup;
                break;
            case DOWN:
                y += spaceshipSpeed / fps;
                currentBitmap = bitmapdown;
                break;
        }

        // Wrap around the screen edges
        if (x < 0 - length) x = screenX;
        else if (x > screenX) x = 0 - length;
        if (y < 0 - height) y = screenY;
        else if (y > screenY) y = 0 - height;

        // Update the rectangle for collision detection
        rect.set(x, y, x + length, y + height);

        // Check for game-over conditions
        checkForGameOverCondition();
    }

    private void checkForGameOverCondition() {
        if (health <= 0 || x < 0 || x > screenX || y < 0 || y > screenY) {
            if (gameOverListener != null) {
                gameOverListener.onGameOver();
            }
        }
    }

    public void checkCollisionWithEnemy(RectF enemyRect) {
        if (RectF.intersects(this.rect, enemyRect)) {
            reduceHealth(20);
        }
    }

    public void reduceHealth(int damage) {
        health -= damage;
        if (health <= 0) {
            checkForGameOverCondition();
        }
    }

    // Getters and Setters
    public RectF getRect() {
        return rect;
    }

    public Bitmap getBitmap() {
        return currentBitmap;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getLength() {
        return length;
    }


    public float getHeight(){
        return height;
    }

    public void updateShipPosition(int curX, int curY, int tarX, int tarY) {

        float currentX = curX;
        float currentY = curY;

        float deltaX = tarX - currentX;
        float deltaY = tarY - currentY;


    }


}
