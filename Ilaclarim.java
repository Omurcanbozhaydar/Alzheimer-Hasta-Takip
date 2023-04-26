package com.example.alzheimerhastatakip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Ilaclarim extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Button newIlacBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilaclarim);
        setTitle("İlaçlarım");
        ListView dataListView= (ListView) findViewById(R.id.listIlaclar);
        List<String> docIDS = new ArrayList<>();


        db.collection("ilaclar").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<IlacModel> ilacList = new ArrayList<>();

                if(!queryDocumentSnapshots.isEmpty()){

                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot document :list){
                        docIDS.add(document.getId().toString());
                        IlacModel ilaclar = document.toObject(IlacModel.class);
                        ilacList.add(ilaclar);
                    }

                    ilacAdapter adapter = new ilacAdapter(getApplicationContext(),ilacList);
                    dataListView.setAdapter(adapter);
                }



            }
        });

        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                                    String passData = docIDS.get(i).trim().toString();
                                                    Intent ilacDetail = new Intent(getBaseContext(),IlacDetails.class);
                                                    ilacDetail.putExtra("IID",passData);
                                                    startActivity(ilacDetail);
                                                }
                                            }
        );







        newIlacBtn = findViewById(R.id.newIlacBtn);
        newIlacBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIlac= new Intent(Ilaclarim.this,YeniIlac.class);
                startActivity(newIlac);
            }
        });
    }
}