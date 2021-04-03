package com.revel.experimentalmusicapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import static com.revel.experimentalmusicapp.MyService.mp;


public class OutgoingReceiver extends BroadcastReceiver {
    boolean playingWhileCalled = false;

    public OutgoingReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            if (mp.isPlaying()) {
                playingWhileCalled = true;
                mp.pause();
            } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE)) {
                if (playingWhileCalled) {
                    mp.start();
                }
            }
        }
    }
}
