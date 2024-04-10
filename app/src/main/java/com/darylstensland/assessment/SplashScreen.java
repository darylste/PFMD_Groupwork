package com.darylstensland.assessment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashScreen extends MainActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
            // Add SYSTEM_UI_FLAG_FULLSCREEN
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // Add this flag for colored status bar
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            finish();
        }, 3000);

        // Create a LinearLayout as the root layout
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        // Load the background image as a Bitmap
        Bitmap backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.image);

        // Set the background image
        rootLayout.setBackground(new BitmapDrawable(getResources(), backgroundImage));

        // Add TextViews
        TextView spaceTextView = createTextView("Space", 60, Typeface.BOLD_ITALIC, Gravity.START, 500, 70, 0);
        TextView explorerTextView = createTextView("Explorers", 60, Typeface.BOLD_ITALIC, Gravity.START, 350, 0, 0);

        // Add TextViews to the root layout
        rootLayout.addView(spaceTextView);
        rootLayout.addView(explorerTextView);

        // Add ImageView for the rotate image
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.rotate);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(400, 300); // Set width and height of the image
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL; // Align image to center horizontally
        layoutParams.setMargins(0, 190, 0, 20); // Add margins
        imageView.setLayoutParams(layoutParams);
        rootLayout.addView(imageView);

        // Adding  TextView for Rotate Device
        TextView textView = createTextView("Rotate Device", 25, Typeface.BOLD, Gravity.CENTER, 0, 0, 20);
        rootLayout.addView(textView);

        // Adding ProgressBar
        ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams progressBarParams = new LinearLayout.LayoutParams(550, 100);
        // Increase width and height of the progress bar
        progressBarParams.gravity = Gravity.CENTER_HORIZONTAL;
        // Align progress bar to center horizontally
        progressBarParams.topMargin = 10;
        // Set top margin
        progressBar.setLayoutParams(progressBarParams);
        progressBar.setIndeterminate(true);
        // Set indeterminate mode
        progressBar.setMax(100);
        // Set maximum progress to 100
        progressBar.setProgress(0);
        // Set initial progress to 0
        rootLayout.addView(progressBar);

        // Add TextView for the bottom project description
        TextView bottomTextView = createTextView("A project by Aurélien Terredes, Bibika Ghimire, Daryl Stensland, and Summer Rhoda.", 15, Typeface.NORMAL, Gravity.CENTER_HORIZONTAL, 0, 740, 0);
        rootLayout.addView(bottomTextView);

        // Set the content view to the root layout
        setContentView(rootLayout);
    }
    // Method to create a TextView with specified properties
    private TextView createTextView(String text, int textSize, int typeface, int gravity, int paddingLeft, int paddingTop, int paddingBottom) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(textSize);
        textView.setTypeface(null, typeface);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(gravity);
        textView.setPadding(paddingLeft, paddingTop, 0, paddingBottom);
        return textView;
    }
}
