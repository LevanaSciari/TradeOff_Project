package com.example.tradeoff;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;







import android.content.Context;
import android.os.Handler;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Timer;
import java.util.TimerTask;



public class notification extends Service{
    Timer timer;
    TimerTask timerTask;
    String TAG = "Timers";
    int Your_X_SECS = 5;
    DatabaseReference database;
    String email="";
    String email_Point="";
    boolean flag=true;
    boolean connection=true;
    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        startTimer();

        Bundle bundle = new Bundle();
        Bundle extra_exit = intent.getExtras();
        email="";
        email_Point=extra_exit.getString("email");
        connection = extra_exit.getBoolean("connection");
        for(int i = 0 ; i <email_Point.length() ; i++){
            if(email_Point.charAt(i)=='.'){
                email+='_';
            }else{
                email+=email_Point.charAt(i);
            }
        }
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        Log.e(TAG, "onCreate");
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        stoptimertask();
        super.onDestroy();
    }

    //We are going to use a handler to be able to run in our TimerTask.
    final Handler handler = new Handler();

    public void startTimer()
    {
        //Sets a new Timer.
        timer = new Timer();

        //Initializes the TimerTask's job.
        initializeTimerTask();

        //Schedules the timer, after the first 5000ms the TimerTask will run every 10000ms.
        timer.schedule(timerTask, 5000, Your_X_SECS * 1000);
    }

    public void stoptimertask()
    {
        //Stops the timer, if it's not already null.
        if (timer != null)
        {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask()
    {
        timerTask = new TimerTask()
        {
            public void run()
            {
                //Uses an handler to run a toast that shows the current timestamp.
                handler.post(new Runnable()
                {
                    public void run()
                    {

                        database = FirebaseDatabase.getInstance().getReference();

                        database.child("Like").child(email).addChildEventListener(new ChildEventListener()
                        {
                            @Override
                            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s)
                            {
//                                if(connection==false){ // first time
//                                    connection=true;
//                                }else
                               if(flag==true) { //first time after connectiion
                                    showNotification(dataSnapshot.getKey() + " send you a Like", "" + "");
                                    flag = false;
                                }
                            }

                            @Override
                            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s)
                            {

                                showNotification(dataSnapshot.getKey() +" send you a Like",  "");
                            }

                            @Override
                            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot)
                            {
                            }

                            @Override
                            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s)
                            {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError)
                            {
                            }
                        });
                    }
                });
            }
        };
    }

    //-------------------------Notification-------------------------
    void showNotification(String title, String message)
    {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("1", "Message", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Message");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "1")
                .setSmallIcon(R.mipmap.ic_launcher) //Notification icon.
                .setContentTitle(title) //Title for notification.
                .setContentText(message)//Message for notification.
                .setAutoCancel(true); //Clears notification after click.


        Intent intent = new Intent(getApplicationContext(), MyNotificationActivity.class);
        intent.putExtra("email",email);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mBuilder.setOngoing(true);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
