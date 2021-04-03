package com.revel.experimentalmusicapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import static com.revel.experimentalmusicapp.DuoDrawer.playPause;
import static com.revel.experimentalmusicapp.MyService.isPlaying;
import static com.revel.experimentalmusicapp.MyService.mp;

public class CallReceiver extends BroadcastReceiver {

    TelephonyManager telManager;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private final PhoneStateListener phoneListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            try {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING: {

                        if (mp.isPlaying()) {
                            SecondActivity.vusikView.pauseNotesFall();
                            mp.pause();
                        }
                        break;
                    }
                    case TelephonyManager.CALL_STATE_OFFHOOK: {

                        break;
                    }
                    case TelephonyManager.CALL_STATE_IDLE: {
                        if (playPause == 1||isPlaying) {
                            mp.start();
                            SecondActivity.vusikView.resumeNotesFall();
                        }
                        break;
                    }
                    default: {
                    }
                }
            } catch (Exception ex) {

            }
        }
    };
}
