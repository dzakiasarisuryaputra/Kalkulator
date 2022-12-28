package com.example.kalkulator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private ArrayList<History> mylist;
    private Context context;
    private SharedPreferences sharedPreferences;

    public HistoryAdapter(ArrayList<History> mylist,Context context,SharedPreferences sh){
        this.context = context;
        this.mylist = mylist;
        this.sharedPreferences = sh;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater ly = LayoutInflater.from(context);
        ViewHolder holder = new ViewHolder(ly.inflate(R.layout.item_list,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History list = mylist.get(position);
        holder.riwayat.setText(list.getRiwayat());
    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView riwayat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            riwayat = itemView.findViewById(R.id.txtHistory);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int p = getLayoutPosition();
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Hapus Riwayat").setMessage("Ingin menghapus hitungan ini?")
                            .setPositiveButton("Cancel",(dialogInterface, i) -> dialogInterface.cancel())
                            .setPositiveButton("Yes",(dialogInterface, i) -> {
                                String id = mylist.get(p).getId();
                                sharedPreferences.edit().remove(id).commit();

                                for (int l = 0;l<mylist.size();l++){
                                    if (id.equalsIgnoreCase(mylist.get(l).getId())){
                                        mylist.remove(l);
                                        notifyItemRemoved(l);
                                        notifyItemChanged(l);
                                        notifyItemRangeChanged(l,mylist.size());
                                    }
                                }
                            });
                    AlertDialog dialog = alert.create();
                    dialog.show();
                    return true;
                }
            });
        }
    }
}