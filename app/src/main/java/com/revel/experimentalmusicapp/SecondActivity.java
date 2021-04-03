package com.revel.experimentalmusicapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

import java.io.File;
import java.util.Random;


import dyanamitechetan.vusikview.VusikView;

import static com.revel.experimentalmusicapp.DuoDrawer.firstIntent;
import static com.revel.experimentalmusicapp.DuoDrawer.sizeAlbum;
import static com.revel.experimentalmusicapp.DuoDrawer.sizePath;
import static com.revel.experimentalmusicapp.DuoDrawer.sizeSong;
import static com.revel.experimentalmusicapp.DuoDrawer.tempPosition;
import static com.revel.experimentalmusicapp.MyService.pp;
import static com.revel.experimentalmusicapp.Splash.arrayListAlbumImage;
import static com.revel.experimentalmusicapp.Splash.arrayListData;
import static com.revel.experimentalmusicapp.Splash.arrayListImagePath;
import static com.revel.experimentalmusicapp.Splash.arrayListSongImage;
import static com.revel.experimentalmusicapp.Splash.arrayListTitle;
import static com.revel.experimentalmusicapp.Splash.num;
import static com.revel.experimentalmusicapp.DuoDrawer.playPause;
import static com.revel.experimentalmusicapp.MyService.position;
import static com.revel.experimentalmusicapp.MyService.isPlaying;
import static com.revel.experimentalmusicapp.MyService.mp;

public class SecondActivity extends AppCompatActivity {

    public static VusikView vusikView;
    Button button;
    public static CircleMenu circleMenu;
    public static int flag = 0;
    Button nextModeButton;
    public static ImageView iv;
    public static SeekBar seekBar;
    public static TextView stv, etv;
    public static int duration;
    public static int i;
    public static boolean threadCanMove;
    MySeekBar mySeekBar;
    public static boolean onCurrentPage = false, secondPage = false;
    public static String currentImage;
    ConstraintLayout secondBackground;
    Button menuButton;
    int flagTheme = 1;
    Random random = new Random();
    int color;
    boolean re = false;


    int[] background = {R.drawable.background1, R.drawable.background2, R.drawable.background3, R.drawable.background4, R.drawable.background5
            , R.drawable.background6, R.drawable.background7, R.drawable.background8, R.drawable.background9, R.drawable.background10};

