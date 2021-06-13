package com.example.main.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.main.R;
import com.example.main.data.model.ImageDescription;
import com.example.main.data.model.Item;
import com.example.main.data.model.Result;
import com.example.main.util.ImageConverterUtil;
import com.example.main.viewmodel.LabelImageViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class CameraActivity extends AppCompatActivity {
    private final String dir_str = "history";
    private ArrayList<Item> items;

    private LabelImageViewModel labelImageViewModel;

    private Button btn;
    private ImageView myImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Get all history items
        File dir = new File(this.getFilesDir().getAbsolutePath() + "/" + dir_str);
        if (dir.exists()) {
            // If history file exists, read from Internal Storage
            items = readListItemFromFile(dir);
        }
        else {
            // Assign an empty list
            items = new ArrayList<>();
        }

        // Make a login view model
        labelImageViewModel = new ViewModelProvider(this).get(LabelImageViewModel.class);

        labelImageViewModel.getResult().observe(this, result -> {
            if (result instanceof Result.Error){
                Toast.makeText(this,
                        ((Result.Error<ImageDescription>) result).getError().getMessage(), Toast.LENGTH_SHORT
                ).show();
            }
            else if (result instanceof Result.Success) {
                Result.Success<ImageDescription> success = (Result.Success<ImageDescription>) result;
                Toast.makeText(this, success.getData().getLabel(), Toast.LENGTH_SHORT).show();

                //Add new item to history
                items.add(new Item(
                        success.getData().getImageBase64(),
                        success.getData().getLabel(),
                        success.getData().getDefinition(),
                        true)
                );

                this.writeListItemToFilename(items, dir_str);
            }
        });

        myImage = (ImageView) findViewById(R.id.myImage);

        btn = (Button) findViewById(R.id.button);
        btn.setText("Take picture");
        Toast.makeText(this,this.getFilesDir().getAbsolutePath(),Toast.LENGTH_LONG).show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

    }

    private void takePicture(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 0);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (resultCode == RESULT_OK){
            Bitmap b = (Bitmap)data.getExtras().get("data");
            myImage.setImageBitmap(b);
            //Saving items to history file in Internal Storage

            // Encode bitmap to string
            String imageBase64 = ImageConverterUtil.BitMapToString(b);

            // Send to server to label
            labelImageViewModel.labelImage(imageBase64);
        }
    }

    private ArrayList<Item> readListItemFromFile(File file)
    {
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream is = new ObjectInputStream(fis);

            ArrayList<Item> res = (ArrayList<Item>) is.readObject();

            is.close();
            fis.close();

            return res;
        }
        catch(Exception e){
            Log.e("exception encountered reading history file", Log.getStackTraceString(e));

            return new ArrayList<>();
        }
    }

    private void writeListItemToFilename(ArrayList<Item> items, String filename)
    {
        try {
            FileOutputStream fos = this.openFileOutput(filename, MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);

            os.writeObject(items);

            os.close();
            fos.close();
        }
        catch(Exception e){
            Log.e("exception encountered writing to history file", Log.getStackTraceString(e));
        }
    }
}
