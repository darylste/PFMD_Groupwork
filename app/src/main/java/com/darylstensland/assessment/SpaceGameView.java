package com.darylstensland.assessment;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Space;

import java.io.IOException;


public class SpaceGameView extends SurfaceView implements Runnable{
    private Context context;
    private Thread gameThread = null;
    private SurfaceHolder ourHolder;
    private volatile boolean playing;
    private boolean paused = true;
    private Canvas canvas;
    private Paint paint;
    private long fps;
    private long timeThisFrame;
    private int screenX;
    private int screenY;
    public int score = 0;
    private int lives = 3;
    private Spaceship spaceShip;
    private Bullet bullet;

    private Enemy[] enemies = new Enemy[5];
    private int numEnemies = 0;

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
        numEnemies = 0;
        for (int row = 0; row < 5; row++) {
            enemies[row] = new Enemy(context, row, screenX, screenY);
            numEnemies++;
        }
    }


    @Override
    public void run() {
        while (playing) {
            long startFrameTime = System.currentTimeMillis();

            if(!paused){
                update();
            }

            draw();

            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }

        }

    }



    private void update(){
        spaceShip.update(fps);

        if(bullet.getStatus()) {
            bullet.update(fps);
            checkCollisions();

        }

        for (int row = 0; row < 5; row++) {
            if (enemies[row].getVisibility()) {
                enemies[row].update(fps);
                // Check collision with bullet
                checkCollisions();

            }

            if(enemies[row].getY() > screenY - enemies[row].getLength()) {
                enemies[row].dropDownAndReverse();
            }
            if (enemies[row].getY() < -enemies[row].getLength()) {
                enemies[row].dropDownAndReverse();
            }

        }

        if(lives <= 0) {
            playing = false;
        }

    }


    private void checkCollisions() {
//        if (spaceShip.getX() > screenX - spaceShip.getLength())
//            spaceShip.setX(0);
//        if (spaceShip.getX() < 0 + spaceShip.getLength())
//            spaceShip.setX(screenX);
//
//        if (spaceShip.getY() > screenY - spaceShip.getLength())
//            spaceShip.setY(0);
//        if (spaceShip.getY() < 0 + spaceShip.getLength())
//            spaceShip.setY(screenY);
//
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

//        Handle enemies hitting player
        for (int i = 0; i < 5; i++) {
            if(enemies[i].isVisible &&
               enemies[i].getX() >= spaceShip.getX() &&
               enemies[i].getX() <= spaceShip.getX() + spaceShip.getHeight() &&
               enemies[i].getY() >= spaceShip.getY() &&
               enemies[i].getY() <= spaceShip.getY() + spaceShip.getLength()){
                lives = lives-1;
                enemies[i].setInvisible();
            }
        }

    }




    private void draw(){
        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();


            canvas.drawBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.background), 0, 0, paint);
            canvas.drawBitmap(spaceShip.getBitmap(),spaceShip.getX(), spaceShip.getY(), paint);
            paint.setColor(Color.argb(255,  249, 129, 0));
            paint.setTextSize(40);
            canvas.drawText("Score: " + score + "   Lives: " +
                    lives, 10,50, paint);

            // arrow buttons
            canvas.drawBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_up), 275, 1500, paint);
            canvas.drawBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_down), 25, 1500, paint);
            canvas.drawBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_left), 150, 1375, paint);
            canvas.drawBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_right), 150, 1625, paint);

            if(bullet.getStatus())
                canvas.drawRect(bullet.getRect(), paint);

            for (int row = 0; row < 5; row++) {
                if (enemies[row].getVisibility()) {
                    canvas.drawBitmap(enemies[row].getBitmap(), enemies[row].getX(), enemies[row].getY(), paint);
                    numEnemies ++;
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
        int touchX = (int)motionEvent.getX();
        int touchY = (int)motionEvent.getY();

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                paused = false;

//                if(motionEvent.getY() > screenY - screenY / 2) {
//                    if (motionEvent.getX() > screenX / 2) {
//                        spaceShip.setMovementState(spaceShip.RIGHT);
//                    } else {
//                        spaceShip.setMovementState(spaceShip.LEFT);
//                    }
//
//
//                }
//
//                if(motionEvent.getY() < screenY - screenY / 2) {
//                    if (motionEvent.getX() > screenX / 2) {
//                        spaceShip.setMovementState(spaceShip.UP);
//                    } else {
//                        spaceShip.setMovementState(spaceShip.DOWN);
//                    }
//
//
//                }
                if ((motionEvent.getX() > 150) && (motionEvent.getX() < 250)) {
                    if ((motionEvent.getY() > 1375) && (motionEvent.getY() < 1475) /*&& (spaceShip.getY() > 0)*/) {
                        spaceShip.setMovementState(spaceShip.UP);
                        bullet.shoot(spaceShip.getX() + spaceShip.getLength()/2, spaceShip.getY() + spaceShip.getLength()/2, bullet.UP);
                    } else if ((motionEvent.getY() > 1625) && (motionEvent.getY() < 1725)) {
                        spaceShip.setMovementState(spaceShip.DOWN);
                        bullet.shoot(spaceShip.getX() + spaceShip.getLength()/2, spaceShip.getY() + spaceShip.getLength()/2, bullet.DOWN);
                    }


                }

                if ((motionEvent.getY() > 1500) && (motionEvent.getY() < 1600)) {
                    if ((motionEvent.getX() > 275) && (motionEvent.getX() < 375)) {
                        spaceShip.setMovementState(spaceShip.RIGHT);
                        bullet.shoot(spaceShip.getX() + spaceShip.getHeight()/2, spaceShip.getY() + spaceShip.getLength()/2, bullet.RIGHT);
                    } else if ((motionEvent.getX() > 25) && (motionEvent.getX() < 125)/* && (spaceShip.getX() > 0)*/) {
                        spaceShip.setMovementState(spaceShip.LEFT);
                        bullet.shoot(spaceShip.getX() + spaceShip.getHeight()/2, spaceShip.getY() + spaceShip.getLength()/2, bullet.LEFT);
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
