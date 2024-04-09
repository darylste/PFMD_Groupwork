package com.darylstensland.assessment;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SpaceGameView extends SurfaceView implements Runnable, Spaceship.GameOverListener {
    private Thread gameThread = null;
    private SurfaceHolder ourHolder;
    private volatile boolean playing;
    private boolean paused = true;
    private Paint paint;
    private long fps;
    private int screenX;
    private int screenY;
    public int score = 0;
    private int lives = 3;
    private Spaceship spaceShip;

    public SpaceGameView(Context context, int x, int y) {
        super(context);

        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        initLevel();
    }

    private void initLevel(){
        // Note: Ensure that this class implements Spaceship.GameOverListener interface correctly
        spaceShip = new Spaceship(getContext(), screenX, screenY, this); // Passing 'this' as GameOverListener
    }

    @Override
    public void run() {
        while (playing) {
            long startFrameTime = System.currentTimeMillis();

            if(!paused){
                update();
            }

            draw();

            long timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    private void update(){
        spaceShip.update(fps);
    }

    private void draw() {
        if (ourHolder.getSurface().isValid()) {
            Canvas canvas = ourHolder.lockCanvas();

            // Clear the screen with a solid color
            canvas.drawColor(Color.BLACK);

            // Draw the background
            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.background), 0, 0, paint);

            // Draw the spaceship
            canvas.drawBitmap(spaceShip.getBitmap(), spaceShip.getX(), spaceShip.getY(), paint);

            // Draw the score and lives
            paint.setColor(Color.argb(255, 249, 129, 0));
            paint.setTextSize(40);
            canvas.drawText("Score: " + score + " Lives: " + lives, 10, 50, paint);

            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public void onGameOver() {
        // Handle the game over logic here
        paused = true;
        // Perform additional actions like resetting the game or showing a game over screen
    }

    public void pause() {
        playing = false;
        try {
            if (gameThread != null) gameThread.join();
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
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                paused = false;
                // Determine direction based on touch position
                // Existing movement logic...
                break;
            case MotionEvent.ACTION_UP:
                spaceShip.setMovementState(Spaceship.STOPPED);
                break;
        }
        return true;
    }
}
