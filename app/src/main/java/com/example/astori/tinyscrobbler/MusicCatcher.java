package com.example.astori.tinyscrobbler;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import 	android.util.Log;
import android.os.IBinder;
import android.support.annotation.Nullable;


public class MusicCatcher extends Service {
    private static boolean isRegistered = false;
    private static BroadcastReceiver receiver;
    private static Context context;
    private static TrackChangedListener listener;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        register(getApplicationContext());
        // register listener
        setListener(new TrackChangedListener() {
            @Override
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public void trackChanged(Intent intent) {
                Intent notificationIntent = new Intent(getApplicationContext(), ScrollingActivity.class);
                String artist = getFromIntent(intent, "artist");
                String track = getFromIntent(intent, "track");
                if (track != null && artist != null) {
                    notificationIntent.putExtra("artist", artist);
                    notificationIntent.putExtra("track", track);
                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingNotificationIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    Notification n = new Notification.Builder(getApplicationContext())
                            .setContentTitle(artist + " - " + track + " is now playing")
                            .setContentText("Tap to read lyrics")
                            .setSmallIcon(R.drawable.musical)
                            .setContentIntent(pendingNotificationIntent)
                            .build();
                    n.flags |= Notification.FLAG_AUTO_CANCEL;
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(0, n);
                }
            }
        });
}

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("AutoNowPlayingService", "Stopped Service");

        unRegister(getApplicationContext());
        setListener(null);
    }
    private void register(Context c) {
          boolean isRegistered = false;
          BroadcastReceiver receiver;
          Context context;
        if (isRegistered) {
            return;
        }
        context = c;
        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                if ( listener != null )
                {
                    listener.trackChanged(intent);
                }

            }
        };
        IntentFilter iF = new IntentFilter();
        // stock android
        iF.addAction("com.android.music.metachanged");
        iF.addAction("com.android.music.playstatechanged");
        iF.addAction("com.android.music.playbackcomplete");
        iF.addAction("com.android.music.queuechanged");
        iF.addAction("soundbar.music.metachanged");
        c.registerReceiver(receiver, iF);
    }
    public void setListener (TrackChangedListener li)
    {
        listener = li;
    }

    public static void unRegister(Context c)
    {
        if (isRegistered)
        {
            c.unregisterReceiver(receiver);
            isRegistered = false;
        }

    }
    public static String getFromIntent(Intent intent, String name)
    {
        if (intent.getStringExtra(name) != null)
        {
            return intent.getStringExtra(name);
        }
        else
        {
            return null;
        }
    }

}
