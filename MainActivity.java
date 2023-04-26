package com.example.alzheimerhastatakip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;
    Button gorevlerBtn;
    Button ilaclarBtn;
    Button albumBtn;
    Button konumBtn;
    TextView mapDetails;
    LocationManager locationManager;
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  scheduleNotification(getNotification( "5 second delay" ) , 10000 ) ;

         gorevlerBtn = findViewById(R.id.gorevlerBtn);
         gorevlerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gorevlerIntent = new Intent(MainActivity.this,Gorevler.class);
                startActivity(gorevlerIntent);
            }
        });

        ilaclarBtn = findViewById(R.id.ilaclarBtn);
        ilaclarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ilaclarIntent = new Intent(MainActivity.this,Ilaclarim.class);
                startActivity(ilaclarIntent);
            }
        });

        albumBtn = findViewById(R.id.albumBtn);
        albumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent albumIntent = new Intent(MainActivity.this,AileAlbumum.class);
                startActivity(albumIntent);
            }
        });

        konumBtn = findViewById(R.id.konumBtn);
        konumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                mapDetails = findViewById(R.id.mapDetails);
                mapDetails.setText("Konumunuz: " + "\n" + "Enlem: " + String.valueOf(lat) + "\n" + "Boylam: " + String.valueOf(longi));
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void scheduleNotification (Notification notification , int delay) {
        Intent notificationIntent = new Intent( this, MyNotificationPublisher. class ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        long futureInMillis = SystemClock. elapsedRealtime () + delay ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , futureInMillis , pendingIntent) ;
    }
    private Notification getNotification (String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "Scheduled Notification" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        return builder.build() ;
    }


}
