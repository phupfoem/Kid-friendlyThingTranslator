package com.example.main;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button btnFav, btnRec, btnHis, btnCam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homelayout);
        //Favorite List
        btnFav = (Button) findViewById(R.id.favoriteslist);
        btnFav.setText("Favorites List");

        //Rec words
        btnRec = (Button) findViewById(R.id.recwords);
        btnRec.setText("Words of the week");

        //History search
        btnHis = (Button) findViewById(R.id.historysearch);
        btnHis.setText("History Search");

        //Camera
        btnCam = (Button) findViewById(R.id.camera);
        btnCam.setText("Take a picture");



    }
}