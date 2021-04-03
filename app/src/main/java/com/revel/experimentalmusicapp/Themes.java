package com.revel.experimentalmusicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Themes extends AppCompatActivity implements View.OnClickListener {
    ImageView im1, im2, im3, im4, im5, im6, im7, im8, im9, im10;
    Button select;
    int flag = 1;

    @Override
    public void onBackPressed() {
        flag = 0;
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        DuoDrawer.appClosed = true;
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.themes);

        DuoDrawer.appClosed = false;
        DuoDrawer.z = 0;

        im1 = findViewById(R.id.im1);
        im2 = findViewById(R.id.im2);
        im3 = findViewById(R.id.im3);
        im4 = findViewById(R.id.im4);
        im5 = findViewById(R.id.im5);
        im6 = findViewById(R.id.im6);
        im7 = findViewById(R.id.im7);
        im8 = findViewById(R.id.im8);
        im9 = findViewById(R.id.im9);
        im10 = findViewById(R.id.im10);
        select = findViewById(R.id.personal);

        im1.setOnClickListener(this);
        im2.setOnClickListener(this);
        im3.setOnClickListener(this);
        im4.setOnClickListener(this);
        im5.setOnClickListener(this);
        im6.setOnClickListener(this);
        im7.setOnClickListener(this);
        im8.setOnClickListener(this);
        im9.setOnClickListener(this);
        im10.setOnClickListener(this);
        im1.setBackgroundColor(Color.parseColor("#776D6D"));
        im2.setBackgroundColor(Color.parseColor("#776D6D"));
        im3.setBackgroundColor(Color.parseColor("#776D6D"));
        im4.setBackgroundColor(Color.parseColor("#776D6D"));
        im5.setBackgroundColor(Color.parseColor("#776D6D"));
        im6.setBackgroundColor(Color.parseColor("#776D6D"));
        im7.setBackgroundColor(Color.parseColor("#776D6D"));
        im8.setBackgroundColor(Color.parseColor("#776D6D"));
        im9.setBackgroundColor(Color.parseColor("#776D6D"));
        im10.setBackgroundColor(Color.parseColor("#776D6D"));


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Themes.this, SecondActivity.class);
                intent.putExtra("requestCode", 1);
                intent.putExtra("flagTheme", flag);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im1:
                flag = 1;
                im1.setBackgroundColor(Color.parseColor("#CCAD25"));
                im2.setBackgroundColor(Color.parseColor("#776D6D"));
                im3.setBackgroundColor(Color.parseColor("#776D6D"));
                im4.setBackgroundColor(Color.parseColor("#776D6D"));
                im5.setBackgroundColor(Color.parseColor("#776D6D"));
                im6.setBackgroundColor(Color.parseColor("#776D6D"));
                im7.setBackgroundColor(Color.parseColor("#776D6D"));
                im8.setBackgroundColor(Color.parseColor("#776D6D"));
                im9.setBackgroundColor(Color.parseColor("#776D6D"));
                im10.setBackgroundColor(Color.parseColor("#776D6D"));
                break;
            case R.id.im2:
                flag = 2;
                im2.setBackgroundColor(Color.parseColor("#CCAD25"));
                im1.setBackgroundColor(Color.parseColor("#776D6D"));
                im3.setBackgroundColor(Color.parseColor("#776D6D"));
                im4.setBackgroundColor(Color.parseColor("#776D6D"));
                im5.setBackgroundColor(Color.parseColor("#776D6D"));
                im6.setBackgroundColor(Color.parseColor("#776D6D"));
                im7.setBackgroundColor(Color.parseColor("#776D6D"));
                im8.setBackgroundColor(Color.parseColor("#776D6D"));
                im9.setBackgroundColor(Color.parseColor("#776D6D"));
                im10.setBackgroundColor(Color.parseColor("#776D6D"));
                break;
            case R.id.im3:
                flag = 3;
                im3.setBackgroundColor(Color.parseColor("#CCAD25"));
                im1.setBackgroundColor(Color.parseColor("#776D6D"));
                im2.setBackgroundColor(Color.parseColor("#776D6D"));
                im4.setBackgroundColor(Color.parseColor("#776D6D"));
                im5.setBackgroundColor(Color.parseColor("#776D6D"));
                im6.setBackgroundColor(Color.parseColor("#776D6D"));
                im7.setBackgroundColor(Color.parseColor("#776D6D"));
                im8.setBackgroundColor(Color.parseColor("#776D6D"));
                im9.setBackgroundColor(Color.parseColor("#776D6D"));
                im10.setBackgroundColor(Color.parseColor("#776D6D"));
                break;
            case R.id.im4:
                flag = 4;
                im4.setBackgroundColor(Color.parseColor("#CCAD25"));
                im2.setBackgroundColor(Color.parseColor("#776D6D"));
                im3.setBackgroundColor(Color.parseColor("#776D6D"));
                im1.setBackgroundColor(Color.parseColor("#776D6D"));
                im5.setBackgroundColor(Color.parseColor("#776D6D"));
                im6.setBackgroundColor(Color.parseColor("#776D6D"));
                im7.setBackgroundColor(Color.parseColor("#776D6D"));
                im8.setBackgroundColor(Color.parseColor("#776D6D"));
                im9.setBackgroundColor(Color.parseColor("#776D6D"));
                im10.setBackgroundColor(Color.parseColor("#776D6D"));
                break;
            case R.id.im5:
                flag = 5;
                im5.setBackgroundColor(Color.parseColor("#CCAD25"));
                im2.setBackgroundColor(Color.parseColor("#776D6D"));
                im3.setBackgroundColor(Color.parseColor("#776D6D"));
                im4.setBackgroundColor(Color.parseColor("#776D6D"));
                im1.setBackgroundColor(Color.parseColor("#776D6D"));
                im6.setBackgroundColor(Color.parseColor("#776D6D"));
                im7.setBackgroundColor(Color.parseColor("#776D6D"));
                im8.setBackgroundColor(Color.parseColor("#776D6D"));
                im9.setBackgroundColor(Color.parseColor("#776D6D"));
                im10.setBackgroundColor(Color.parseColor("#776D6D"));
                break;
            case R.id.im6:
                flag = 6;
                im6.setBackgroundColor(Color.parseColor("#CCAD25"));
                im2.setBackgroundColor(Color.parseColor("#776D6D"));
                im3.setBackgroundColor(Color.parseColor("#776D6D"));
                im4.setBackgroundColor(Color.parseColor("#776D6D"));
                im5.setBackgroundColor(Color.parseColor("#776D6D"));
                im1.setBackgroundColor(Color.parseColor("#776D6D"));
                im7.setBackgroundColor(Color.parseColor("#776D6D"));
                im8.setBackgroundColor(Color.parseColor("#776D6D"));
                im9.setBackgroundColor(Color.parseColor("#776D6D"));
                im10.setBackgroundColor(Color.parseColor("#776D6D"));
                break;
            case R.id.im7:
                flag = 7;
                im7.setBackgroundColor(Color.parseColor("#CCAD25"));
                im2.setBackgroundColor(Color.parseColor("#776D6D"));
                im3.setBackgroundColor(Color.parseColor("#776D6D"));
                im4.setBackgroundColor(Color.parseColor("#776D6D"));
                im5.setBackgroundColor(Color.parseColor("#776D6D"));
                im6.setBackgroundColor(Color.parseColor("#776D6D"));
                im1.setBackgroundColor(Color.parseColor("#776D6D"));
                im8.setBackgroundColor(Color.parseColor("#776D6D"));
                im9.setBackgroundColor(Color.parseColor("#776D6D"));
                im10.setBackgroundColor(Color.parseColor("#776D6D"));
                break;
            case R.id.im8:
                flag = 8;
                im8.setBackgroundColor(Color.parseColor("#CCAD25"));
                im2.setBackgroundColor(Color.parseColor("#776D6D"));
                im3.setBackgroundColor(Color.parseColor("#776D6D"));
                im4.setBackgroundColor(Color.parseColor("#776D6D"));
                im5.setBackgroundColor(Color.parseColor("#776D6D"));
                im6.setBackgroundColor(Color.parseColor("#776D6D"));
                im7.setBackgroundColor(Color.parseColor("#776D6D"));
                im1.setBackgroundColor(Color.parseColor("#776D6D"));
                im9.setBackgroundColor(Color.parseColor("#776D6D"));
                im10.setBackgroundColor(Color.parseColor("#776D6D"));
                break;
            case R.id.im9:
                flag = 9;
                im9.setBackgroundColor(Color.parseColor("#CCAD25"));
                im2.setBackgroundColor(Color.parseColor("#776D6D"));
                im3.setBackgroundColor(Color.parseColor("#776D6D"));
                im4.setBackgroundColor(Color.parseColor("#776D6D"));
                im5.setBackgroundColor(Color.parseColor("#776D6D"));
                im6.setBackgroundColor(Color.parseColor("#776D6D"));
                im7.setBackgroundColor(Color.parseColor("#776D6D"));
                im8.setBackgroundColor(Color.parseColor("#776D6D"));
                im1.setBackgroundColor(Color.parseColor("#776D6D"));
                im10.setBackgroundColor(Color.parseColor("#776D6D"));
                break;
            case R.id.im10:
                flag = 10;
                im10.setBackgroundColor(Color.parseColor("#CCAD25"));
                im2.setBackgroundColor(Color.parseColor("#776D6D"));
                im3.setBackgroundColor(Color.parseColor("#776D6D"));
                im4.setBackgroundColor(Color.parseColor("#776D6D"));
                im5.setBackgroundColor(Color.parseColor("#776D6D"));
                im6.setBackgroundColor(Color.parseColor("#776D6D"));
                im7.setBackgroundColor(Color.parseColor("#776D6D"));
                im8.setBackgroundColor(Color.parseColor("#776D6D"));
                im9.setBackgroundColor(Color.parseColor("#776D6D"));
                im1.setBackgroundColor(Color.parseColor("#776D6D"));
                break;

        }
    }
}
