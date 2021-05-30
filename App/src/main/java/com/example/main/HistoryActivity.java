package com.example.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class HistoryActivity extends AppCompatActivity {
    ListView simpleList; //change to Recycler View since its legacy now :(
    String[] countryList = {"India", "China", "australia", "Portugle", "America", "NewZealand","1","2","3","4","5","6","7"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);
        simpleList = ((ListView)this.findViewById(R.id.list_view1));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countryList);
        simpleList.setAdapter(arrayAdapter);
    }
}