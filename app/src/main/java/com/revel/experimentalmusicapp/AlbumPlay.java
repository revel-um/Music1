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

import static com.revel.experimentalmusicapp.AlbumActivity.arrayAlbum;
import static com.revel.experimentalmusicapp.DuoDrawer.sizeSong;
import static com.revel.experimentalmusicapp.Splash.arrayListData;
import static com.revel.experimentalmusicapp.Splash.arrayListSongImage;
import static com.revel.experimentalmusicapp.Splash.arrayListTitle;

public class AlbumPlay extends AppCompatActivity {
    GridView gridView;
    int albumPosition;
    public static ArrayList<String> songsDataFromAlbum, songsTitleFromAlbum;
    String currImage;
    public static int posi;
    public static boolean playingAlbums = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_play);
        gridView = findViewById(R.id.grid_play);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        songsDataFromAlbum = new ArrayList<>();
        songsTitleFromAlbum = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        albumPosition = bundle.getInt("albumPosition");
        currImage = bundle.getString("currImage", null);
        for (int j = 0; j < sizeSong; j++) {
            if (arrayAlbum.get(albumPosition).equals(arrayListSongImage.get(j))) {
                songsDataFromAlbum.add(arrayListData.get(j));
                songsTitleFromAlbum.add(arrayListTitle.get(j));
            }
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                posi = position;
                Intent intent = new Intent(AlbumPlay.this, AlbumService.class);
                stopService(intent);
                intent.putExtra("posi", position);
                startService(intent);
            }
        });

        ArrayAdapter adapter = new MyArrayAdapter(this, android.R.layout.simple_list_item_1, songsTitleFromAlbum);
        gridView.setAdapter(adapter);
    }

    private class MyArrayAdapter extends ArrayAdapter {
        public MyArrayAdapter(AlbumPlay albumPlay, int simple_list_item_1, ArrayList<String> arrayListTitle) {
            super(albumPlay, simple_list_item_1, arrayListTitle);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.custom_grid, parent, false);
            ImageView im = v.findViewById(R.id.grid_image);
            TextView tv = v.findViewById(R.id.grid_text);
            if (currImage != null)
                Glide.with(AlbumPlay.this).load(currImage).into(im);
            else
                Glide.with(AlbumPlay.this).load(R.drawable.album).into(im);
            tv.setText(songsTitleFromAlbum.get(position));
            return v;
        }
    }
}
