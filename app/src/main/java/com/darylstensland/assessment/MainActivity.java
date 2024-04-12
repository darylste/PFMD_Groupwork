package com.darylstensland.assessment;

import android.os.Bundle;
import android.graphics.Point;
import android.view.Display;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SpaceGameView spaceGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the screen dimensions
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        // Initialize the game view
        spaceGameView = new SpaceGameView(this);

        // If SpaceGameView manages its dimensions internally or via a setup method
        spaceGameView.setupGame(size.x, size.y); // Ensure this method exists and is public in SpaceGameView

        setContentView(spaceGameView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        spaceGameView.resume(); // Resume the game
    }

    @Override
    protected void onPause() {
        super.onPause();
        spaceGameView.pause(); // Pause the game
    }
}
