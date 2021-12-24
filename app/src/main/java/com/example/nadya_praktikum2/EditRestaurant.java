package com.example.nadya_praktikum2;

import androidx.appcompat.app.AppCompatActivity;

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

public class EditRestaurant extends AppCompatActivity {
    String fasilitas="";
    EditText nama,alamat;
    RadioGroup jenis1,jenis2;
    CheckBox toilet, musholla, playground;
    Button submit;
    String jenisrestaurant;
    AlertDialog.Builder builder;
    RadioButton western, japanese, thai, indonesian, chinese, korean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);
        getSupportActionBar().setTitle("Edit Restaurant");
        nama = findViewById(R.id.nama);
        alamat = findViewById(R.id.alamat);
        jenis1 = findViewById(R.id.jenis1);
        jenis2 = findViewById(R.id.jenis2);
        toilet = findViewById(R.id.toilet);
        musholla = findViewById(R.id.musholla);
        playground = findViewById(R.id.playground);
        submit = findViewById(R.id.submit);
        western = findViewById(R.id.western);
        japanese = findViewById(R.id.japanese);
        thai = findViewById(R.id.thai);
        indonesian = findViewById(R.id.indonesian);
        chinese = findViewById(R.id.chinese);
        korean = findViewById(R.id.korean);
        builder = new AlertDialog.Builder(this);
        jenis1.setOnCheckedChangeListener(rgListener1);
        jenis2.setOnCheckedChangeListener(rgListener2);
        submit.setOnClickListener(view -> submitData());

        nama.setText(getIntent().getExtras().getString("nama"));
        alamat.setText(getIntent().getExtras().getString("alamat"));
        String fasList = getIntent().getExtras().getString("fasilitas");
        String[] fas = fasList.split(" ");
        for (String s : fas) {
            switch (s) {
                case "Toilet":
                    toilet.setChecked(true);
                    break;
                case "Musholla":
                    musholla.setChecked(true);
                    break;
                case "Playground":
                    playground.setChecked(true);
                    break;
            }
        }
        jenisrestaurant = getIntent().getExtras().getString("jenis");
        switch (jenisrestaurant) {
            case "Western":
                western.setChecked(true);
                break;
            case "Japanese":
                japanese.setChecked(true);
                break;
            case "Thai":
                thai.setChecked(true);
                break;
            case "Indonesian":
                indonesian.setChecked(true);
                break;
            case "Chinese":
                chinese.setChecked(true);
                break;
            default:
                korean.setChecked(true);
                break;
        }
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
                            input = dbHelper.updateRestaurant(rst, getIntent().getExtras().getInt("id"));
                            if (input) {
                                Toast.makeText(getApplicationContext(), "Restaurant berhasil diupdate", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Kesalahan terjadi!", Toast.LENGTH_SHORT).show();
                            }
                            dbHelper.close();
                            Intent intent = new Intent(EditRestaurant.this, MainActivity.class);
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
