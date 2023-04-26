package com.example.alzheimerhastatakip;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Console;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class YeniGorev extends AppCompatActivity {
    Button saveBtn;
    Button gorevGunBtn;
    Button gorevSaatBtn;
    EditText txtGorevAdi;
    EditText txtGorevGun;
    EditText txtGorevSaat;
    EditText txtGorevTekrar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Calendar calendar;
    private int year,month,dayOfMonth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;


    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }
    public long printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        return different;

    }

    private void scheduleNotification (android.app.Notification notification , long difference) {
        Intent notificationIntent = new Intent( this, MyNotificationPublisher. class ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        long futureInMillis = SystemClock. elapsedRealtime () + difference ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;

        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , futureInMillis , pendingIntent) ;

    }
    private Notification getNotification (String content) {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, Gorevler.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "Görev Hatırlatması" ) ;
        builder.setContentText(content) ;
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        return builder.build() ;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yeni_gorev);
        setTitle("Yeni Görev Ekle");
        txtGorevAdi = findViewById(R.id.txtGorevAdi);
        txtGorevGun = findViewById(R.id.txtGorevGun);
        txtGorevSaat  = findViewById(R.id.txtGorevSaat);
        txtGorevTekrar  = findViewById(R.id.txtGorevTekrar);
        saveBtn = findViewById(R.id.kaydetBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("gorevler");
                GorevlerModel gorevler = new GorevlerModel(
                        txtGorevAdi.getText().toString(),
                        txtGorevGun.getText().toString(),
                        txtGorevSaat.getText().toString(),
                        txtGorevTekrar.getText().toString()
                );
                myRef.child("gorev").setValue(gorevler);
                db.collection("gorevler").add(gorevler);
                Toast.makeText(getApplicationContext(),"Eklendi", Toast.LENGTH_LONG).show();
                String date = txtGorevGun.getText() + " " + txtGorevSaat.getText();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.ROOT);
                LocalDateTime localDate = LocalDateTime.parse(date, formatter);
                calendar = Calendar.getInstance();
                long timeInMilliseconds = localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
                String splitGun[] = txtGorevGun.getText().toString().split("/");
                int gun = Integer.parseInt(splitGun[0]);
                int ay = Integer.parseInt(splitGun[1])-1;
                int yil = Integer.parseInt(splitGun[2]);
                String splitSaat[] = txtGorevSaat.getText().toString().split(":");
                int saat = Integer.parseInt(splitSaat[0]);
                int dk = Integer.parseInt(splitSaat[1]);
                Calendar myAlarmDate = Calendar.getInstance();
                myAlarmDate.setTimeInMillis(System.currentTimeMillis());
                myAlarmDate.set(yil, Integer.parseInt(checkDigit(ay)), Integer.parseInt(checkDigit(gun)), Integer.parseInt(checkDigit(saat)), Integer.parseInt(checkDigit(dk)),0);
                long difference = printDifference(calendar.getTime(),myAlarmDate.getTime());

                scheduleNotification(getNotification(  "Görev: "+txtGorevAdi.getText().toString() ) , difference ) ;



            }
        });

       /* saveBtn.findViewById(R.id.kaydetBtn);
        saveBtn.setOnClickListener(view -> {

        });*/




        gorevGunBtn = findViewById(R.id.gorevGunBtn);
        gorevGunBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year=calendar.get(Calendar.YEAR);
                month=calendar.get(Calendar.MONTH);
                dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog= new DatePickerDialog(YeniGorev.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                txtGorevGun.setText(checkDigit(day)+"/"+checkDigit(month+1)+"/"+year);

                            }
                        },year,month,dayOfMonth);
                datePickerDialog.show();


            }
        });



        gorevSaatBtn = findViewById(R.id.gorevSaatBtn);
        gorevSaatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                timePickerDialog = new TimePickerDialog(YeniGorev.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        txtGorevSaat.setText(checkDigit(hour) + ":" + checkDigit(minute));
                    }
                },hour,minute,true);
                timePickerDialog.show();
            }}
        );
    }
}