package com.example.nadya_praktikum2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RestaurantViewAdapter extends RecyclerView.Adapter<RestaurantViewAdapter.ViewHolder>{
    private ArrayList idList;
    private ArrayList namaList;
    private ArrayList alamatList;
    private ArrayList jenisList;
    private ArrayList fasilitasList;
    private Context context;
    View V;

    RestaurantViewAdapter(ArrayList idList, ArrayList namaList, ArrayList alamatList, ArrayList jenisList, ArrayList fasilitasList){
        this.idList = idList;
        this.namaList = namaList;
        this.alamatList = alamatList;
        this.jenisList = jenisList;
        this.fasilitasList = fasilitasList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nama, jenis;
        private ImageButton edit, delete;

        ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            nama = itemView.findViewById(R.id.nama);
            jenis = itemView.findViewById(R.id.jenis);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        V = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_restaurant, parent, false);
        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final int id = (int) idList.get(position);
        final String nama = (String) namaList.get(position);
        final String alamat = (String) alamatList.get(position);
        final String jenis = (String) jenisList.get(position);
        final String fasilitas = (String) fasilitasList.get(position);
        int pink_soft = Color.parseColor("#F8BBD0");
        if (position%2 == 1){
            V.setBackgroundColor(pink_soft);
        }
        holder.nama.setText(nama);
        holder.jenis.setText(jenis);

        V.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent(view.getContext(), Result.class);
                intent.putExtra("id", id);
                intent.putExtra("nama", nama);
                intent.putExtra("alamat", alamat);
                intent.putExtra("jenis", jenis);
                intent.putExtra("fasilitas", fasilitas);
                context.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                int idRes = id;
                androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                alertDialogBuilder.setTitle("Hapus Data "+nama+"?");
                alertDialogBuilder
                        .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DBHelper dbHelper = new DBHelper(context.getApplicationContext());
                                dbHelper.deleteRestaurant(idRes);
                                Toast.makeText(context.getApplicationContext(), "Restaurant berhasil dihapus", Toast.LENGTH_SHORT).show();
                                dbHelper.close();
                                Intent intent = new Intent(view.getContext(), MainActivity.class);
                                context.startActivity(intent);

                            }
                        })
                        .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent(view.getContext(), EditRestaurant.class);
                intent.putExtra("id", id);
                intent.putExtra("nama", nama);
                intent.putExtra("alamat", alamat);
                intent.putExtra("jenis", jenis);
                intent.putExtra("fasilitas", fasilitas);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return idList.size();
    }
}
