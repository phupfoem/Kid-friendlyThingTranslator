package com.example.main.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.main.R;

public class MainActivity extends AppCompatActivity {
    Button btnFav, btnRec, btnHis, btnCam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homelayout);
        //Favorite List
        btnFav = (Button) findViewById(R.id.favoriteslist);
        btnFav.setText("Favorites List");
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFavorite();
            }
        });

        //Rec words
        /*btnRec = (Button) findViewById(R.id.recwords);
        btnRec.setText("Words of the week");*/

        //History search
        btnHis = (Button) findViewById(R.id.historysearch);
        btnHis.setText("History Search");
        btnHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHistory();
            }
        });

        //Camera
        btnCam = (Button) findViewById(R.id.camera);
        btnCam.setText("Take a picture");
        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
    }
    public void openCamera(){
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }
    public void openHistory(){
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }
    public void openFavorite(){
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
    }
}