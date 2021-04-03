package com.revel.experimentalmusicapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static com.revel.experimentalmusicapp.DuoDrawer.circleImage;
import static com.revel.experimentalmusicapp.DuoDrawer.sizeAlbum;
import static com.revel.experimentalmusicapp.DuoDrawer.sizePath;
import static com.revel.experimentalmusicapp.DuoDrawer.sizeSong;
import static com.revel.experimentalmusicapp.DuoDrawer.tabBarImage;
import static com.revel.experimentalmusicapp.DuoDrawer.tempPosition;
import static com.revel.experimentalmusicapp.DuoDrawer.titleView;
import static com.revel.experimentalmusicapp.SecondActivity.currentImage;
import static com.revel.experimentalmusicapp.SecondActivity.duration;
import static com.revel.experimentalmusicapp.SecondActivity.etv;
import static com.revel.experimentalmusicapp.SecondActivity.flag;
import static com.revel.experimentalmusicapp.SecondActivity.iv;
import static com.revel.experimentalmusicapp.SecondActivity.onCurrentPage;
import static com.revel.experimentalmusicapp.SecondActivity.secondPage;
import static com.revel.experimentalmusicapp.SecondActivity.seekBar;
import static com.revel.experimentalmusicapp.SecondActivity.threadCanMove;
import static com.revel.experimentalmusicapp.SecondActivity.vusikView;
import static com.revel.experimentalmusicapp.Splash.arrayListAlbumImage;
import static com.revel.experimentalmusicapp.Splash.arrayListArtist;
import static com.revel.experimentalmusicapp.Splash.arrayListData;
import static com.revel.experimentalmusicapp.Splash.arrayListImagePath;
import static com.revel.experimentalmusicapp.Splash.arrayListSongArtist;
import static com.revel.experimentalmusicapp.Splash.arrayListSongImage;
import static com.revel.experimentalmusicapp.Splash.arrayListTitle;
import static com.revel.experimentalmusicapp.Splash.num;

public class MyService extends Service {
    public MyService() {
    }

