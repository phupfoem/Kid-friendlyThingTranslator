package com.example.main.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.main.R;
import com.example.main.data.model.Item;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private ArrayList<Item> items;
    private ArrayList<Item> oitems; // backup using for filter/search
    private Context context;
    public FavoriteAdapter(ArrayList<Item> items) {
        this.items = items;
        this.oitems = new ArrayList<Item>(items);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.name.setText(item.getLabel());
        holder.image.setImageBitmap(StringToBitMap(item.getImage()));
        holder.favorite.setText("Unfavorite");
        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Unfavorite");
                alert.setMessage("Are you sure you want to unfavorite " + item.getLabel() + "?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Open history using items2
                        ArrayList<Item> items2 = new ArrayList<Item>();
                        String dir_str = "history";
                        File dir = new File(context.getFilesDir().getAbsolutePath() + "/history");
                        if(dir.exists()) {
                            try {
                                FileInputStream fis = new FileInputStream(dir);
                                ObjectInputStream is = new ObjectInputStream(fis);
                                items2 = (ArrayList<Item>) is.readObject();
                                is.close();
                                fis.close();
                            }catch(Exception e){
                                Log.e("exception 1 in FavoriteAdapter", Log.getStackTraceString(e));
                            }
                        }
                        else {
                            Toast.makeText(context,"Someone deleted history file",Toast.LENGTH_LONG);
                        }
                        for (int i = 0; i < items2.size();i++){
                            if(items2.get(i).equals(item)){
                                items2.get(i).setFavorite(false);
                                try {
                                    FileOutputStream fos = context.openFileOutput(dir_str, context.MODE_PRIVATE);
                                    ObjectOutputStream os = new ObjectOutputStream(fos);
                                    os.writeObject(items2);
                                    os.close();
                                    fos.close();
                                } catch (Exception e) {
                                    Log.e("exception 1 in FavoriteAdapter", Log.getStackTraceString(e));
                                }
                                break;
                            }
                        }
                        //oitems.remove(position);
                        for (int i = 0; i < oitems.size();i++){
                            if (oitems.get(i).equals(item)){
                                oitems.remove(i);
                                break;
                            }
                        }
                        items.remove(position);
                        notifyDataSetChanged();
                        //Intent refresh = new Intent(context, FavoriteActivity.class);
                        //context.startActivity(refresh);
                        //((Activity)context).finish();
                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Delete");
                alert.setMessage("Are you sure you want to delete " + item.getLabel() + "?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Open history using items2
                        ArrayList<Item> items2 = new ArrayList<Item>();
                        String dir_str = "history";
                        File dir = new File(context.getFilesDir().getAbsolutePath() + "/history");
                        if(dir.exists()) {
                            try {
                                FileInputStream fis = new FileInputStream(dir);
                                ObjectInputStream is = new ObjectInputStream(fis);
                                items2 = (ArrayList<Item>) is.readObject();
                                is.close();
                                fis.close();
                            }catch(Exception e){
                                Log.e("exception 1 in FavoriteAdapter", Log.getStackTraceString(e));
                            }
                        }
                        else {
                            Toast.makeText(context,"Someone deleted history file",Toast.LENGTH_LONG);
                        }
                        for (int i = 0; i < items2.size();i++){
                            if(items2.get(i).equals(item)){
                                items2.remove(i);
                                try {
                                    FileOutputStream fos = context.openFileOutput(dir_str, context.MODE_PRIVATE);
                                    ObjectOutputStream os = new ObjectOutputStream(fos);
                                    os.writeObject(items2);
                                    os.close();
                                    fos.close();
                                } catch (Exception e) {
                                    Log.e("exception 1 in HistoryAdapter", Log.getStackTraceString(e));
                                }
                                break;
                            }
                        }
                        //oitems.remove(position);
                        for (int i = 0; i < oitems.size();i++){
                            if (oitems.get(i).equals(item)){
                                oitems.remove(i);
                                break;
                            }
                        }
                        items.remove(position);
                        notifyDataSetChanged();
                        //Intent refresh = new Intent(context, FavoriteActivity.class);
                        //context.startActivity(refresh);
                        //((Activity)context).finish();
                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();

            }
        });
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TextToSpeechActivity.class);
                intent.putExtra("item", item);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView name;

        public final ImageView image;
        public final Button favorite;
        public final Button delete;
        public final LinearLayout linearLayout;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.item_name);

            image = view.findViewById(R.id.item_image);
            favorite = view.findViewById(R.id.favorite_button);
            delete = view.findViewById(R.id.delete_button);
            linearLayout = view.findViewById(R.id.item_layout);
        }
    }
    public void addContext(Context context){
        this.context = context;
    }

    public void filterList(ArrayList<Item> filteredList){
        items = filteredList;
        notifyDataSetChanged();
    }
    public ArrayList<Item> getItems(){
        return items;
    }
    public ArrayList<Item> getoItems(){
        return oitems;
    }
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
}

