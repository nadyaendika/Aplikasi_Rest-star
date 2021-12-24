package com.example.nadya_praktikum2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Objects;

public class EditReview extends AppCompatActivity {
    TextView alamat, jenis, fasilitas;
    EditText review, namaRev;
    SeekBar rating;
    TextView pointrating;
    Button submit;
    float rate=0;
    AlertDialog.Builder builder;
    Spinner spinner;
    int POST_ID = 0;
    String POST_ALAMAT, POST_JENIS, POST_FASILITAS, nama;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_review);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit Review");
        review = findViewById(R.id.review);
        rating = findViewById(R.id.rating);
        alamat = findViewById(R.id.alamat);
        jenis = findViewById(R.id.jenis);
        fasilitas = findViewById(R.id.fasilitas);
        builder = new AlertDialog.Builder(this);
        submit = findViewById(R.id.submit);
        namaRev = findViewById(R.id.namaRev);
        spinner = findViewById(R.id.spinner1);
        submit.setOnClickListener(view -> submitData());
        loadspinner();
        final DBHelper dh = new DBHelper(getApplicationContext());
        Cursor cursor = dh.getRestaurant(getIntent().getExtras().getInt("restaurantId"));
        cursor.moveToFirst();
        nama = cursor.getString(1);
        alamat.setText(cursor.getString(2));
        jenis.setText(cursor.getString(3));
        fasilitas.setText(cursor.getString(4));
        dh.close();

        Cursor c = (Cursor)spinner.getItemAtPosition(0);
        for (int i = 0; i < spinner.getCount(); i++) {
            c.moveToPosition(i);
            String itemText;
            itemText = c.getString(c.getColumnIndex("nama"));
            if (itemText.equals(nama)) {
                spinner.setSelection(i);
            }
        }
        review.setText(getIntent().getExtras().getString("review"));
        namaRev.setText(getIntent().getExtras().getString("namaRev"));
        pointrating = findViewById(R.id.pointrating);
        rate = getIntent().getExtras().getFloat("rating");
        rating.setMax(10);
        rating.setProgress((int) ((getIntent().getExtras().getFloat("rating"))*2));
        pointrating.setText(String.valueOf(getIntent().getExtras().getFloat("rating")));
        rating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                rate= (float) i/2;
                pointrating.setText(String.valueOf(rate));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar){}
        });
    }

    @SuppressLint("Range")
    private void loadspinner() {
        DBHelper dh = new DBHelper(getApplicationContext());
        Cursor cursor = dh.getServiceCursor();
        String[] from = {"nama"};
        int[] to = {android.R.id.text1};
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.spinner_item, cursor, from, to, 0){
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(Color.GRAY);
                tv.setTextSize(13);
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("Range")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object valueID = parent.getSelectedItemId();
                POST_ID = Integer.parseInt(String.valueOf(valueID));

                Cursor qc = adapter.getCursor();
                POST_ALAMAT = qc.getString(qc.getColumnIndex("alamat"));
                alamat.setText(POST_ALAMAT);
                POST_JENIS = qc.getString(qc.getColumnIndex("jenis"));
                jenis.setText(POST_JENIS);
                POST_FASILITAS = qc.getString(qc.getColumnIndex("fasilitas"));
                fasilitas.setText(POST_FASILITAS);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void submitData(){
        if(review.getText().toString().isEmpty()){
            Toast.makeText(this, "Review Harus Diisi", Toast.LENGTH_LONG).show();
        }else{
            builder.setTitle("Review Restaurant")
                    .setMessage("Review : "+review.getText().toString()+"\n"
                            +"Rating : "+rate+"\n")
                    .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            DBHelper dbHelper = new DBHelper(getApplicationContext());
                            Review rev = new Review();
                            rev.setRestaurantId(POST_ID);
                            rev.setNama(namaRev.getText().toString());
                            rev.setReview(review.getText().toString());
                            rev.setRating(rate);
                            boolean input;
                            input = dbHelper.updateReview(rev, getIntent().getExtras().getInt("id"));
                            if (input) {
                                Toast.makeText(getApplicationContext(), "Review berhasil diupdate", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Kesalahan terjadi!", Toast.LENGTH_SHORT).show();
                            }
                            dbHelper.close();
                            Intent intent = new Intent(EditReview.this, Result.class);
                            intent.putExtra("id", POST_ID);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            builder.show();
        }
    }
}
