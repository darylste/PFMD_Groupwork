package com.darylstensland.assessment;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Space;

import java.io.IOException;


import android.content.Context;

import android.graphics.Canvas;

import android.graphics.Color;

import android.graphics.Paint;

import android.graphics.Typeface;

import android.view.MotionEvent;

import android.view.SurfaceView;


public class SpaceGameView extends SurfaceView implements Runnable {
    public volatile boolean playing;
    public int score = 0;
    public int lives = 3;
    private Context context;
    private Thread gameThread = null;
    private SurfaceHolder ourHolder;
    private boolean paused = true;
    private Canvas canvas;
    private Paint paint;
    private long fps;
    private long timeThisFrame;
    private int screenX;
    private int screenY;
    private Spaceship spaceShip;
    private Bullet bullet;
    private Asteroid[] asteroids = new Asteroid[5];
    private Enemy[] enemies = new Enemy[5];
    private int numEnemies = 0;
    private int numAsteroids = 0;


    public SpaceGameView(Context context, int x, int y) {
        super(context);
        this.context = context;

        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        spaceShip = new Spaceship(context, screenX, screenY);
        bullet = new Bullet(screenX, screenY);


        initLevel();
    }


    private void initLevel() {
       initEnemies();
       initAsteroids();
    }
    private void initEnemies() {
        numEnemies = 0;
        for (int row = 0; row < 5; row++) {
            enemies[row] = new Enemy(context, row, screenX, screenY);
            numEnemies++;
        }
    }

    private void initAsteroids() {
        numAsteroids = 0;
        for(int row = 0; row <5; row++) {
            asteroids[row] = new Asteroid(context, row, screenX, screenY);
            asteroids[row].setVisible();
            numAsteroids++;
        }
    }


