package com.revel.experimentalmusicapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.rtugeek.android.colorseekbar.ColorSeekBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import es.claucookie.miniequalizerlibrary.EqualizerView;

import static com.revel.experimentalmusicapp.MyService.isPlaying;
import static com.revel.experimentalmusicapp.MyService.mp;
import static com.revel.experimentalmusicapp.MyService.position;
import static com.revel.experimentalmusicapp.MyService.pp;
import static com.revel.experimentalmusicapp.SecondActivity.circleMenu;
import static com.revel.experimentalmusicapp.SecondActivity.currentImage;
import static com.revel.experimentalmusicapp.SecondActivity.duration;
import static com.revel.experimentalmusicapp.SecondActivity.flag;
import static com.revel.experimentalmusicapp.SecondActivity.onCurrentPage;
import static com.revel.experimentalmusicapp.Splash.arrayListAlbumImage;
import static com.revel.experimentalmusicapp.Splash.arrayListData;
import static com.revel.experimentalmusicapp.Splash.arrayListImagePath;
import static com.revel.experimentalmusicapp.Splash.arrayListSongArtist;
import static com.revel.experimentalmusicapp.Splash.arrayListSongImage;
import static com.revel.experimentalmusicapp.Splash.arrayListTitle;
import static com.revel.experimentalmusicapp.Splash.num;

public class DuoDrawer extends AppCompatActivity {
    public static Button play;
    public static byte playPause = 0;
    ListView listView;
    public static TextView titleView;
    public static ImageView tabBarImage, circleImage;
    public static EqualizerView equalizer;
    public static int sizeSong;
    public static int sizePath;
    public static int sizeAlbum;
    public static int tempPosition;
    boolean threadCanMove1;
    LinearLayout linearLayout;
    public static boolean firstIntent = false;
    int color;
    int backColor;
    LinearLayout duoBackground;
    boolean blockTab;
    Button pre, next;
    public static boolean appClosed;
    public static int z;

    @Override
    protected void onPause() {
        appClosed = true;
        onCurrentPage = false;
        super.onPause();
    }


    @Override
    protected void onResume() {
        z = 0;
        appClosed = false;
        blockTab = false;
        duoBackground.setBackgroundColor(-9216168);
        onCurrentPage = true;
        if (currentImage != null) {
            Glide.with(DuoDrawer.this).load(currentImage).centerCrop().into(tabBarImage);
            Glide.with(DuoDrawer.this).load(currentImage).circleCrop().into(circleImage);
        } else {
            Glide.with(DuoDrawer.this).load(R.drawable.album).centerCrop().into(tabBarImage);
            Glide.with(DuoDrawer.this).load(R.drawable.album).circleCrop().into(circleImage);
        }

        if (isPlaying) {
            duration = mp.getDuration();
            i = mp.getCurrentPosition();
        } else {
            i = 0;
            duration = 0;
        }
        if (position != -1)
            titleView.setText(arrayListTitle.get(position));
        else
            titleView.setText(arrayListTitle.get(0));

        if (isPlaying || playPause == 1) {
            play.setBackgroundResource(R.drawable.ic_pause);
            equalizer.animateBars();
        } else {
            play.setBackgroundResource(R.drawable.ic_play);
            equalizer.stopBars();
        }

        Window window = getWindow();

        SharedPreferences sp = getSharedPreferences("duoData", MODE_PRIVATE);
        backColor = sp.getInt("color", 0);
        if (backColor != 0) {
            duoBackground.setBackgroundColor(backColor);
            window.setStatusBarColor(backColor);
        } else
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        super.onResume();
    }

    int i;
    ColorSeekBar colorSeekBar;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            backColor = resultCode;
            SharedPreferences sp = getSharedPreferences("duoData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("color", resultCode);
            editor.apply();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.share, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                Uri curr = Uri.parse(arrayListData.get(position));
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("music/mp3");
                intent.putExtra(Intent.EXTRA_STREAM, curr);
                startActivity(Intent.createChooser(intent, "" + arrayListTitle.get(position)));
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        onCurrentPage = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duo_2);

