package com.darylstensland.assessment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import java.util.Random;


public class Asteroid {
    Random random = new Random();
    RectF rect;
    private Bitmap bitmap;
    private float length;
    private float height;
    private float x;
    private float y;
    private float speed;
    boolean isVisible;


    public Asteroid(Context context, int row, int screenX, int screenY) {
        rect = new RectF();
        length = screenX / 20;
        height = screenY / 20;
        isVisible = true;
        int padding = screenX / 25;
        y = random.nextInt(screenY - (int) height);
        x = screenX + random.nextInt(500);

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid);
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);

        speed = 100;
    }

    public void update(long fps) {
        x = x - speed / fps;

        // Update rect which is used to detect hits
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;

    }

    public void setInvisible() {
        isVisible = false;
    }

    public void setVisible() {
        isVisible = true;
    }

    public boolean getVisibility() {
        return isVisible;
    }

    public RectF getRect() {
        return rect;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setY() {
        this.y = 0;
    }


    public float getLength() {
        return length;
    }

    public float getHeight() {
        return height;
    }
}