    @Override
    public void run() {
        while (playing) {
            long startFrameTime = System.currentTimeMillis();

            if (!paused) {
                update();
            }

            draw();

            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }

        }
    }


    private void update() {

        spaceShip.update(fps);

        if (bullet.getStatus()) {
            bullet.update(fps);
            checkCollisions();

        }

        for (int row = 0; row < 5; row++) {
            if (enemies[row].getVisibility()) {
                enemies[row].update(fps);
                // Check collision with bullet
                checkCollisions();
            }

            if (enemies[row].getY() > screenY - enemies[row].getLength()) {
                enemies[row].dropDownAndReverse();
            }
            if (enemies[row].getY() < -enemies[row].getLength()) {
                enemies[row].dropDownAndReverse();
            }

            if(asteroids[row].getVisibility()){
                asteroids[row].update(fps);
                checkCollisions();
            }

        }

        if (lives <= 0) {
            playing = false;
        }

        if(numAsteroids == 0) {
            initAsteroids();
        }

    }


    private void checkCollisions() {
//        Stop spaceship leaving the screen
        if (spaceShip.getY() < 1 ||
                spaceShip.getY() + spaceShip.getLength() * 3 > screenY ||
                spaceShip.getX() < 1 ||
                spaceShip.getX() + spaceShip.getLength() > screenX
        ) {
            spaceShip.setMovementState(spaceShip.STOPPED);
        }

//        Set bullets inactive when off screen
        if (bullet.getImpactPointY() < 0 ||
                bullet.getImpactPointY() > screenY ||
                bullet.getImpactPointX() < 0 ||
                bullet.getImpactPointX() > screenX
        ) {
            bullet.setInactive();
        }

//        Handle bullets hitting enemies
        for (int i = 0; i < 5; i++) {
            if (bullet.getImpactPointX() >= enemies[i].getX() &&
                    bullet.getImpactPointX() <= enemies[i].getX() + enemies[i].getLength() &&
                    bullet.getImpactPointY() >= enemies[i].getY() &&
                    bullet.getImpactPointY() <= enemies[i].getY() + enemies[i].getHeight() &&
                    bullet.getStatus() &&
                    enemies[i].getVisibility()
            ) {
                bullet.setInactive();
                enemies[i].setInvisible();
                numEnemies--;
                score = score + 1;
            }
        }

//        Handle bullets hitting asteroids
        for(int i = 0; i < 5; i++){
            if(bullet.getImpactPointX() >= asteroids[i].getX() &&
                    bullet.getImpactPointX() <= asteroids[i].getX() + asteroids[i].getLength() &&
                    bullet.getImpactPointY() >= asteroids[i].getY() &&
                    bullet.getImpactPointY() <= asteroids[i].getY() + asteroids[i].getHeight() &&
                    bullet.getStatus() &&
                    asteroids[i].getVisibility()) {
                bullet.setInactive();
                asteroids[i].setInvisible();
                numAsteroids = numAsteroids -1;
                score = score + 1;
            }
        }

//        Handle enemies hitting player
        for (int i = 0; i < 5; i++) {
            if (enemies[i].isVisible &&
                    enemies[i].getX() >= spaceShip.getX() &&
                    enemies[i].getX() <= spaceShip.getX() + spaceShip.getLength() &&
                    enemies[i].getY() >= spaceShip.getY() &&
                    enemies[i].getY() <= spaceShip.getY() + spaceShip.getHeight()) {
                lives = lives - 1;
                enemies[i].setInvisible();
            }
        }
//        Set asteroids inactive when off screen
        for(int i = 0; i < 5; i++) {
            if(asteroids[i].isVisible &&
               asteroids[i].getX() <= 0){
                asteroids[i].setInvisible();
                numAsteroids = numAsteroids - 1;
            }
        }

//        Handle player hitting asteroid
        for (int i = 0; i < 5; i++) {
            if(asteroids[i].isVisible &&
                    asteroids[i].getX() >= spaceShip.getX() &&
                    asteroids[i].getX() <= spaceShip.getX() + spaceShip.getLength() &&
                    asteroids[i].getY() >= spaceShip.getY() &&
                    asteroids[i].getY() <= spaceShip.getY() + spaceShip.getHeight()) {
                lives = lives -1;
                asteroids[i].setInvisible();
                numAsteroids = numAsteroids - 1;
            }
        }

    }


    private void draw() {
        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();
            canvas.drawBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.gameplay_background), 0, 0, paint);
            canvas.drawBitmap(spaceShip.getBitmap(), spaceShip.getX(), spaceShip.getY(), paint);
            paint.setColor(Color.argb(255, 249, 129, 0));
            paint.setTextSize(52);
            canvas.rotate(90);
            canvas.drawText("Score: " + score + "   Lives: " +
                    lives, 50, -50, paint);
            canvas.rotate(-90);
            // arrow buttons
            canvas.drawBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_up), 275, 1500, paint);
            canvas.drawBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_down), 25, 1500, paint);
            canvas.drawBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_left), 150, 1375, paint);
            canvas.drawBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_right), 150, 1625, paint);

            if (bullet.getStatus()) {
                canvas.drawRect(bullet.getRect(), paint);
            }

            for (int row = 0; row < 5; row++) {
                if (enemies[row].getVisibility()) {
                    canvas.drawBitmap(enemies[row].getBitmap(), enemies[row].getX(), enemies[row].getY(), paint);
                }
                if (asteroids[row].getVisibility()) {
                    canvas.drawBitmap(asteroids[row].getBitmap(), asteroids[row].getX(), asteroids[row].getY(), paint);
                }
            }
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int touchX = (int) motionEvent.getX();
        int touchY = (int) motionEvent.getY();

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                paused = false;

                if ((motionEvent.getX() > 150) && (motionEvent.getX() < 250)) {
                    if ((motionEvent.getY() > 1375) && (motionEvent.getY() < 1475) /*&& (spaceShip.getY() > 0)*/) {
                        spaceShip.setMovementState(spaceShip.UP);
                        bullet.shoot(spaceShip.getX() + spaceShip.getLength() / 2, spaceShip.getY() + spaceShip.getLength() / 2, bullet.UP);
                    } else if ((motionEvent.getY() > 1625) && (motionEvent.getY() < 1725)) {
                        spaceShip.setMovementState(spaceShip.DOWN);
                        bullet.shoot(spaceShip.getX() + spaceShip.getLength() / 2, spaceShip.getY() + spaceShip.getLength() / 2, bullet.DOWN);
                    }
                }

                if ((motionEvent.getY() > 1500) && (motionEvent.getY() < 1600)) {
                    if ((motionEvent.getX() > 275) && (motionEvent.getX() < 375)) {
                        spaceShip.setMovementState(spaceShip.RIGHT);
                        bullet.shoot(spaceShip.getX() + spaceShip.getLength(), spaceShip.getY() + spaceShip.getHeight() / 2, bullet.RIGHT);
                    } else if ((motionEvent.getX() > 25) && (motionEvent.getX() < 125)/* && (spaceShip.getX() > 0)*/) {
                        spaceShip.setMovementState(spaceShip.LEFT);
                        bullet.shoot(spaceShip.getX() + spaceShip.getLength(), spaceShip.getY() + spaceShip.getHeight() / 2, bullet.LEFT);
                    }


                }

                break;

            case MotionEvent.ACTION_UP:

                //   if(motionEvent.getY() > screenY - screenY / 10) {
                spaceShip.setMovementState(spaceShip.STOPPED);
                //   }
                break;
        }
        return true;
    }
}
