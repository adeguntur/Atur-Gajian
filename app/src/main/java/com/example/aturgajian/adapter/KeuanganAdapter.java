package com.example.aturgajian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aturgajian.R;
import com.example.aturgajian.entities.Keuangan;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class KeuanganAdapter extends RecyclerView.Adapter<KeuanganAdapter.MyViewHolder> {

    private Context context;
    private List<Keuangan> transaksiList;


    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView tvTransaksi;
        public TextView tvKeterangan;
        public TextView tvJumlah;
        public TextView tvStatus;
        public TextView tvTanggal;

        public MyViewHolder(View view) {
            super(view);
            tvTransaksi = view.findViewById(R.id.text_transaksi_id);
            tvKeterangan = view.findViewById(R.id.text_keterangan);
            tvJumlah = view.findViewById(R.id.text_jumlah);
            tvStatus = view.findViewById(R.id.text_status);
            tvTanggal = view.findViewById(R.id.text_tanggal);

        }
    }

    public KeuanganAdapter(Context context, List<Keuangan>noteslist){
        this.context = context;
        this.transaksiList = noteslist;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_keuangan,parent,false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Keuangan keuangan = transaksiList.get(position);

        NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);

        holder.tvTransaksi.setText(String.valueOf(keuangan.getId()));
        holder.tvKeterangan.setText(keuangan.getKeterangan());
        holder.tvStatus.setText("Rp."+rupiahFormat.format(keuangan.getJumlah()));
        holder.tvTanggal.setText(keuangan.getDate());

    }

    @Override
    public int getItemCount() {
        return transaksiList.size();
    }
}
