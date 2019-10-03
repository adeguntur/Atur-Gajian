package com.example.aturgajian;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.aturgajian.adapter.KeuanganAdapter;
import com.example.aturgajian.entities.Keuangan;
import com.example.aturgajian.helper.SqliteHelper;
import com.example.aturgajian.models.AturGajianModel;
import com.example.aturgajian.utils.RecyclerItemClickListener;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private List<Keuangan> listStudent = new ArrayList<Keuangan>();
    private RecyclerView recyclerView;
    private KeuanganAdapter adapter;

    TextView text_masuk, text_keluar, text_total,text_jumlah;
    SwipeRefreshLayout swipe_refresh;

    public static String transaksi_id, tgl_dari, tgl_ke;
    public static boolean filter;

    String query_kas, query_total;
    SqliteHelper sqliteHelper;
    AturGajianModel aturGajianModel;
    Cursor cursor;

    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initData();

        recyclerView = (RecyclerView) findViewById(R.id.rv_keuangan);
        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddActivityKeluar.class));

            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddActivityMasuk.class));

            }
        });

        transaksi_id = ""; tgl_dari = ""; tgl_ke = ""; query_kas = ""; query_total = "";  filter = false;
        sqliteHelper = new SqliteHelper(this);

        text_masuk      = (TextView) findViewById(R.id.text_masuk);
        text_keluar     = (TextView) findViewById(R.id.text_keluar);
        text_total      = (TextView) findViewById(R.id.text_total);
        swipe_refresh   = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        text_jumlah = (TextView)findViewById(R.id.text_jumlah);



        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                KasAdapter();
            }
        });

        KasAdapter();

    }

    private void KasAdapter(){

        listStudent.clear(); recyclerView.setAdapter(null);

        listStudent.addAll(aturGajianModel.allTransaksi());

        adapter = new KeuanganAdapter(this, listStudent);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        transaksi_id = ((TextView) view.findViewById(R.id.text_transaksi_id)).getText().toString();
                        ListMenu();
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        KasTotal();
    }

    private void KasTotal(){
        NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);

        SQLiteDatabase database = sqliteHelper.getReadableDatabase();
        cursor = database.rawQuery( query_total, null);
        cursor.moveToFirst();
        text_masuk.setText("Rp."+rupiahFormat.format(cursor.getDouble(1)) );
        text_keluar.setText("Rp."+rupiahFormat.format(cursor.getDouble(2)) );
        text_total.setText("Rp."+
                rupiahFormat.format(cursor.getDouble(1) - cursor.getDouble(2))
        );

        swipe_refresh.setRefreshing(false);

    }

    @Override
    public void onResume(){
        super.onResume();



        KasAdapter();

    }

    private void ListMenu(){

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.item_menu);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        TextView text_edit  = (TextView) dialog.findViewById(R.id.text_edit);
        TextView text_hapus = (TextView) dialog.findViewById(R.id.text_hapus);
        dialog.show();

        text_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, EditActivity.class));
            }
        });
        text_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Hapus();
            }
        });
    }

    private void Hapus(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi");
        builder.setMessage("Yakin untuk mengahapus transaksi ini?");
        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                        aturGajianModel.delete(transaksi_id);

                        Toast.makeText(getApplicationContext(), "Tranksaki berhasil dihapus",
                                Toast.LENGTH_LONG).show();

                        KasAdapter();

                    }
                });

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        builder.show();
    }
    private void initData() {
        this.aturGajianModel = new AturGajianModel(this);
    }
}
