package com.example.main.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.main.util.ImageConverterUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private ArrayList<Item> items;
    private final ArrayList<Item> oitems; // backup using for filter/search
    private Context context;
    public HistoryAdapter(ArrayList<Item> items) {
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

        holder.image.setImageBitmap(ImageConverterUtil.StringToBitMap(item.getImage()));
        if(!item.getFavorite()){
            holder.favorite.setText("Favorite");
            holder.favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < oitems.size();i++){
                        if (oitems.get(i).equals(item)){
                            oitems.get(i).setFavorite(true);
                            break;
                        }
                    }
                    items.get(position).setFavorite(true);
                    //oitems.get(position).setFavorite(true);
                    String dir_str = "history";
                    File dir = new File(context.getFilesDir().getAbsolutePath() + "/history");
                    if(dir.exists()) {
                        try {
                            FileOutputStream fos = context.openFileOutput(dir_str, Context.MODE_PRIVATE);
                            ObjectOutputStream os = new ObjectOutputStream(fos);
                            os.writeObject(oitems); // change items to oitems here
                            os.close();
                            fos.close();
                        } catch (Exception e) {
                            Log.e("exception 1 in HistoryAdapter", Log.getStackTraceString(e));
                        }
                    }
                    else{
                        Toast.makeText(context,"someone delete history in HistoryAdapter",Toast.LENGTH_LONG).show();
                    }
                    notifyDataSetChanged();
                }
            });
        }
        else{
            holder.favorite.setText("Unfavorite");
            holder.favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < oitems.size();i++){
                        if (oitems.get(i).equals(item)){
                            oitems.get(i).setFavorite(false);
                            break;
                        }
                    }
                    items.get(position).setFavorite(false);
                    //oitems.get(position).setFavorite(false);
                    String dir_str = "history";
                    File dir = new File(context.getFilesDir().getAbsolutePath() + "/history");
                    if(dir.exists()) {
                        try {
                            FileOutputStream fos = context.openFileOutput(dir_str, Context.MODE_PRIVATE);
                            ObjectOutputStream os = new ObjectOutputStream(fos);
                            os.writeObject(oitems); //change items to oitems here
                            os.close();
                            fos.close();
                        } catch (Exception e) {
                            Log.e("exception 1 in HistoryAdapter", Log.getStackTraceString(e));
                        }
                    }
                    else{
                        Toast.makeText(context,"someone delete history in HistoryAdapter",Toast.LENGTH_LONG).show();
                    }
                    notifyDataSetChanged();
                }
            });
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Delete");
                alert.setMessage("Are you sure you want to delete " + item.getLabel() + "?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < oitems.size();i++){
                            if (oitems.get(i).equals(item)){
                                oitems.remove(i);
                                break;
                            }
                        }
                        items.remove(position);
                        //oitems.remove(position);
                        try {
                            String dir_str = "history";
                            FileOutputStream fos = context.openFileOutput(dir_str, Context.MODE_PRIVATE);
                            ObjectOutputStream os = new ObjectOutputStream(fos);
                            os.writeObject(oitems); // change items to oitems here
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

    public ArrayList<Item> getItems(){
        return items;
    }
    public ArrayList<Item> getoItems(){
        return oitems;
    }
    public void filterList(ArrayList<Item> filteredList){
        items = filteredList;
        notifyDataSetChanged();
    }
}
