package com.example.alzheimerhastatakip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class YeniAlbum extends AppCompatActivity {
    Button resimSec;
    Button saveBtn;
    EditText txtIsim;
    ImageView viewImage;
    String imageUri="";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yeni_album);
        setTitle("Yeni Kişi Ekle");
        viewImage=(ImageView)findViewById(R.id.viewImage);
        resimSec = findViewById(R.id.resimSecBtn);
        resimSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resimSec = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(resimSec,2);
            }
        });



        txtIsim  = findViewById(R.id.txtIsim);
        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("gorevler");
                AlbumModel album = new AlbumModel(
                        txtIsim.getText().toString(),imageUri

                );
                //myRef.child("aileabumu").setValue(album);
                db.collection("ailealbumu").add(album);
                Toast.makeText(getApplicationContext(),"Eklendi", Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of image from gallery......******************.........", picturePath + "");
                viewImage.setImageBitmap(thumbnail);
                Uri file = Uri.fromFile(new File(picturePath));
                StorageReference storageRef = storage.getReference();
                storageRef.putFile(file);
                imageUri=picturePath;

            }
        }
    }
}