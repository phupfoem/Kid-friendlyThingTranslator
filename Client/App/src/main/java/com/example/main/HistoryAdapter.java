package com.example.main;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private ArrayList<Item> items;
    private Context context;
    public HistoryAdapter(ArrayList<Item> items) {
        this.items = items;
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

        holder.name.setText(item.getName());
        holder.description.setText(item.getDescription());
        holder.image.setImageBitmap(StringToBitMap(item.getImage()));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Delete");
                alert.setMessage("Are you sure you want to delete " + item.getName() + "?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        items.remove(position);
                        try {
                            String dir_str = "history";
                            FileOutputStream fos = context.openFileOutput(dir_str, context.MODE_PRIVATE);
                            ObjectOutputStream os = new ObjectOutputStream(fos);
                            os.writeObject(items);
                            os.close();
                            fos.close();
                        }
                        catch(Exception e){
                            Log.e("exception 1 in HistoryAdapter", Log.getStackTraceString(e));
                        }
                        notifyDataSetChanged();

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
        holder.linearLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context, TextToSpeech.class);
                intent.putExtra("item",item);
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
        public final TextView description;
        public final ImageView image;
        public final Button delete;
        public final LinearLayout linearLayout;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.item_name);
            description = view.findViewById(R.id.item_description);
            image = view.findViewById(R.id.item_image);
            delete = view.findViewById(R.id.delete_button);
            linearLayout = view.findViewById(R.id.item_layout);
        }
    }
    public void addContext(Context context){
        this.context = context;
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
