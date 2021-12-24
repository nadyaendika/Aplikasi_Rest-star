package com.example.nadya_praktikum2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Result extends AppCompatActivity {
    private ArrayList idList;
    private ArrayList restaurantIdList;
    private ArrayList namaList;
    private ArrayList reviewList;
    private ArrayList ratingList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    TextView nama, alamat, jenis, fasilitas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        idList = new ArrayList<>();
        namaList = new ArrayList<>();
        restaurantIdList = new ArrayList<>();
        reviewList = new ArrayList<>();
        ratingList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler);
        getData();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ReviewAdapter(idList, restaurantIdList, namaList, reviewList, ratingList);
        recyclerView.setAdapter(adapter);

        nama = findViewById(R.id.nama);
        alamat = findViewById(R.id.alamat);
        jenis = findViewById(R.id.jenis);
        fasilitas = findViewById(R.id.fasilitas);

        final DBHelper dh = new DBHelper(getApplicationContext());
        Cursor cursor = dh.getRestaurant(getIntent().getExtras().getInt("id"));
        cursor.moveToFirst();
        nama.setText(cursor.getString(1));
        alamat.setText(cursor.getString(2));
        jenis.setText(cursor.getString(3));
        fasilitas.setText(cursor.getString(4));
        getSupportActionBar().setTitle("Restaurant "+cursor.getString(1));
        dh.close();
    }

    @SuppressLint("Recycle")
    protected void getData(){
        final DBHelper dh = new DBHelper(getApplicationContext());
        Cursor cursor = dh.getReviewRestaurant(getIntent().getExtras().getInt("id"));
        cursor.moveToFirst();
        for(int count=0; count < cursor.getCount(); count++){
            cursor.moveToPosition(count);
            idList.add(cursor.getInt(0));
            restaurantIdList.add(cursor.getInt(1));
            namaList.add(cursor.getString(2));
            reviewList.add(cursor.getString(3));
            ratingList.add(cursor.getFloat(4));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "Application On Start", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "Application On Stop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "Application On Restart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "Application On Resume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "Application On Pause", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Selamat Tinggal", Toast.LENGTH_SHORT).show();
    }

    public void back(View view) {
        Intent intent = new Intent(Result.this, MainActivity.class);
        startActivity(intent);
    }
}
