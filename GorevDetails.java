package com.example.alzheimerhastatakip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GorevDetails extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView gorevAdi;
    TextView gorevGun;
    TextView gorevSaat;
    TextView gorevTekrar;
    Button gorevSil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gorev_details);
        setTitle("Görev Detayları");
        Bundle extra = getIntent().getExtras();
        if(extra!=null){
            String gID = extra.getString("gID");
            Log.d("TAG", "Gorev ID: : "+gID);




            db.collection("gorevler").document(gID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.d("TAG", "Data: "+ documentSnapshot.get("gorevAdi"));

                    gorevAdi = findViewById(R.id.gorevAdi);
                    gorevAdi.setText(documentSnapshot.get("gorevAdi").toString());

                    gorevGun = findViewById(R.id.gorevGun);
                    gorevGun.setText(documentSnapshot.get("gorevGun").toString());

                    gorevSaat = findViewById(R.id.gorevSaat);
                    gorevSaat.setText(documentSnapshot.get("gorevSaat").toString());

                    gorevTekrar = findViewById(R.id.gorevTekrar);
                    gorevTekrar.setText(documentSnapshot.get("gorevTekrar").toString());


                }
            });


            gorevSil = findViewById(R.id.gorevSil);

            gorevSil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.collection("gorevler").document(gID).delete();
                    Toast.makeText(getApplication(),"Silindi", Toast.LENGTH_LONG).show();
                    Intent gorevler = new Intent(getBaseContext(),Gorevler.class);
                    startActivity(gorevler);
                }
            });





        }
    }
}