        linearLayout = findViewById(R.id.tab);

        colorSeekBar = findViewById(R.id.colorSlider);
        colorSeekBar.setMaxPosition(100000);
        colorSeekBar.setColorSeeds(R.array.material_colors);

        colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
            @Override
            public void onColorChangeListener(int colorBarPosition, int alphaBarPosition, int color) {
                linearLayout.setBackgroundColor(color);
                DuoDrawer.this.color = color;
            }
        });
        duoBackground = findViewById(R.id.duoBackground);
        colorSeekBar.setY(-1000000000);
        new Thread() {
            int k;

            public void run() {
                boolean inc = true;
                while (k >= 0) {
                    colorSeekBar.setPosition(k, k);
                    if (inc)
                        k++;
                    else
                        k--;
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (k == 100000) {
                        inc = false;
                    }
                    if (k == 0) {
                        inc = true;
                    }
                }
            }
        }.start();
        final LinearLayout tab = findViewById(R.id.tabular);

        listView = findViewById(R.id.listView);
        play = findViewById(R.id.play);
        titleView = findViewById(R.id.titleView);
        tabBarImage = findViewById(R.id.tabBarImage);
        equalizer = findViewById(R.id.equalizer_view);
        Button search = findViewById(R.id.search);
        circleImage = findViewById(R.id.circleImage);
        pre = findViewById(R.id.pre);
        next = findViewById(R.id.next);

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DuoDrawer.this, MyService.class);
                SecondActivity.i = 0;
                if (position > 0)
                    --position;
                else
                    position = sizeSong - 1;
                stopService(intent);
                intent.putExtra("change", true);
                intent.putExtra("position", position);
                pp = 0;
                startService(intent);
                equalizer.animateBars();
                play.setBackgroundResource(R.drawable.ic_pause);
                Glide.with(DuoDrawer.this).load(R.drawable.album).circleCrop().into(circleImage);
                Glide.with(DuoDrawer.this).load(R.drawable.album).circleCrop().into(tabBarImage);
                currentImage = String.valueOf(R.drawable.note1);
                changeImage();
                firstIntent = true;
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DuoDrawer.this, MyService.class);
                SecondActivity.i = 0;
                if (flag == 0 || flag == 1) {
                    if (position < sizeSong - 1) {
                        ++position;
                    } else
                        position = 0;
                }
                if (flag == 2) {
                    int bound = sizeSong - 1;
                    Random random = new Random();
                    position = random.nextInt(bound);
                }
                Glide.with(DuoDrawer.this).load(R.drawable.album).circleCrop().into(circleImage);
                Glide.with(DuoDrawer.this).load(R.drawable.album).circleCrop().into(tabBarImage);
                changeImage();
                stopService(intent);
                equalizer.animateBars();
                play.setBackgroundResource(R.drawable.ic_pause);
                intent.putExtra("change", true);
                intent.putExtra("position", position);
                pp = 0;
                startService(intent);
                currentImage = String.valueOf(R.drawable.note1);
                firstIntent = true;
            }
        });


        registerForContextMenu(tab);
        if (!firstIntent) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            currentImage = sp.getString("currImage", null);
            duration = sp.getInt("duration", 0);
            SecondActivity.i = i = sp.getInt("progress", 0);
            position = sp.getInt("position", -1);

            if (currentImage != null) {
                Glide.with(DuoDrawer.this).load(currentImage).centerCrop().into(tabBarImage);
                Glide.with(DuoDrawer.this).load(currentImage).circleCrop().into(circleImage);
            } else {
                Glide.with(DuoDrawer.this).load(R.drawable.album).centerCrop().into(tabBarImage);
                Glide.with(DuoDrawer.this).load(R.drawable.album).circleCrop().into(circleImage);
            }
        }
        circleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DuoDrawer.this, MyService.class);
                if (!firstIntent) {
                    pp = 0;
                    stopService(intent);
                    intent.putExtra("firstIntent", false);
                    if (position != 0)
                        intent.putExtra("position", position);
                    else
                        intent.putExtra("position", 0);
                    startService(intent);
                    firstIntent = true;
                    playPause = 1;
                } else {
                    if (position == -1) {
                        stopService(intent);
                        intent.putExtra("position", 0);
                        pp = 0;
                        startService(intent);
                    }
                }
                if (!blockTab) {
                    blockTab = true;
                    Intent intent1 = new Intent(DuoDrawer.this, SecondActivity.class);
                    startActivity(intent1);
                }
            }
        });

        tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DuoDrawer.this, MyService.class);
                if (!firstIntent && position != -1) {
                    stopService(intent);
                    pp = 0;
                    intent.putExtra("firstIntent", false);
                    if (position != -1)
                        intent.putExtra("position", position);
                    else
                        intent.putExtra("position", 0);
                    startService(intent);
                    firstIntent = true;
                    playPause = 1;
                } else {
                    if (position == -1) {
                        stopService(intent);
                        intent.putExtra("position", 0);
                        pp = 0;
                        startService(intent);
                    }
                }
                if (!blockTab) {
                    blockTab = true;
                    Intent intent1 = new Intent(DuoDrawer.this, SecondActivity.class);
                    startActivity(intent1);
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DuoDrawer.this, SearchActivity.class));
            }
        });
        final BoomMenuButton bmb = findViewById(R.id.bmb);
        bmb.setNormalColor(Color.parseColor("#00f0f0"));
        bmb.addBuilder(new HamButton.Builder()
                .normalImageRes(R.drawable.library)
                .normalText("Library")
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {

                    }
                }));
        bmb.addBuilder(new HamButton.Builder()
                .normalImageRes(R.drawable.albums)
                .normalText("Albums")
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        startActivity(new Intent(DuoDrawer.this, AlbumActivity.class));

                    }
                }));
        bmb.addBuilder(new HamButton.Builder()
                .normalImageRes(R.drawable.themes)
                .normalText("Themes")
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        startActivityForResult(new Intent(DuoDrawer.this, BackgroundActivity.class), 2);

                    }
                }));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SecondActivity.i = 0;
                AlbumPlay.playingAlbums = false;
                Intent intent = new Intent(DuoDrawer.this, MyService.class);
                stopService(intent);
                intent.putExtra("position", position);
                pp = 0;
                startService(intent);
                threadCanMove1 = true;
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(arrayListData.get(position));
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                duration = mediaPlayer.getDuration();
                equalizer.animateBars();
                play.setClickable(true);
                MyService.position = position;
                playPause = 1;
                Glide.with(DuoDrawer.this).load(R.drawable.album).centerCrop().into(tabBarImage);
                Glide.with(DuoDrawer.this).load(R.drawable.album).circleCrop().into(circleImage);
                currentImage = null;

                for (int j = 0; j < num; j++) {
                    if (sizeSong - 1 >= position && sizeAlbum - 1 >= j && sizePath - 1 >= j)
                        if (arrayListAlbumImage.get(j) != null && arrayListSongImage.get(position) != null)
                            if (arrayListAlbumImage.get(j).equals(arrayListSongImage.get(position))) {
                                currentImage = arrayListImagePath.get(j);
                                if (currentImage != null) {
                                    Glide.with(DuoDrawer.this).load(arrayListImagePath.get(j)).centerCrop().into(tabBarImage);
                                    Glide.with(DuoDrawer.this).load(arrayListImagePath.get(j)).circleCrop().into(circleImage);
                                }
                            }
                }

                play.setBackgroundResource(R.drawable.ic_pause);

                tempPosition = MyService.position = position;

                titleView.setText(arrayListTitle.get(position));
                if (!firstIntent) {
                    firstIntent = true;
                    startActivity(new Intent(DuoDrawer.this, SecondActivity.class));
                }
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DuoDrawer.this, MyService.class);
                if (playPause == 1) {
                    intent.putExtra("position", position);
                    intent.putExtra("playPause", -1);
                    equalizer.stopBars();
                    play.setBackgroundResource(R.drawable.ic_play);
                    playPause = 0;
                    threadCanMove1 = false;
                    pp = 1;
                    startService(intent);
                } else {
                    if (!firstIntent) {
                        stopService(intent);
                        pp = 0;
                        if (position != -1) {
                            intent.putExtra("position", position);
                        } else
                            intent.putExtra("position", 0);
                        intent.putExtra("firstIntent", false);
                        startService(intent);
                        play.setBackgroundResource(R.drawable.ic_pause);
                        equalizer.animateBars();
                        firstIntent = true;
                        playPause = 1;
                        startActivity(new Intent(DuoDrawer.this, SecondActivity.class));
                    } else {
                        intent.putExtra("position", position);
                        intent.putExtra("playPause", -2);
                        equalizer.animateBars();
                        play.setBackgroundResource(R.drawable.ic_pause);
                        playPause = 1;
                        pp = 0;
                        threadCanMove1 = true;
                        startService(intent);
                    }
                }
            }
        });
        ArrayAdapter adapterTitle = new MyAdapter(DuoDrawer.this, android.R.layout.simple_list_item_1, arrayListTitle);
        listView.setAdapter(adapterTitle);
    }

    private void changeImage() {
        for (int j = 0; j < num; j++) {
            if (sizeSong - 1 >= position && sizeAlbum - 1 >= j && sizePath - 1 >= j)
                if (arrayListAlbumImage.get(j) != null && arrayListSongImage.get(MyService.position) != null)
                    if (arrayListAlbumImage.get(j).equals(arrayListSongImage.get(position))) {
                        Glide.with(DuoDrawer.this).load(arrayListImagePath.get(j)).circleCrop().into(circleImage);
                        Glide.with(DuoDrawer.this).load(arrayListImagePath.get(j)).circleCrop().into(tabBarImage);
                        currentImage = arrayListImagePath.get(j);
                    }
        }
    }

    private class MyAdapter extends ArrayAdapter {


        public MyAdapter(DuoDrawer duoDrawer, int simple_list_item_1, ArrayList arrayListTitle) {

            super(duoDrawer, simple_list_item_1, arrayListTitle);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

            LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View v = li.inflate(R.layout.custom_layout, parent, false);
            TextView tv = v.findViewById(R.id.tv);
            tv.setText("" + arrayListTitle.get(position) + "\n" + arrayListSongArtist.get(position));
            ImageView iv = v.findViewById(R.id.image);
            Button button = v.findViewById(R.id.contextMenu);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DuoDrawer.this, MenuActivity.class);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            });

            int sizeSong = arrayListData.size();
            int sizeAlbum = arrayListAlbumImage.size();
            int sizePath = arrayListImagePath.size();
            DuoDrawer.sizeAlbum = sizeAlbum;
            DuoDrawer.sizeSong = sizeSong;
            DuoDrawer.sizePath = sizePath;

            for (int i = 0; i < num; i++)
                if (sizeSong - 1 >= position && sizeAlbum - 1 >= i && sizePath - 1 >= i)
                    if (arrayListSongImage.get(position) != null && arrayListAlbumImage.get(i) != null)
                        if (arrayListAlbumImage.get(i).equals(arrayListSongImage.get(position))) {
                            if (arrayListImagePath.get(i) != null)
                                Glide.with(DuoDrawer.this).load(arrayListImagePath.get(i)).into(iv);
                            else
                                Glide.with(DuoDrawer.this).load(R.drawable.note1).centerCrop().into(iv);
                        }

            return v;
        }
    }
}