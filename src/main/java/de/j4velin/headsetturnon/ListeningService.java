package de.j4velin.headsetturnon;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;


public class ListeningService extends Service {

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) android.util.Log.d("HeadsetTurnOn", "service create");
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        if (BuildConfig.DEBUG) android.util.Log.d("HeadsetTurnOn", "service start");
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        am.registerMediaButtonEventReceiver(new ComponentName(this, Receiver.class));


        NotificationCompat.Builder b = new NotificationCompat.Builder(this);
        b.setOngoing(true).setContentText("Click to disable")
                .setContentTitle("HeadsetTurnOn active").setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(PendingIntent.getActivity(this, 0,
                        new Intent(this, Main.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0));

        startForeground(1, b.build());

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (BuildConfig.DEBUG) android.util.Log.d("HeadsetTurnOn", "service destroy");
        stopForeground(true);
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        am.unregisterMediaButtonEventReceiver(new ComponentName(this, Receiver.class));
    }
}
