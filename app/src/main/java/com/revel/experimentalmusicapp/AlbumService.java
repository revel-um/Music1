package com.revel.experimentalmusicapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

import static com.revel.experimentalmusicapp.AlbumPlay.posi;
import static com.revel.experimentalmusicapp.AlbumPlay.songsDataFromAlbum;
import static com.revel.experimentalmusicapp.DuoDrawer.circleImage;
import static com.revel.experimentalmusicapp.DuoDrawer.firstIntent;
import static com.revel.experimentalmusicapp.DuoDrawer.playPause;
import static com.revel.experimentalmusicapp.DuoDrawer.sizeAlbum;
import static com.revel.experimentalmusicapp.DuoDrawer.sizePath;
import static com.revel.experimentalmusicapp.DuoDrawer.sizeSong;
import static com.revel.experimentalmusicapp.DuoDrawer.tabBarImage;
import static com.revel.experimentalmusicapp.DuoDrawer.titleView;
import static com.revel.experimentalmusicapp.MyService.isPlaying;
import static com.revel.experimentalmusicapp.MyService.mp;
import static com.revel.experimentalmusicapp.MyService.position;
import static com.revel.experimentalmusicapp.MyService.pp;
import static com.revel.experimentalmusicapp.SecondActivity.currentImage;
import static com.revel.experimentalmusicapp.SecondActivity.duration;
import static com.revel.experimentalmusicapp.SecondActivity.etv;
import static com.revel.experimentalmusicapp.SecondActivity.iv;
import static com.revel.experimentalmusicapp.SecondActivity.onCurrentPage;
import static com.revel.experimentalmusicapp.SecondActivity.secondPage;
import static com.revel.experimentalmusicapp.SecondActivity.seekBar;
import static com.revel.experimentalmusicapp.Splash.arrayListAlbumImage;
import static com.revel.experimentalmusicapp.Splash.arrayListArtist;
import static com.revel.experimentalmusicapp.Splash.arrayListData;
import static com.revel.experimentalmusicapp.Splash.arrayListImagePath;
import static com.revel.experimentalmusicapp.Splash.arrayListSongArtist;
import static com.revel.experimentalmusicapp.Splash.arrayListSongImage;
import static com.revel.experimentalmusicapp.Splash.arrayListTitle;
import static com.revel.experimentalmusicapp.Splash.num;

public class AlbumService extends Service {
    public AlbumService() {
    }

    String artist;
    String CHANNEL_ID_1 = "CHANNEL_ID_1";
    String CHANNEL_NAME = "CHANNEL_NAME";
    String cim, ct, ca;
    int icPP[] = {R.drawable.ic_pause, R.drawable.ic_play};

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        for (int k = 0; k < sizeSong; k++) {
            if (arrayListData.get(k).equals(songsDataFromAlbum.get(posi))) {
                position = k;
            }
        }
        try {
            Toast.makeText(this, "Playing song from album", Toast.LENGTH_SHORT).show();
            mp.stop();
            mp.reset();
            mp.setDataSource(songsDataFromAlbum.get(posi));
            mp.prepare();
            mp.start();
            AlbumPlay.playingAlbums = true;
            isPlaying = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        changeEtv();
        createNotification();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mp.stop();
        AlbumPlay.playingAlbums = false;
        isPlaying = false;
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createChannel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void changeEtv() {
        for (int j = 0; j < num; j++) {
            if (sizeSong - 1 >= position && sizeAlbum - 1 >= j && sizePath - 1 >= j)
                if (arrayListAlbumImage.get(j) != null && arrayListSongImage.get(MyService.position) != null)
                    if (arrayListAlbumImage.get(j).equals(arrayListSongImage.get(position))) {
                        SecondActivity.currentImage = arrayListImagePath.get(j);
                        if (onCurrentPage) {
                            titleView.setText(arrayListTitle.get(position));
                            Glide.with(AlbumService.this).load(R.drawable.album).circleCrop().into(tabBarImage);
                            Glide.with(AlbumService.this).load(R.drawable.album).circleCrop().into(circleImage);
                            if (currentImage != null) {
                                Glide.with(AlbumService.this).load(currentImage).circleCrop().into(tabBarImage);
                                Glide.with(AlbumService.this).load(currentImage).circleCrop().into(circleImage);
                            }
                        }
                        if (secondPage) {
                            duration = mp.getDuration();
                            if (seekBar != null)
                                seekBar.setMax(duration);

                            final int time1 = duration / 1000;
                            if (etv != null) {
                                etv.setText(time1 / 60 + ":" + time1 % 60);
                                if ((time1 % 60) / 10 == 0) {
                                    etv.setText(time1 / 60 + ":0" + time1 % 60);
                                }
                            }
                            Glide.with(AlbumService.this).load(R.drawable.album).circleCrop().into(iv);
                            if (currentImage != null) {
                                Glide.with(AlbumService.this).load(currentImage).circleCrop().into(iv);
                            }
                        }

                    }
        }
    }

    private String findCurrArtist() {
        for (int j = 0; j < arrayListArtist.size() - 1; j++) {
            if (arrayListArtist.get(j).equals(arrayListSongArtist.get(position))) {
                artist = arrayListArtist.get(j);
            }
        }
        return artist;
    }

    public static String findCurrImage() {

        for (int i = 0; i < num; i++)
            if (sizeSong - 1 >= position && sizeAlbum - 1 >= i && sizePath - 1 >= i)
                if (arrayListSongImage.get(position) != null && arrayListAlbumImage.get(i) != null)
                    if (arrayListAlbumImage.get(i).equals(arrayListSongImage.get(position))) {
                        if (arrayListImagePath.get(i) != null)
                            currentImage = arrayListImagePath.get(i);
                        else
                            currentImage = null;
                    }
        return currentImage;

    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID_1, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);

            channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            channel1.enableLights(true);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }

