package de.j4velin.headsetturnon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.view.KeyEvent;

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (BuildConfig.DEBUG) android.util.Log.d("HeadsetTurnOn", intent.getAction());
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {

            KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (event == null) {
                if (BuildConfig.DEBUG) android.util.Log.d("HeadsetTurnOn", "event null");
                return;
            }
            int action = event.getAction();
            if (action == KeyEvent.ACTION_DOWN) {
                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                if (BuildConfig.DEBUG)
                    android.util.Log.d("HeadsetTurnOn", "down, screen: " + pm.isScreenOn());
                if (pm.isScreenOn()) {
                    if (BuildConfig.DEBUG) android.util.Log.d("HeadsetTurnOn", "is on");
                    AdminReceiver.lock(context);
                } else {
                    if (BuildConfig.DEBUG) android.util.Log.d("HeadsetTurnOn", "turning on");
                    context.startActivity(new Intent(context, OnActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }
        }
    }
}
