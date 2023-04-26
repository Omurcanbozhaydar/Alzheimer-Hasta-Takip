package com.example.alzheimerhastatakip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Gorevler extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    Button yeniGorevBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gorevler);
        setTitle("Görevler");
        ListView dataListView= (ListView) findViewById(R.id.listGorevler);
        List<String> docIDS = new ArrayList<>();


        db.collection("gorevler").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<GorevlerModel> gorevList = new ArrayList<>();

                        if(!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot document :list){
                                docIDS.add(document.getId().toString());
                                GorevlerModel gorevler = document.toObject(GorevlerModel.class);
                                gorevList.add(gorevler);
                            }

                            gorevAdapter adapter = new gorevAdapter(getApplicationContext(),gorevList);
                            dataListView.setAdapter(adapter);
                        }



                    }
                });

        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String passData = docIDS.get(i).trim().toString();
                Intent gorevDetail = new Intent(getBaseContext(),GorevDetails.class);
                gorevDetail.putExtra("gID",passData);
                startActivity(gorevDetail);
            }
        }
        );






        yeniGorevBtn = findViewById(R.id.yeniGorevBtn);
        yeniGorevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent yeniGorevIntent = new Intent(Gorevler.this,YeniGorev.class);
                startActivity(yeniGorevIntent);
            }
        });

    }
}