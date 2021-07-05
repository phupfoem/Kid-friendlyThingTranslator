package com.example.main.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.main.R;
import com.example.main.data.model.Item;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;



public class HistoryActivity extends AppCompatActivity {
    RecyclerView simpleList; //change to Recycler View since its legacy now :(
    ArrayList<Item> list_of_item = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ArrayList<Item> list_of_item = new ArrayList<Item>();
        String dir_str = "history";
        File dir = new File(this.getFilesDir().getAbsolutePath() + "/history");
        setContentView(R.layout.history_layout);
        if (dir.exists()){
            try {
                FileInputStream fis = new FileInputStream(dir);
                ObjectInputStream is = new ObjectInputStream(fis);
                list_of_item = (ArrayList<Item>)is.readObject();
                is.close();
                fis.close();
            }
            catch(Exception e) {
                Log.e("exception 2", Log.getStackTraceString(e));
            }
            EditText search_bar = findViewById(R.id.history_search);

            simpleList = ((RecyclerView) this.findViewById(R.id.recycler_view1));
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            simpleList.setLayoutManager(mLayoutManager);
            HistoryAdapter adapter = new HistoryAdapter(list_of_item);
            adapter.addContext(this);
            simpleList.setAdapter(adapter);
            search_bar.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    ArrayList<Item> filteredList = new ArrayList<Item>();
                    for(Item item : adapter.getoItems()){
                        if(item.getLabel().toLowerCase().contains(s.toString().toLowerCase())){
                            filteredList.add(item);
                        }
                    }

                    adapter.filterList(filteredList);
                }
            });
        }
        else{
            Toast.makeText(this,"No History yet",Toast.LENGTH_LONG).show();
        }

    }
}