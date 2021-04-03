package com.revel.experimentalmusicapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashSet;

import static com.revel.experimentalmusicapp.Splash.arrayListAlbumImage;
import static com.revel.experimentalmusicapp.Splash.arrayListImagePath;
import static com.revel.experimentalmusicapp.Splash.num;

public class AlbumActivity extends AppCompatActivity {
    public static ArrayList<String> arrayAlbum;
    HashSet hashSet;
    String currImage;

    @Override
    protected void onPause() {
        DuoDrawer.appClosed = true;
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        DuoDrawer.appClosed = false;
        DuoDrawer.z = 0;

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        final GridView grid = findViewById(R.id.grid);

        arrayAlbum = new ArrayList<>();
        hashSet = new HashSet();

        hashSet.addAll(arrayListAlbumImage);
        arrayAlbum.addAll(hashSet);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for (int k = 0; k < num; k++) {
                    if (arrayListAlbumImage.get(k).equals(arrayAlbum.get(position))) {
                        currImage = arrayListImagePath.get(k);
                    }
                }

                Intent intent = new Intent(AlbumActivity.this, AlbumPlay.class);
                intent.putExtra("albumPosition", position);
                intent.putExtra("currImage", currImage);
                startActivity(intent);
            }
        });

        ArrayAdapter adapter = new MyArrayAdapter(this, android.R.layout.simple_list_item_1, arrayAlbum);
        grid.setAdapter(adapter);
    }

    private class MyArrayAdapter extends ArrayAdapter {
        public MyArrayAdapter(AlbumActivity albumActivity, int simple_list_item_1, ArrayList images) {
            super(albumActivity, simple_list_item_1, images);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.custom_grid, parent, false);
            ImageView im = v.findViewById(R.id.grid_image);
            TextView tv = v.findViewById(R.id.grid_text);
            for (int k = 0; k < num; k++) {
                currImage = arrayListImagePath.get(k);
                if (arrayListAlbumImage.get(k).equals(arrayAlbum.get(position))) {
                    if (currImage != null)
                        Glide.with(AlbumActivity.this).load(currImage).into(im);
                    else
                        Glide.with(AlbumActivity.this).load(R.drawable.albums).into(im);
                }
            }
            tv.setText(arrayAlbum.get(position));
            return v;
        }
    }
}
