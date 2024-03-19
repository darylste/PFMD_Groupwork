package com.darylstensland.assessment;

import android.content.Context;   // The import statements we will need
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import java.util.Random;

public class Enemy {

    RectF rect;
    Random generator;
    private Bitmap bitmap;
    private float length;
    private float height;
    private float x;
    private float y;
    private float shipSpeed;
    public final int LEFT = 1;
    public final int RIGHT = 2;
    private int shipMoving = RIGHT;
    boolean isVisible;


    public Enemy(Context context, int row, int screenX, int screenY) {

// Initialize a blank RectF

        rect = new RectF();

//Set the length
//Set the height

        length = screenX / 20;
        height = screenY / 20;

//set visible to true

        isVisible = true;

//set some space between the images

        int padding = screenX / 25;

//set the position of the image

        y = row * (screenY / 6) ;
        x = screenX - (height + padding );


// Initialize the bitmap

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.alien);

// stretch the bitmap to a size appropriate for the screen resolution
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);

// How fast is the invader in pixels per second
        shipSpeed = 200;


    }


    public void update(long fps) {
        if (shipMoving == LEFT) {
            y = y - shipSpeed / fps;

        }

        if (shipMoving == RIGHT) {
            y = y + shipSpeed / fps;
        }


        // Update rect which is used to detect hits
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;

    }

    public void dropDownAndReverse() {
        if (shipMoving == LEFT) {
            shipMoving = RIGHT;
        } else {
            shipMoving = LEFT;
        }

        x = x - 100;
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

// public Bitmap getBitmap2(){
//     return bitmap2;
//  }

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
