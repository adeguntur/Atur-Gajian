package com.example.aturgajian;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.aturgajian.entities.Keuangan;
import com.example.aturgajian.helper.SqliteHelper;
import com.example.aturgajian.models.AturGajianModel;

public class AddActivityKeluar extends AppCompatActivity {

    CurrencyEditText edit_jumlah;
    EditText edit_keterangan;
    Button btn_simpan;

    public AturGajianModel mAturGajian;

    String status = "KELUAR";
    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_keluar);
        initData();
        sqliteHelper = new SqliteHelper(this);

        edit_jumlah     = (CurrencyEditText) findViewById(R.id.edt_jumlah);
        edit_keterangan = (EditText) findViewById(R.id.edit_keterangan);
        btn_simpan      = (Button) findViewById(R.id.btn_simpan);

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_jumlah.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Isi data dengan benar",
                            Toast.LENGTH_LONG).show();
                } else {

                    String keterangan = edit_keterangan.getText().toString();

                    Keuangan keuanganBaru = new Keuangan();
                    keuanganBaru.setStatus(status);
                    keuanganBaru.setJumlah(edit_jumlah.getRawValue());
                    keuanganBaru.setKeterangan(keterangan);

                    mAturGajian.insert(keuanganBaru);

                    Toast.makeText(getApplicationContext(), "Transaksi berhasil disimpan", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pengeluaran");

    }
    private void initData() {
        this.mAturGajian = new AturGajianModel(this);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
