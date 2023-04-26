package com.example.alzheimerhastatakip;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AlbumDetails extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView albumAdi;
    ImageView albumResim;
    Button albumSil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);


        setTitle("Fotoğraf Detayları");
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String PID = extra.getString("PID");
            Log.d("TAG", "Photo ID: : " + PID);


            db.collection("ailealbumu").document(PID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.d("TAG", "Data: " + documentSnapshot.get("isim"));

                    albumAdi = findViewById(R.id.albumAdi);
                    albumAdi.setText(documentSnapshot.get("isim").toString());


                    albumResim = findViewById(R.id.albumResim);
                    albumResim.setImageURI(Uri.parse(documentSnapshot.get("resim").toString()));


                }
            });


            albumSil = findViewById(R.id.albumSil);

            albumSil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.collection("ailealbumu").document(PID).delete();
                    Toast.makeText(getApplication(), "Silindi", Toast.LENGTH_LONG).show();
                    Intent ailealbumu = new Intent(getBaseContext(), AileAlbumum.class);
                    startActivity(ailealbumu);
                }
            });


        }
    }

}