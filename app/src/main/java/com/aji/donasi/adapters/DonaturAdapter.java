package com.aji.donasi.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aji.donasi.R;
import com.aji.donasi.models.Donatur;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DonaturAdapter extends RecyclerView.Adapter<DonaturAdapter.DonaturViewHolder> {

    private Context mCtx;
    private ArrayList<Donatur> donaturList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public DonaturAdapter(Context mCtx, ArrayList<Donatur> donaturList) {
        this.mCtx = mCtx;
        this.donaturList = donaturList;
    }

//    public KontenAdapter(Context mCtx, List<Konten> kontenList) {
//        this.mCtx = mCtx;
//        this.kontenList = kontenList;
//    }

    @NonNull
    @Override
    public DonaturViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_donatur, parent, false);
        return new DonaturViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonaturViewHolder holder, int position) {
        Donatur donatur = donaturList.get(position);

        //nama
        holder.tv_nama.setText(donatur.getNama());

        //jumlah
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        holder.tv_jumlah.setText(formatRupiah.format((double)donatur.getJumlah()));

        //tanggal

    }

    @Override
    public int getItemCount() {
        return donaturList.size();
    }

    class DonaturViewHolder extends RecyclerView.ViewHolder {

        TextView tv_nama, tv_jumlah, tv_createdat;

        public DonaturViewHolder(View itemView) {
            super(itemView);

            tv_nama = itemView.findViewById(R.id.tvNama);
            tv_jumlah = itemView.findViewById(R.id.tv_jumlah);
            //tv_createdat = itemView.findViewById(R.id.tv_createdat);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mListener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            mListener.onItemClick(position);
//                        }
//                    }
//                }
//            });
        }
    }
}
