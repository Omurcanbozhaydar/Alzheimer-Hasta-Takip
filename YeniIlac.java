package com.example.alzheimerhastatakip;

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
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class YeniIlac extends AppCompatActivity {
    Button saveBtn;
    Button gunSecBtn;
    Button saatSecBtn;
    EditText txtIlacAdi;
    EditText txtIlacGun;
    EditText txtIlacSaat;
    EditText txtIlacDoz;
    EditText txtIlacNasil;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Calendar calendar;
    private int year,month,dayOfMonth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yeni_ilac);
        setTitle("Yeni İlaç Ekle");
        txtIlacAdi = findViewById(R.id.txtIlacAdi);
        txtIlacGun = findViewById(R.id.txtIlacGun);
        txtIlacSaat  = findViewById(R.id.txtIlacSaat);
        txtIlacDoz  = findViewById(R.id.txtIlacDoz);
        txtIlacNasil  = findViewById(R.id.txtIlacNasil);

        saveBtn = findViewById(R.id.kaydetBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            //İLAÇ KAYDETME BUTONUNU DİNLİYORUZ
            // İLACI VERİTANINA KAYDEDİP BİLDİRİM İÇİN TAKVİME EKLEME YAPIYORUZ.
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                IlacModel ilaclar = new IlacModel(
                        txtIlacAdi.getText().toString(),
                        txtIlacGun.getText().toString(),
                        txtIlacSaat.getText().toString(),
                        txtIlacDoz.getText().toString(),
                        txtIlacNasil.getText().toString(),
                        0
                );
                String lastID;

                db.collection("ilaclar").add(ilaclar).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Toast.makeText(getApplicationContext(),"Eklendi: ", Toast.LENGTH_LONG).show();
                        String date = txtIlacGun.getText() + " " + txtIlacSaat.getText();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.ROOT);
                        LocalDateTime  localDate = LocalDateTime.parse(date, formatter);
                        calendar = Calendar.getInstance();
                        long timeInMilliseconds = localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
                        String splitGun[] = txtIlacGun.getText().toString().split("/");
                        int gun = Integer.parseInt(splitGun[0]);
                        int ay = Integer.parseInt(splitGun[1])-1;
                        int yil = Integer.parseInt(splitGun[2]);
                        String splitSaat[] = txtIlacSaat.getText().toString().split(":");
                        int saat = Integer.parseInt(splitSaat[0]);
                        int dk = Integer.parseInt(splitSaat[1]);
                        Calendar myAlarmDate = Calendar.getInstance();
                        myAlarmDate.setTimeInMillis(System.currentTimeMillis());
                        myAlarmDate.set(yil, Integer.parseInt(checkDigit(ay)), Integer.parseInt(checkDigit(gun)), Integer.parseInt(checkDigit(saat)), Integer.parseInt(checkDigit(dk)),0);
                        long difference = printDifference(calendar.getTime(),myAlarmDate.getTime());

                        scheduleNotification(getNotification(  "İlaç: "+txtIlacAdi.getText().toString(),documentReference.getId() ) , difference  ) ;


                    }
                });






            }
        });

        gunSecBtn = findViewById(R.id.gunSecBtn);
        gunSecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year=calendar.get(Calendar.YEAR);
                month=calendar.get(Calendar.MONTH);
                dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog= new DatePickerDialog(YeniIlac.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                txtIlacGun.setText(checkDigit(day)+"/"+checkDigit(month+1)+"/"+year);
                            }
                        },year,month,dayOfMonth);
                datePickerDialog.show();


            }
        });



        saatSecBtn = findViewById(R.id.saatSecBtn);
        saatSecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                timePickerDialog = new TimePickerDialog(YeniIlac.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        txtIlacSaat.setText(checkDigit(hour) + ":" + checkDigit(minute));
                    }
                },hour,minute,true);
                timePickerDialog.show();
            }}
        );



    }

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


        /*AlarmManager alarmManager1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent1 = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");

        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent1, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 5);
        alarmManager1.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);*/
    }
    private Notification getNotification (String content , String lastID) {
        Intent notIntent = new Intent(this, IlacDetails.class);
        notIntent.putExtra("IID",lastID);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,notIntent
                , PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "İlaç Hatırlatması" ) ;
        builder.setContentIntent(contentIntent);
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        return builder.build() ;
    }

}