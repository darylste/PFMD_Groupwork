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

    }




    private void draw(){
        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();


            canvas.drawBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.background), 0, 0, paint);
            canvas.drawBitmap(spaceShip.getBitmap(), spaceShip.getX(), spaceShip.getY(), paint);
            paint.setColor(Color.argb(255,  249, 129, 0));
            paint.setTextSize(40);
            canvas.drawText("Score: " + score + "   Lives: " +
                    lives, 10,50, paint);
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

                if(motionEvent.getY() > screenY - screenY / 2) {
                    if (motionEvent.getX() > screenX / 2) {
                        spaceShip.setMovementState(spaceShip.RIGHT);
                    } else {
                        spaceShip.setMovementState(spaceShip.LEFT);
                    }


                }

                if(motionEvent.getY() < screenY - screenY / 2) {
                    if (motionEvent.getX() > screenX / 2) {
                        spaceShip.setMovementState(spaceShip.UP);
                    } else {
                        spaceShip.setMovementState(spaceShip.DOWN);
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