    public static String CHANNEL_ID_1 = "CHANNEL_ID_1";
    public static String CHANNEL_NAME = "CHANNEL_NAME";
    public static MediaPlayer mp = new MediaPlayer();
    Uri uriAlbum, uriSong, uriArtist;
    Cursor cursorSong;
    ArrayList<String> arrayListDataCurr;
    public static int position = -1;
    public static boolean isPlaying = false;
    int playPause;
    boolean change = false;
    int[] icPP = {R.drawable.ic_pause, R.drawable.ic_play};
    public static int pp;
    String cim, ct, ca;
    boolean firstIntent;
    String artist = null;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (arrayListData.size() > 0) {
            if (intent != null) {
                firstIntent = intent.getBooleanExtra("firstIntent", true);
                playPause = intent.getIntExtra("playPause", 0);
                change = intent.getBooleanExtra("change", false);
                position = intent.getIntExtra("position", 0);
                tempPosition = position;
            }
            if (!firstIntent) {
                try {
                    mp.reset();
                    if (position >= 0 && position < sizeSong)
                        mp.setDataSource(arrayListDataCurr.get(position));
                    else
                        mp.setDataSource(arrayListDataCurr.get(0));
                    mp.prepare();
                    mp.seekTo(SecondActivity.i);
                    mp.start();
                    isPlaying = true;
                } catch (IOException e) {
                    Intent intent1 = new Intent(MyService.this, MyReceiver.class);
                    stopService(intent1);
                    intent.putExtra("position", tempPosition);
                    startService(intent1);
                    e.printStackTrace();
                }
            }
            if (firstIntent) {
                if (playPause == -1) {
                    mp.pause();
                    isPlaying = false;
                } else if (playPause == -2) {
                    mp.start();
                    isPlaying = true;
                } else {
                    try {
                        mp.stop();
                        mp.reset();
                        if (position >= 0 && position < sizeSong)
                            mp.setDataSource(arrayListDataCurr.get(position));
                        else
                            mp.setDataSource(arrayListDataCurr.get(0));
                        mp.prepare();
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.start();
                                isPlaying = true;
                            }
                        });
                        if (change && (onCurrentPage || secondPage))
                            changeEtv();
                    } catch (IOException e) {
                        Intent intent1 = new Intent(MyService.this, MyReceiver.class);
                        stopService(intent);
                        intent.putExtra("position", tempPosition);
                        startService(intent1);
                        e.printStackTrace();

                    }
                }
            }
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (!MyService.mp.isPlaying()) {
                        if (AlbumPlay.playingAlbums) {
                            Intent intent = new Intent(MyService.this, AlbumService.class);
                            startService(intent);
                            if (flag == 0) {
                                if (AlbumPlay.songsDataFromAlbum.size() - 1 > AlbumPlay.posi)
                                    ++AlbumPlay.posi;
                                else
                                    AlbumPlay.posi = 0;
                            } else if (flag == 1) {

                            } else {
                                int bound = AlbumPlay.songsDataFromAlbum.size() - 1;
                                if (bound != 0)
                                    AlbumPlay.posi = new Random().nextInt();
                                else
                                    AlbumPlay.posi = 0;
                            }
                            startService(intent);
                        } else {
                            Intent intent = new Intent(MyService.this, MyService.class);
                            stopService(intent);
                            SecondActivity.i = 0;
                            if (flag == 0) {
                                if (position < sizeSong - 1) {
                                    ++position;
                                } else
                                    position = 0;
                            } else if (flag == 1) {
                            } else {
                                int bound = sizeSong - 1;
                                if (bound != 0)
                                    position = new Random().nextInt(sizeSong - 1);
                            }
                            if (position < 0 && position > sizeSong - 1) {
                                position = 0;
                            }
                            if (position >= 0 && position < sizeSong) {
                                intent.putExtra("position", position);
                                intent.putExtra("change", true);
                            } else
                                intent.putExtra("position", 0);
                            startService(intent);
                        }
                    }
                }
            });

            createNotification();
        }
        return START_NOT_STICKY;
    }

    private void changeEtv() {
        if (position >= 0 && position < sizeSong)
            for (int j = 0; j < num; j++) {
                if (sizeSong - 1 >= position && sizeAlbum - 1 >= j && sizePath - 1 >= j)
                    if (arrayListAlbumImage.get(j) != null && arrayListSongImage.get(MyService.position) != null)
                        if (arrayListAlbumImage.get(j).equals(arrayListSongImage.get(position))) {
                            SecondActivity.currentImage = arrayListImagePath.get(j);
                            titleView.setText(arrayListTitle.get(position));
                            Glide.with(MyService.this).load(R.drawable.album).circleCrop().into(tabBarImage);
                            Glide.with(MyService.this).load(R.drawable.album).circleCrop().into(circleImage);
                            if (currentImage != null) {
                                Glide.with(MyService.this).load(currentImage).circleCrop().into(tabBarImage);
                                Glide.with(MyService.this).load(currentImage).circleCrop().into(circleImage);
                            }
                            change = false;
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
                            threadCanMove = true;
                            vusikView.resumeNotesFall();
                            Glide.with(MyService.this).load(R.drawable.album).circleCrop().into(iv);
                            if (currentImage != null) {
                                Glide.with(MyService.this).load(currentImage).circleCrop().into(iv);
                            }
                        }
            }
    }

    @Override
    public void onCreate() {
        super.onCreate();


        uriAlbum = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        uriSong = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        uriArtist = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;

        arrayListDataCurr = new ArrayList<>();

        ContentResolver resolver = getContentResolver();
        cursorSong = resolver.query(uriSong, null, MediaStore.Audio.Media.IS_MUSIC + "!=0", null, MediaStore.Audio.Media.TITLE + " ASC");

        while (cursorSong.moveToNext()) {
            String data = cursorSong.getString(cursorSong.getColumnIndex(MediaStore.Audio.Media.DATA));
            arrayListDataCurr.add(data);
        }

    }

    @Override
    public void onDestroy() {
        mp.stop();
        isPlaying = false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotification() {
        createChannel();
        int dominantColor = getDominantColor(BitmapFactory.decodeFile(currentImage));

        Intent intent1 = new Intent(MyService.this, MyReceiver.class);
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(MyService.this, 100, intent1, PendingIntent.FLAG_ONE_SHOT);

        Intent intent2 = new Intent(MyService.this, MyReceiver2.class);
        PendingIntent pausePendingIntent = PendingIntent.getBroadcast(MyService.this, 200, intent2, PendingIntent.FLAG_ONE_SHOT);

        Intent intent3 = new Intent(MyService.this, MyReceiver3.class);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(MyService.this, 300, intent3, PendingIntent.FLAG_ONE_SHOT);

        Intent intent4 = new Intent(MyService.this, MyReceiver4.class);
        PendingIntent deleteAction = PendingIntent.getBroadcast(MyService.this, 400, intent4, PendingIntent.FLAG_ONE_SHOT);

        Intent intent5 = new Intent(MyService.this, Splash.class);
        PendingIntent directPage = PendingIntent.getActivity(MyService.this, 500, intent5, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, CHANNEL_ID_1);
        builder.setSmallIcon(R.drawable.note1);
        builder.addAction(R.drawable.ic_skip_previous, "Previous", prevPendingIntent); // #0
        builder.addAction(icPP[pp], "Pause", pausePendingIntent);// #1
        builder.addAction(R.drawable.ic_skip_next, "Next", nextPendingIntent);
        builder.addAction(R.drawable.ic_cancel, "cancel", deleteAction); // #2
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setColor(dominantColor);
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

    private String findCurrArtist() {
        if (position >= 0 && position < sizeSong)
            for (int j = 0; j < arrayListArtist.size() - 1; j++) {
                if (arrayListArtist.get(j).equals(arrayListSongArtist.get(position))) {
                    artist = arrayListArtist.get(j);
                }
            }
        return artist;
    }

    public static String findCurrImage() {
        if (position >= 0 && position < sizeSong)
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

    public static int getDominantColor(Bitmap bitmap) {
        int color = 250;
        if (bitmap != null) {
            Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
            color = newBitmap.getPixel(0, 0);
        }
        return color;
    }
}