    @Override
    protected void onPause() {
        secondPage = false;
        DuoDrawer.appClosed = true;
        position = tempPosition;
        super.onPause();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            color = resultCode;
            SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("color", resultCode);
            flagTheme = 0;
            re = true;
            editor.putInt("flagTheme", 0);
            editor.apply();
        }
    }

    @Override
    protected void onResume() {
        DuoDrawer.appClosed = false;
        DuoDrawer.z = 0;
        secondPage = true;
        if (currentImage != null) {
            Glide.with(SecondActivity.this).load(currentImage).circleCrop().into(iv);
        }
        if (playPause == 0 && !isPlaying) {
            vusikView.pauseNotesFall();
            circleMenu.setMainMenu(Color.parseColor("#B9E4DF"), R.drawable.ic_play, R.drawable.ic_play);
        } else {
            vusikView.resumeNotesFall();
            circleMenu.setMainMenu(Color.parseColor("#B9E4DF"), R.drawable.ic_pause, R.drawable.ic_pause);
        }
        i = mp.getCurrentPosition();
        seekBar.setProgress(i);

        SharedPreferences sp1 = getSharedPreferences("nextMode", MODE_PRIVATE);
        flag = sp1.getInt("flag", 0);
        switch (flag) {
            case 0:
                nextModeButton.setBackgroundResource(R.drawable.repeatone);
                flag = 1;
                break;
            case 1:
                nextModeButton.setBackgroundResource(R.drawable.shuffle);
                flag = 2;
                break;
            case 2:
                nextModeButton.setBackgroundResource(R.drawable.repeat);
                flag = 0;
                break;
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !re) {
            SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            int requestCode = bundle.getInt("requestCode");
            if (requestCode == 1) {
                flagTheme = bundle.getInt("flagTheme");
                editor.putInt("flagTheme", flagTheme);
            }
            editor.apply();
        }
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        flagTheme = sp.getInt("flagTheme", 0);
        color = sp.getInt("color", 0);
        secondBackground = findViewById(R.id.second_background);
        Window window = getWindow();
        window.setStatusBarColor(Color.parseColor("#2E2626"));
        if (flagTheme != 0) {
            secondBackground.setBackgroundResource(background[flagTheme - 1]);
            // window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        } else {
            if (color != 0) {
                secondBackground.setBackgroundColor(color);
                window.setStatusBarColor(color);
            }
        }
        super.onResume();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        secondPage = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        if (arrayListData != null)
            sizeSong = arrayListData.size();
        if (arrayListAlbumImage != null)
            sizeAlbum = arrayListAlbumImage.size();
        if (arrayListImagePath != null)
            sizePath = arrayListImagePath.size();

        threadCanMove = true;
        vusikView = findViewById(R.id.vusic);
        menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu pop = new PopupMenu(SecondActivity.this, v);
                final MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu, pop.getMenu());
                pop.show();
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.share:
                                Uri curr = Uri.parse(arrayListData.get(position));
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("music/mp3");
                                intent.putExtra(Intent.EXTRA_STREAM, curr);
                                startActivity(Intent.createChooser(intent, "" + arrayListTitle.get(position)));
                                break;
                            case R.id.theme:
                                startActivity(new Intent(SecondActivity.this, Themes.class));
                                finish();
                                break;
                            case R.id.inBack:
                                startActivityForResult(new Intent(SecondActivity.this, BackgroundActivity.class), 1);
                                break;
                        }
                        return false;
                    }
                });
            }
        });

        if (playPause == 1 || isPlaying)
            vusikView.start();
        button = findViewById(R.id.start);
        circleMenu = findViewById(R.id.cm);
        stv = findViewById(R.id.stv);
        etv = findViewById(R.id.etv);
        nextModeButton = findViewById(R.id.nextMode);
        nextModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("nextMode", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                switch (flag) {
                    case 0:
                        nextModeButton.setBackgroundResource(R.drawable.repeatone);
                        editor.putInt("flag", flag);
                        flag = 1;
                        break;
                    case 1:
                        nextModeButton.setBackgroundResource(R.drawable.shuffle);
                        editor.putInt("flag", flag);
                        flag = 2;
                        break;
                    case 2:
                        nextModeButton.setBackgroundResource(R.drawable.repeat);
                        editor.putInt("flag", flag);
                        flag = 0;
                        break;
                }
                editor.apply();
            }
        });

        iv = findViewById(R.id.iv);
        seekBar = findViewById(R.id.sb);
        if (mp != null)
            duration = mp.getDuration();
        final int time = duration / 1000;
        seekBar.setMax(duration);
        etv.setText(time / 60 + ":" + time % 60);
        if ((time % 60) / 10 == 0) {
            etv.setText(time / 60 + ":0" + time % 60);
        }
        Glide.with(SecondActivity.this).load(R.drawable.album).circleCrop().into(iv);

        circleMenu.addSubMenu(Color.parseColor("#B9E4DF"), R.drawable.ic_play)
                .addSubMenu(Color.parseColor("#B9E4DF"), R.drawable.ic_skip_next)
                .addSubMenu(Color.parseColor("#B9E4DF"), R.drawable.ic_pause)
                .addSubMenu(Color.parseColor("#B9E4DF"), R.drawable.ic_skip_previous);
        circleMenu.setOnMenuSelectedListener(new OnMenuSelectedListener() {
            @Override
            public void onMenuSelected(int i) {
                Intent intent = new Intent(SecondActivity.this, MyService.class);
                switch (i) {
                    case 0:
                        if (!firstIntent) {
                            stopService(intent);
                            intent.putExtra("firstIntent", false);
                            intent.putExtra("position", position);
                            startService(intent);
                            vusikView.resumeNotesFall();
                            circleMenu.setMainMenu(Color.parseColor("#B9E4DF"), R.drawable.ic_pause, R.drawable.ic_pause);
                            threadCanMove = true;
                            firstIntent = true;
                            playPause = 1;
                        } else {
                            intent.putExtra("position", position);
                            intent.putExtra("playPause", -2);
                            threadCanMove = true;
                            circleMenu.setMainMenu(Color.parseColor("#B9E4DF"), R.drawable.ic_pause, R.drawable.ic_pause);
                            vusikView.resumeNotesFall();
                            playPause = 1;
                            pp = 0;
                            startService(intent);
                        }
                        break;
                    case 1:
                        SecondActivity.i = 0;
                        circleMenu.setMainMenu(Color.parseColor("#B9E4DF"), R.drawable.ic_pause, R.drawable.ic_pause);
                        threadCanMove = true;
                        seekBar.setProgress(i);
                        if (flag == 0 || flag == 1) {
                            if (position < sizeSong - 1) {
                                ++position;
                            } else
                                position = 0;
                        }
                        if (flag == 2) {
                            int bound = sizeSong - 1;
                            position = random.nextInt(bound);
                        }
                        Glide.with(SecondActivity.this).load(R.drawable.album).circleCrop().into(iv);
                        changeImage();
                        stopService(intent);
                        intent.putExtra("change", true);
                        intent.putExtra("position", position);
                        pp = 0;
                        startService(intent);

                        duration = mp.getDuration();
                        seekBar.setMax(duration);
                        final int time1 = duration / 1000;
                        etv.setText(time1 / 60 + ":" + time1 % 60);
                        if ((time1 % 60) / 10 == 0) {
                            etv.setText(time1 / 60 + ":0" + time1 % 60);
                        }
                        currentImage = String.valueOf(R.drawable.note1);
                        vusikView.resumeNotesFall();
                        break;
                    case 2:
                        vusikView.pauseNotesFall();
                        playPause = 0;
                        circleMenu.setMainMenu(Color.parseColor("#B9E4DF"), R.drawable.ic_play, R.drawable.ic_play);
                        threadCanMove = false;
                        intent.putExtra("position", position);
                        intent.putExtra("playPause", -1);
                        pp = 1;
                        startService(intent);
                        break;
                    case 3:
                        SecondActivity.i = 0;
                        circleMenu.setMainMenu(Color.parseColor("#B9E4DF"), R.drawable.ic_pause, R.drawable.ic_pause);
                        vusikView.resumeNotesFall();
                        threadCanMove = true;
                        if (position > 0)
                            --position;
                        else
                            position = sizeSong - 1;
                        stopService(intent);
                        intent.putExtra("change", true);
                        intent.putExtra("position", position);
                        pp = 0;
                        startService(intent);

                        duration = mp.getDuration();
                        seekBar.setMax(duration);
                        final int time2 = duration / 1000;
                        etv.setText(time2 / 60 + ":" + time2 % 60);
                        if ((time2 % 60) / 10 == 0) {
                            etv.setText(time2 / 60 + ":0" + time2 % 60);
                        }

                        Glide.with(SecondActivity.this).load(R.drawable.album).circleCrop().into(iv);
                        currentImage = String.valueOf(R.drawable.note1);
                        changeImage();
                        break;
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    seekBar.setProgress(progress);
                    i = progress;
                    int time = i / 1000;
                    if ((time % 60) / 10 != 0)
                        stv.setText(time / 60 + ":" + time % 60);
                    else
                        stv.setText(time / 60 + ":0" + time % 60);
                    mp.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mySeekBar = new MySeekBar();
        mySeekBar.start();
    }

    private void changeImage() {
        for (int j = 0; j < num; j++) {
            if (sizeSong - 1 >= position && sizeAlbum - 1 >= j && sizePath - 1 >= j)
                if (arrayListAlbumImage.get(j) != null && arrayListSongImage.get(MyService.position) != null)
                    if (arrayListAlbumImage.get(j).equals(arrayListSongImage.get(position))) {
                        Glide.with(SecondActivity.this).load(arrayListImagePath.get(j)).circleCrop().into(iv);
                        currentImage = arrayListImagePath.get(j);
                    }
        }
    }

    class MySeekBar extends Thread {
        public void run() {
            while (i >= 0) {
                if (i >= duration) {
                    i = 0;
                }
                if (threadCanMove && i != duration) {
                    if (mp.isPlaying() || isPlaying) {
                        i = mp.getCurrentPosition();
                        seekBar.setProgress(i);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int time = i / 1000;
                            if ((time % 60) / 10 != 0)
                                stv.setText(time / 60 + ":" + time % 60);
                            else
                                stv.setText(time / 60 + ":0" + time % 60);
                        }
                    });
                }
            }
        }
    }
}

