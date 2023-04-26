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

public class AileAlbumum extends AppCompatActivity {
Button yeniAlbumBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aile_albumum);
        setTitle("Aile Albümüm");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        ListView dataListView= (ListView) findViewById(R.id.listViewAlbum);
        List<String> docIDS = new ArrayList<>();


        db.collection("ailealbumu").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<AlbumModel> albumList = new ArrayList<>();

                if(!queryDocumentSnapshots.isEmpty()){

                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot document :list){
                        docIDS.add(document.getId().toString());
                        AlbumModel photos = document.toObject(AlbumModel.class);
                        albumList.add(photos);
                    }

                    albumAdapter adapter = new albumAdapter(getApplicationContext(),albumList);
                    dataListView.setAdapter(adapter);
                }



            }
        });

        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                                    String passData = docIDS.get(i).trim().toString();
                                                    Intent albumDetails = new Intent(getBaseContext(),AlbumDetails.class);
                                                    albumDetails.putExtra("PID",passData);
                                                    startActivity(albumDetails);
                                                }
                                            }
        );


        yeniAlbumBtn = findViewById(R.id.yeniAlbumBtn);
        yeniAlbumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent yeniAlbum = new Intent(getApplicationContext(),YeniAlbum.class);
                startActivity(yeniAlbum);
            }
        });
    }
}