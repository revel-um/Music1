package com.revel.experimentalmusicapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import static com.revel.experimentalmusicapp.DuoDrawer.equalizer;
import static com.revel.experimentalmusicapp.DuoDrawer.play;
import static com.revel.experimentalmusicapp.DuoDrawer.playPause;
import static com.revel.experimentalmusicapp.MyService.isPlaying;
import static com.revel.experimentalmusicapp.MyService.position;
import static com.revel.experimentalmusicapp.SecondActivity.circleMenu;
import static com.revel.experimentalmusicapp.SecondActivity.onCurrentPage;
import static com.revel.experimentalmusicapp.SecondActivity.secondPage;


public class MyReceiver2 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DuoDrawer.tempPosition = position;
        playPause = 0;
        Intent intent1 = new Intent(context, MyService.class);
        if (isPlaying) {
            position = DuoDrawer.tempPosition;
            intent1.putExtra("position", position);
            intent1.putExtra("playPause", -1);
            if (onCurrentPage) {
                play.setBackgroundResource(R.drawable.ic_play);
                equalizer.stopBars();
                if (secondPage) {
                    SecondActivity.vusikView.pauseNotesFall();
                    circleMenu.setMainMenu(Color.parseColor("#B9E4DF"), R.drawable.ic_play, R.drawable.ic_play);
                }
            }
            MyService.pp = 1;
            context.startService(intent1);
        } else {
            intent1.putExtra("position", position);
            intent1.putExtra("playPause", -2);
            if (onCurrentPage) {
                play.setBackgroundResource(R.drawable.ic_pause);
                equalizer.animateBars();
                if (secondPage) {
                    SecondActivity.vusikView.resumeNotesFall();
                    circleMenu.setMainMenu(Color.parseColor("#B9E4DF"), R.drawable.ic_pause, R.drawable.ic_pause);
                }
            }
            MyService.pp = 0;
            context.startService(intent1);
        }
    }
}