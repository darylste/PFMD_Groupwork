package com.darylstensland.assessment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameOver extends SurfaceView implements SurfaceHolder.Callback {
    private Paint paint;
    private Paint gameOverPaint;
    private final int score;

    public GameOver(Context context, int score) {
        super(context);
        this.score = score;

        // Initialize paint for drawing text
        paint = new Paint();
        paint.setTextSize(75);
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.DEFAULT);

        gameOverPaint = new Paint();
        gameOverPaint.setTextSize(150);
        gameOverPaint.setColor(Color.WHITE);
        gameOverPaint.setTypeface(Typeface.DEFAULT_BOLD);

        // Ensure we get surface creation and change events
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Draw once the surface is ready
        Canvas canvas = holder.lockCanvas();
        if (canvas != null) {
            draw(canvas);  // Use the provided canvas
            holder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Handle changes (e.g., orientation, size change)
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Clean up if necessary
    }

    public void onDraw(Canvas canvas) {
        // Fill canvas with black background
        canvas.drawColor(Color.BLACK);

        // Draw  background image
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.game_over_background), 0, 0, paint);
        // Rotate Canvus
        canvas.rotate(90);

        // Draw "Game Over" text
        canvas.drawText("Game Over...", 650, -700, gameOverPaint);

        // Display score
        canvas.drawText("You Scored: " + score, 775, -500, paint);

        // Draw button image
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.replay_btn), 800, -400, paint);
        canvas.rotate(-90);


    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if ((motionEvent.getY() > -500) &&
            (motionEvent.getY() > -450) &&
            (motionEvent.getX() > 775) &&
            (motionEvent.getX() > 950)) {
           Activity activity = (Activity) getContext();
            Intent intent = new Intent(activity, MainActivity.class);
            activity.finish();
            activity.startActivity(intent);

        }
        return super.onTouchEvent(motionEvent);
    }
}
