package com.revel.experimentalmusicapp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.widget.Toast;

import static com.revel.experimentalmusicapp.DuoDrawer.equalizer;
import static com.revel.experimentalmusicapp.DuoDrawer.firstIntent;
import static com.revel.experimentalmusicapp.DuoDrawer.play;
import static com.revel.experimentalmusicapp.DuoDrawer.playPause;
import static com.revel.experimentalmusicapp.DuoDrawer.tempPosition;
import static com.revel.experimentalmusicapp.MyService.position;
import static com.revel.experimentalmusicapp.MyService.pp;
import static com.revel.experimentalmusicapp.SecondActivity.circleMenu;
import static com.revel.experimentalmusicapp.SecondActivity.currentImage;
import static com.revel.experimentalmusicapp.SecondActivity.duration;
import static com.revel.experimentalmusicapp.SecondActivity.onCurrentPage;
import static com.revel.experimentalmusicapp.SecondActivity.secondPage;
import static com.revel.experimentalmusicapp.SecondActivity.vusikView;

public class MyReceiver4 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, MyService.class);
        playPause = 0;
        firstIntent = false;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("position", position);
        editor.putInt("progress", SecondActivity.i);
        editor.putString("currImage", currentImage);
        editor.putInt("duration", duration);
        editor.apply();
        if (!onCurrentPage) {
            //Toast.makeText(context, "please Wait a second", Toast.LENGTH_SHORT).show();
            tempPosition = position;
            NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nMgr.cancel(1);
            context.stopService(intent1);
        } else {
            intent1.putExtra("playPause", -1);
            play.setBackgroundResource(R.drawable.ic_play);
            equalizer.stopBars();
            if (secondPage) {
                vusikView.pauseNotesFall();
                circleMenu.setMainMenu(Color.parseColor("#8109094E"), R.drawable.ic_play, R.drawable.ic_play);
            }
            pp = 1;
            context.startService(intent1);
        }

    }
}
