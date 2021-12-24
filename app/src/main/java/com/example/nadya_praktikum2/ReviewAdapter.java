package com.example.nadya_praktikum2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{
    private ArrayList idList;
    private ArrayList restaurantIdList;
    private ArrayList namaList;
    private ArrayList reviewList;
    private ArrayList ratingList;
    private Context context;
    View V;

    ReviewAdapter(ArrayList idList, ArrayList restaurantIdList, ArrayList namaList, ArrayList reviewList, ArrayList ratingList){
        this.idList = idList;
        this.restaurantIdList = restaurantIdList;
        this.namaList = namaList;
        this.reviewList = reviewList;
        this.ratingList = ratingList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nama, rating, review;
        private ImageButton edit, delete;

        ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            nama = itemView.findViewById(R.id.nama);
            rating = itemView.findViewById(R.id.rating);
            review = itemView.findViewById(R.id.review);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        V = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_review, parent, false);
        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final int id = (int) idList.get(position);
        final int restaurantId = (int) restaurantIdList.get(position);
        final String nama = (String) namaList.get(position);
        final float rating = (float) ratingList.get(position);
        final String review = (String) reviewList.get(position);
        holder.nama.setText(nama);
        holder.rating.setText(String.valueOf(rating));
        holder.review.setText(review);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(final View view) {
                int idRev = id;
                androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                alertDialogBuilder.setTitle("Hapus Review?");
                alertDialogBuilder
                        .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DBHelper dbHelper = new DBHelper(context.getApplicationContext());
                                dbHelper.deleteReview(idRev);
                                Toast.makeText(context.getApplicationContext(), "Review berhasil dihapus", Toast.LENGTH_SHORT).show();
                                dbHelper.close();
                                Intent intent = new Intent(view.getContext(), Result.class);
                                intent.putExtra("id", restaurantId);
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
                Intent intent = new Intent(view.getContext(), EditReview.class);
                intent.putExtra("id", id);
                intent.putExtra("restaurantId", restaurantId);
                intent.putExtra("namaRev", nama);
                intent.putExtra("rating", rating);
                intent.putExtra("review", review);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return idList.size();
    }
}
