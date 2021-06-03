package com.example.main;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class HistoryActivity extends AppCompatActivity {
    RecyclerView simpleList; //change to Recycler View since its legacy now :(

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Item> list_of_item = new ArrayList<Item>();
        String dir_str = "history";
        File dir = new File(this.getFilesDir().getAbsolutePath() + "/history");
        if (dir.exists()){
            try {
                FileInputStream fis = new FileInputStream(dir);
                ObjectInputStream is = new ObjectInputStream(fis);
                list_of_item = (ArrayList<Item>)is.readObject();
                is.close();
                fis.close();
                Toast.makeText(this,"1",Toast.LENGTH_LONG).show();
            }
            catch(Exception e){
                Log.e("exception 2", Log.getStackTraceString(e));
            }

        }
        else{

        }
        setContentView(R.layout.history_layout);
        simpleList = ((RecyclerView)this.findViewById(R.id.recycler_view1));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        simpleList.setLayoutManager(mLayoutManager);
        HistoryAdapter adapter = new HistoryAdapter(list_of_item);
        simpleList.setAdapter(adapter);
    }
}