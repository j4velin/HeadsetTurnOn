/*
 * Copyright 2015 Thomas Hoffmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.j4velin.headsetturnon;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

public class ListeningService extends Service {

    private final AudioManager.OnAudioFocusChangeListener focusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(final int focusChange) {
                    if (BuildConfig.DEBUG)
                        android.util.Log.d("HeadsetTurnOn", "audioFocusChanged: " + focusChange);
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 500,
                                PendingIntent.getService(ListeningService.this, 0,
                                        new Intent(ListeningService.this, ListeningService.class),
                                        0));
                        stopSelf();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
                        am.registerMediaButtonEventReceiver(
                                new ComponentName(ListeningService.this, Receiver.class));
                    }

                }
            };

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
        boolean failed = am.requestAudioFocus(focusChangeListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_FAILED;
        am.registerMediaButtonEventReceiver(
                new ComponentName(ListeningService.this, Receiver.class));

        if (BuildConfig.DEBUG)
            android.util.Log.d("HeadsetTurnOn", "request audiofocus failed? " + failed);

        NotificationCompat.Builder b = new NotificationCompat.Builder(this);
        b.setOngoing(true).setContentText(getString(R.string.notification_text))
                .setContentTitle(getString(R.string.notification_title))
                .setSmallIcon(R.drawable.ic_notification).setContentIntent(PendingIntent
                .getActivity(this, 0,
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
        am.unregisterMediaButtonEventReceiver(
                new ComponentName(ListeningService.this, Receiver.class));
        am.abandonAudioFocus(focusChangeListener);
    }
}
