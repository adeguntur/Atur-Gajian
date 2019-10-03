package com.example.aturgajian;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aturgajian.helper.SqliteHelper;
import com.example.aturgajian.models.AturGajianModel;
import com.example.aturgajian.utils.DateFormat;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class EditActivity extends AppCompatActivity {

    RadioGroup radio_status;
    RadioButton radio_masuk, radio_keluar;

    EditText edit_jumlah, edit_keterangan, edit_tanggal;
    Button btn_simpan;

    SqliteHelper sqliteHelper;
    Cursor cursor;
    AturGajianModel aturGajianModel;
    String status, tanggal;

    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initData();

        status = ""; tanggal = "";

        radio_status    = (RadioGroup)  findViewById(R.id.radio_status);
        radio_masuk     = (RadioButton) findViewById(R.id.radio_masuk);
        radio_keluar    = (RadioButton) findViewById(R.id.radio_keluar);

        edit_jumlah     = (EditText)    findViewById(R.id.edit_jumlah);
        edit_keterangan = (EditText)    findViewById(R.id.edit_keterangan);
        edit_tanggal    = (EditText)    findViewById(R.id.edit_tanggal);
        btn_simpan      = (Button)      findViewById(R.id.btn_simpan);

        sqliteHelper = new SqliteHelper(this);


        status = cursor.getString(1);
        switch (status){
            case "MASUK":
                radio_masuk.setChecked(true); break;
            case "KELUAR":
                radio_keluar.setChecked(true); break;
        }

        radio_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch(checkedId){
                    case R.id.radio_masuk:
                        status = "MASUK";
                        break;
                    case R.id.radio_keluar:
                        status = "KELUAR";
                        break;
                }

                Log.d("Log status", status);
            }
        });

        edit_jumlah.setText( cursor.getString(2) );
        edit_keterangan.setText( cursor.getString(3) );

        tanggal = cursor.getString(4);
        edit_tanggal.setText( cursor.getString(5) );

        edit_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month_of_year, int day_of_month) {
                        // set day of month , month and year value in the edit text
                        NumberFormat numberFormat = new DecimalFormat("00");
                        tanggal = year + "-" + numberFormat.format(( month_of_year +1 )) + "-" +
                                numberFormat.format(day_of_month);
                        edit_tanggal.setText(numberFormat.format(day_of_month) + "/" + numberFormat.format(( month_of_year +1 )) +
                                "/" + year );
                    }
                }, DateFormat.year, DateFormat.month, DateFormat.day);
                datePickerDialog.show();
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("") || edit_jumlah.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Isi data dengan benar",
                            Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(getApplicationContext(), "Perubahan berhasil disimpan", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit");
    }
    private void initData() {
        this.aturGajianModel = new AturGajianModel(this);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
