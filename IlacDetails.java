package com.example.alzheimerhastatakip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class IlacDetails extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView ilacAdi;
    TextView ilacGun;
    TextView ilacSaat;
    TextView ilacDoz;
    TextView ilacNasil;
    TextView ilacState;
    Button ilacSil;
    Button ilacSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilac_details);
        setTitle("İlaç Detayları");
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String IID = extra.getString("IID");
            Log.d("TAG", "İlaç ID: : " + IID);


            db.collection("ilaclar").document(IID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.d("TAG", "Data: " + documentSnapshot.get("ilacAdi"));

                    ilacAdi = findViewById(R.id.ilacAdi);
                    ilacAdi.setText(documentSnapshot.get("ilacAdi").toString());

                    ilacGun = findViewById(R.id.ilacGun);
                    ilacGun.setText(documentSnapshot.get("ilacGun").toString());

                    ilacSaat = findViewById(R.id.ilacSaat);
                    ilacSaat.setText(documentSnapshot.get("ilacSaat").toString());

                    ilacDoz = findViewById(R.id.ilacDoz);
                    ilacDoz.setText(documentSnapshot.get("ilacDoz").toString());
                    String state ="";
                    Log.d("TAG", "onSuccess: "+ documentSnapshot.get("ilacResult").toString());
                    if(Integer.parseInt(documentSnapshot.get("ilacResult").toString()) == 0){
                        state="İçilmemiş";
                    }else{
                        state="İçilmiş";
                    }

                    ilacState = findViewById(R.id.ilacState);
                    ilacState.setText(state);
                    ilacNasil = findViewById(R.id.ilacNasil);
                    ilacNasil.setText(documentSnapshot.get("ilacNasil").toString());

                }
            });


            ilacSil = findViewById(R.id.ilacSil);

            ilacSil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.collection("ilaclar").document(IID).delete();
                    Toast.makeText(getApplication(), "Silindi", Toast.LENGTH_LONG).show();
                    Intent ilaclar = new Intent(getBaseContext(), Ilaclarim.class);
                    startActivity(ilaclar);
                }
            });

            ilacSuccess = findViewById(R.id.ilacSuccess);
            ilacSuccess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplication(), "Güncellendi", Toast.LENGTH_LONG).show();

                    db.collection("ilaclar").document(IID).update("ilacResult",1);



                }
            });

        }
    }
}