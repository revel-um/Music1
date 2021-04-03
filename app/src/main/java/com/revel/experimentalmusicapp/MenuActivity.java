package com.revel.experimentalmusicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.revel.experimentalmusicapp.MyService.position;
import static com.revel.experimentalmusicapp.Splash.arrayListData;
import static com.revel.experimentalmusicapp.Splash.arrayListTitle;

public class MenuActivity extends AppCompatActivity {
    int position;

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
        setContentView(R.layout.activity_menu);
        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("position", -1);
        ListView lv = findViewById(R.id.list);
        ArrayList options = new ArrayList();
        options.add("Share");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, options);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Uri curr = Uri.parse(arrayListData.get(position));
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("music/mp3");
                        intent.putExtra(Intent.EXTRA_STREAM, curr);
                        startActivity(Intent.createChooser(intent, "" + arrayListTitle.get(position)));
                        break;
                }
            }
        });
    }
}
