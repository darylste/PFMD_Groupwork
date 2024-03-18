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
    Spaceship spaceShip;
    private Bullet bullet;

    public SpaceGameView(Context context, int x, int y) {
        super(context);
        this.context = context;

        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        initLevel();
    }



    private void initLevel(){
        spaceShip = new Spaceship(context, screenX, screenY);
        bullet = new Bullet(screenY,screenX);
    }


    @Override
    public void run() {
        while (playing) {
            score = 10;

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
        }

        checkCollisions();
    }


    private void checkCollisions(){
        if (spaceShip.getX() > screenX - spaceShip.getLength())
            spaceShip.setX(0);
        if (spaceShip.getX() < 0 + spaceShip.getLength())
            spaceShip.setX(screenX);

        if (spaceShip.getY() > screenY - spaceShip.getLength())
            spaceShip.setY(0);
        if (spaceShip.getY() < 0 + spaceShip.getLength())
            spaceShip.setY(screenY);

        if(bullet.getImpactPointY() < 0)
            bullet.setInactive();
        if(bullet.getImpactPointY() > screenY)
            bullet.setInactive();

        if(bullet.getImpactPointX() < 0)
            bullet.setInactive();
        if(bullet.getImpactPointX() > screenX)
            bullet.setInactive();

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
