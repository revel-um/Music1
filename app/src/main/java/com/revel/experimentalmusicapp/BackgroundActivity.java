package com.revel.experimentalmusicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rtugeek.android.colorseekbar.ColorSeekBar;

import java.io.File;

public class BackgroundActivity extends AppCompatActivity {
    ConstraintLayout constraintLayout;
    int color;
    Button set;

    @Override
    protected void onPause() {
        DuoDrawer.appClosed = true;
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DuoDrawer.appClosed = false;
        DuoDrawer.z = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        ColorSeekBar colorSeekBar = findViewById(R.id.colorSeekBar);
        colorSeekBar.setMaxPosition(100000);
        colorSeekBar.setColorSeeds(R.array.material_colors);
        colorSeekBar.setBarHeight(10);
        colorSeekBar.setThumbHeight(30);
        constraintLayout = findViewById(R.id.cl);
        set = findViewById(R.id.set);
        colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
            @Override
            public void onColorChangeListener(int colorBarPosition, int alphaBarPosition, int color) {
                BackgroundActivity.this.color = color;
                constraintLayout.setBackgroundColor(color);
            }
        });
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(color, new Intent());
                finish();
            }
        });
    }
}
