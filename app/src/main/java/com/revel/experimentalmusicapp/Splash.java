package com.revel.experimentalmusicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import java.util.ArrayList;


public class Splash extends AppCompatActivity {
    public static int num, numberOfArtist;
    public static ArrayList<String> arrayListData, arrayListTitle, arrayListSongArtist, arrayListArtist;
    Uri uriSong, uriAlbum, uriArtist;
    Cursor cursorSong, cursorAlbum, cursorArtist;
    String imageAlbum, imagePath, imageSong;
    public static ArrayList<String> arrayListAlbumImage, arrayListSongImage, arrayListImagePath;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED
                && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
            resolveData();
            Intent intent = new Intent(Splash.this, DuoDrawer.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
            finishAffinity();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        arrayListData = new ArrayList<>();
        arrayListTitle = new ArrayList<>();
        arrayListSongArtist = new ArrayList<>();
        arrayListArtist = new ArrayList<>();
        arrayListAlbumImage = new ArrayList<>();
        arrayListSongImage = new ArrayList<>();
        arrayListImagePath = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS) == PackageManager.PERMISSION_GRANTED) {
            resolveData();
            Intent intent = new Intent(Splash.this, DuoDrawer.class);
            startActivity(intent);
            finish();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.VIBRATE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.MODIFY_AUDIO_SETTINGS}, 100);
        }
    }

    private void resolveData() {
        uriAlbum = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        uriSong = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        uriArtist = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;

        ContentResolver resolver = getContentResolver();

        cursorAlbum = resolver.query(uriAlbum, null, null, null, MediaStore.Audio.Albums.ALBUM + " ASC");
        cursorSong = resolver.query(uriSong, null, MediaStore.Audio.Media.IS_MUSIC + "!=0", null, MediaStore.Audio.Media.TITLE + " ASC");
        cursorArtist = resolver.query(uriArtist, null, null, null, MediaStore.Audio.Artists.ARTIST + " ASC");

        while (cursorAlbum.moveToNext()) {
            imageAlbum = cursorAlbum.getString(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
            imagePath = cursorAlbum.getString(cursorAlbum.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART));
            if (imageAlbum != null) {
                arrayListAlbumImage.add(imageAlbum);
                arrayListImagePath.add(imagePath);
                num++;
            } else {
                arrayListAlbumImage.add(null);
                arrayListImagePath.add(null);
            }
        }

        while (cursorSong.moveToNext()) {
            String title = cursorSong.getString(cursorSong.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String data = cursorSong.getString(cursorSong.getColumnIndex(MediaStore.Audio.Media.DATA));
            String artistSong = cursorSong.getString(cursorSong.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            imageSong = cursorSong.getString(cursorSong.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            if (imageSong != null) {
                arrayListSongImage.add(imageSong);
            }
            arrayListSongArtist.add(artistSong);
            if (data != null) {
                arrayListData.add(data);
            }
            arrayListTitle.add(title);
        }
        while (cursorArtist.moveToNext()) {
            String artist = cursorArtist.getString(cursorArtist.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            if (artist != null) {
                arrayListArtist.add(artist);
                numberOfArtist++;
            }
        }
    }

}
