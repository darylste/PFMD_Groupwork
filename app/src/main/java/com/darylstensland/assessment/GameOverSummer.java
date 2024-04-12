package com.darylstensland.assessment;

import android.content.Context;

import android.graphics.Canvas;

import android.graphics.Color;

import android.graphics.Paint;

import android.graphics.Typeface;

import android.view.MotionEvent;

import android.view.SurfaceView;

public class GameOver extends SurfaceView {

    private Paint paint;

    private final int score;

    public GameOver(Context context, int score) {

        super(context);

        this.score = score;

// Initialize paint for drawing text

        paint = new Paint();

        paint.setTextSize(100);

        paint.setColor(Color.RED);

        paint.setTypeface(Typeface.DEFAULT_BOLD);

    }

    @Override

    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

// Fill canvas with a black background

        canvas.drawColor(Color.BLACK);

// Draw "Game Over" text

        canvas.drawText("Game Over", 100, 200, paint);

// Display score

        canvas.drawText("Score: " + score, 100, 400, paint);

// Draw instructions to restart or exit

        paint.setTextSize(50);

        canvas.drawText("Tap anywhere to restart", 100, 600, paint);

    }

    @Override

    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

// Here, implement restarting the game or going back to the main menu

// Possibly calling a method in the main game activity or view

// For simplicity, click the restart action

            System.out.println("Restarting game...");

            return true;

        }

        return super.onTouchEvent(event);

    }

}
