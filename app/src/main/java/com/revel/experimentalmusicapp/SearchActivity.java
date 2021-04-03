package com.revel.experimentalmusicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import static com.revel.experimentalmusicapp.DuoDrawer.sizeSong;
import static com.revel.experimentalmusicapp.Splash.arrayListTitle;

public class SearchActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

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
        setContentView(R.layout.activity_search);
        final AutoCompleteTextView actv = findViewById(R.id.actv);
        actv.setThreshold(0);
        actv.setDropDownBackgroundResource(R.color.colorPrimaryDark);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, arrayListTitle);
        actv.setAdapter(adapter);
        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int i;

                String title = actv.getText().toString();
                for (i = 0; i < sizeSong; i++) {
                    if (arrayListTitle.get(i).equals(title)) {
                        position = i;
                    }
                }
                Intent intent = new Intent(SearchActivity.this, MyService.class);
                stopService(intent);
                intent.putExtra("position", position);
                startService(intent);
                finish();
            }
        });
    }

}
