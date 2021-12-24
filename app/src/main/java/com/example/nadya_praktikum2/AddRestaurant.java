package com.example.nadya_praktikum2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddRestaurant extends AppCompatActivity {
    String fasilitas="";
    EditText nama,alamat;
    RadioGroup jenis1,jenis2;
    CheckBox toilet, musholla, playground;
    Button submit;
    String jenisrestaurant;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);
        getSupportActionBar().setTitle("Tambah Restaurant");
        nama = findViewById(R.id.nama);
        alamat = findViewById(R.id.alamat);
        jenis1 = findViewById(R.id.jenis1);
        jenis2 = findViewById(R.id.jenis2);
        toilet = findViewById(R.id.toilet);
        musholla = findViewById(R.id.musholla);
        playground = findViewById(R.id.playground);
        submit = findViewById(R.id.submit);
        builder = new AlertDialog.Builder(this);
        jenis1.setOnCheckedChangeListener(rgListener1);
        jenis2.setOnCheckedChangeListener(rgListener2);
        submit.setOnClickListener(view -> submitData());
    }

    public void getJenis(int i){
        RadioButton temp = findViewById(i);
        jenisrestaurant = temp.getText().toString();
    }

    public void submitData(){
        if(nama.getText().toString().isEmpty()&&
                alamat.getText().toString().isEmpty()&&
                (jenisrestaurant==null)) {
            Toast.makeText(this, "Semua Field Harus Diisi", Toast.LENGTH_LONG).show();
        }else if(nama.getText().toString().isEmpty()){
            Toast.makeText(this, "Nama Restaurant Belum Diisi", Toast.LENGTH_LONG).show();
        }else if(alamat.getText().toString().isEmpty()) {
            Toast.makeText(this, "Alamat Restaurant Belum Diisi", Toast.LENGTH_LONG).show();
        }else if(jenisrestaurant==null) {
            Toast.makeText(this, "Jenis Restaurant Belum Diisi", Toast.LENGTH_LONG).show();
        }else{
            if(toilet.isChecked()){
                fasilitas+="Toilet ";
            }
            if(musholla.isChecked()){
                fasilitas+="Musholla ";
            }
            if(playground.isChecked()){
                fasilitas+="Playground ";
            }
            builder.setTitle("Data Restaurant")
                    .setMessage("Nama : "+nama.getText().toString()+"\n"
                            +"Alamat : "+alamat.getText().toString()+"\n"
                            +"Jenis : "+jenisrestaurant+"\n"
                            +"Fasilitas : "+(fasilitas.equals("") ?"-":fasilitas)+"\n")
                    .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            DBHelper dbHelper = new DBHelper(getApplicationContext());
                            Restaurant rst = new Restaurant();
                            rst.setNama(nama.getText().toString());
                            rst.setAlamat(alamat.getText().toString());
                            rst.setJenis(jenisrestaurant);
                            rst.setFasilitas((fasilitas.equals("") ?"-":fasilitas));
                            boolean input;
                            input = dbHelper.insertRestaurant(rst);
                            if (input) {
                                Toast.makeText(getApplicationContext(), "Restaurant baru berhasil disimpan", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Kesalahan terjadi!", Toast.LENGTH_SHORT).show();
                            }
                            dbHelper.close();
                            Intent intent = new Intent(AddRestaurant.this, MainActivity.class);
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

    public RadioGroup.OnCheckedChangeListener rgListener1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            jenis2.setOnCheckedChangeListener(null);
            jenis2.clearCheck();
            jenis2.setOnCheckedChangeListener(rgListener2);
            getJenis(i);
        }
    };

    public RadioGroup.OnCheckedChangeListener rgListener2 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            jenis1.setOnCheckedChangeListener(null);
            jenis1.clearCheck();
            jenis1.setOnCheckedChangeListener(rgListener1);
            getJenis(i);
        }
    };
}
