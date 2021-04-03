package com.revel.experimentalmusicapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.bumptech.glide.Glide;

import static com.revel.experimentalmusicapp.DuoDrawer.circleImage;
import static com.revel.experimentalmusicapp.DuoDrawer.equalizer;
import static com.revel.experimentalmusicapp.DuoDrawer.play;
import static com.revel.experimentalmusicapp.DuoDrawer.playPause;
import static com.revel.experimentalmusicapp.DuoDrawer.sizeSong;
import static com.revel.experimentalmusicapp.DuoDrawer.tabBarImage;
import static com.revel.experimentalmusicapp.DuoDrawer.titleView;
import static com.revel.experimentalmusicapp.MyService.findCurrImage;
import static com.revel.experimentalmusicapp.MyService.pp;
import static com.revel.experimentalmusicapp.SecondActivity.circleMenu;
import static com.revel.experimentalmusicapp.SecondActivity.currentImage;
import static com.revel.experimentalmusicapp.SecondActivity.iv;
import static com.revel.experimentalmusicapp.SecondActivity.onCurrentPage;
import static com.revel.experimentalmusicapp.SecondActivity.secondPage;
import static com.revel.experimentalmusicapp.SecondActivity.threadCanMove;
import static com.revel.experimentalmusicapp.SecondActivity.vusikView;
import static com.revel.experimentalmusicapp.Splash.arrayListTitle;
import static com.revel.experimentalmusicapp.MyService.position;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (position > 0)
            --position;
        else
            position = sizeSong - 1;
        Intent intent1 = new Intent(context, MyService.class);
        context.stopService(intent1);
        intent1.putExtra("position", position);
        pp = 0;
        playPause = 1;
        if (onCurrentPage||secondPage) {
            titleView.setText(arrayListTitle.get(position));
            currentImage = findCurrImage();
            if (currentImage != null) {
                Glide.with(context).load(currentImage).circleCrop().into(circleImage);
                Glide.with(context).load(currentImage).centerCrop().into(tabBarImage);
            } else {
                Glide.with(context).load(R.drawable.album).circleCrop().into(circleImage);
                Glide.with(context).load(R.drawable.album).centerCrop().into(tabBarImage);
            }
            play.setBackgroundResource(R.drawable.ic_pause);
            equalizer.animateBars();
            if (secondPage) {
                if (currentImage != null) {
                    Glide.with(context).load(currentImage).circleCrop().into(iv);
                } else {
                    Glide.with(context).load(R.drawable.album).circleCrop().into(iv);
                }
                SecondActivity.i = 0;
                threadCanMove = true;
                vusikView.resumeNotesFall();
                circleMenu.setMainMenu(Color.parseColor("#8109094E"), R.drawable.ic_pause, R.drawable.ic_pause);
            }
        }
        context.startService(intent1);
    }
}