    private void createNotification() {

        Intent intent1 = new Intent(AlbumService.this, MyReceiver.class);
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(AlbumService.this, 100, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent2 = new Intent(AlbumService.this, MyReceiver2.class);
        PendingIntent pausePendingIntent = PendingIntent.getBroadcast(AlbumService.this, 200, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent3 = new Intent(AlbumService.this, MyReceiver3.class);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(AlbumService.this, 300, intent3, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent4 = new Intent(AlbumService.this, MyReceiver4.class);
        PendingIntent deleteAction = PendingIntent.getBroadcast(AlbumService.this, 400, intent4, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent5 = new Intent(AlbumService.this, Splash.class);
        PendingIntent directPage = PendingIntent.getActivity(AlbumService.this, 500, intent5, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(AlbumService.this, CHANNEL_ID_1);
        builder.setSmallIcon(R.drawable.note1);
        builder.addAction(R.drawable.ic_skip_previous, "Previous", prevPendingIntent); // #0
        builder.addAction(icPP[pp], "Pause", pausePendingIntent);// #1
        builder.addAction(R.drawable.ic_skip_next, "Next", nextPendingIntent);
        builder.addAction(R.drawable.ic_cancel, "cancel", deleteAction); // #2
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setColor(Color.parseColor("#00bca0"));
        builder.setVibrate(new long[0]);
        builder.setOngoing(true);
        builder.setColorized(true);
        builder.setContentIntent(directPage);

        // Apply the media style template
        builder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(1, 2 /* #1: pause button */));
        if (playPause == 0) {
            currentImage = findCurrImage();
            cim = currentImage;
            ca = findCurrArtist();
            if (arrayListTitle != null)
                ct = arrayListTitle.get(position);
            else
                ct = null;
            builder.setContentTitle(ct);
            if (ca != null) {
                builder.setContentText(ca);
            } else {
                builder.setContentText("Unknown");
            }
            if (cim != null)
                builder.setLargeIcon(BitmapFactory.decodeFile(cim));
            else
                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.album));
        } else {
            builder.setContentTitle(ct);
            if (ca != null) {
                builder.setContentText(ca);
            } else {
                builder.setContentText("Unknown");
            }
            if (cim != null)
                builder.setLargeIcon(BitmapFactory.decodeFile(cim));
            else
                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.album));
        }
        if (!firstIntent) {
            builder.setLargeIcon(BitmapFactory.decodeFile(currentImage));
            builder.setContentTitle(arrayListTitle.get(position));
            artist = findCurrArtist();
            if (artist != null) {
                builder.setContentText(findCurrArtist());
            } else {
                builder.setContentText("Unknown");
            }
        }
        startForeground(1, builder.build());
    }
}
