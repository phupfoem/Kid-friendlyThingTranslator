package com.example.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Camera extends AppCompatActivity {
    private Button btn;
    private ImageView myImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

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
    /**
     * @param bitmap
     * @return converting bitmap and return a string
     */
    public static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public static Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    private void takePicture(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 0);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode ==RESULT_OK){
            Bitmap b = (Bitmap)data.getExtras().get("data");
            myImage.setImageBitmap(b);
            //Saving items to history file in Internal Storage
            ArrayList<Item> list_of_item = new ArrayList<Item>();
            String dir_str = "history";
            File dir = new File(this.getFilesDir().getAbsolutePath() + "/history");
            if (dir.exists()){ //Check if history file exists? in Internal Storage
                try {
                    //Open history file
                    FileInputStream fis = new FileInputStream(dir);
                    ObjectInputStream is = new ObjectInputStream(fis);
                    list_of_item = (ArrayList<Item>)is.readObject();
                    is.close();
                    fis.close();
                    //Add new item to history
                    list_of_item.add(new Item(BitMapToString(b),"test","testing1",false));
                    try {
                        FileOutputStream fos = this.openFileOutput(dir_str, this.MODE_PRIVATE);
                        ObjectOutputStream os = new ObjectOutputStream(fos);
                        os.writeObject(list_of_item);
                        os.close();
                        fos.close();
                    }
                    catch(Exception e){
                        Log.e("exception 1", Log.getStackTraceString(e));
                    }
                    Toast.makeText(this,"1",Toast.LENGTH_LONG).show();
                }
                catch(Exception e){
                    Log.e("exception 2", Log.getStackTraceString(e));
                }
            }
            else{
                try {
                    //add item to the list_of_item (list_of_item first item tho)
                    list_of_item.add(new Item(BitMapToString(b),"test","testing1",false));
                    //Create history file in Internal Storage
                    FileOutputStream fos = this.openFileOutput(dir_str, this.MODE_PRIVATE);
                    ObjectOutputStream os = new ObjectOutputStream(fos);
                    os.writeObject(list_of_item);
                    os.close();
                    fos.close();
                    Toast.makeText(this,"2",Toast.LENGTH_LONG).show();
                }
                catch(Exception e){
                    Log.e("exception 3", Log.getStackTraceString(e));
                }
            }
        }
    }
}
