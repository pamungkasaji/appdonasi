package com.aji.donasi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aji.donasi.R;
import com.aji.donasi.models.Donatur;

import java.util.ArrayList;

public class DonasiMasukAdapter extends RecyclerView.Adapter<DonasiMasukAdapter.DonasiMasukViewHolder> {

    private Context mCtx;
    private ArrayList<Donatur> donaturList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public DonasiMasukAdapter(Context mCtx, ArrayList<Donatur> donaturList) {
        this.mCtx = mCtx;
        this.donaturList = donaturList;
    }

    @NonNull
    @Override
    public DonasiMasukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_donasi_masuk, parent, false);
        return new DonasiMasukViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonasiMasukViewHolder holder, int position) {
        Donatur donatur = donaturList.get(position);

        holder.tv_nama.setText(donatur.getNama());
        holder.tv_jumlah.setText(String.valueOf(donatur.getJumlah().toString()));

//        String imagePath= "https://lorempixel.com/800/600/"+donaturList.get(position).getGambar();
//
//        Glide.with(mCtx)
//                .load(imagePath)
//                .placeholder(R.drawable.loading)
//                .into(holder.movieImage);
    }

    @Override
    public int getItemCount() {
        return donaturList.size();
    }

    class DonasiMasukViewHolder extends RecyclerView.ViewHolder {

        TextView tv_nama, tv_jumlah;

        public DonasiMasukViewHolder(View itemView) {
            super(itemView);

            tv_nama = itemView.findViewById(R.id.tv_nama);
            tv_jumlah = itemView.findViewById(R.id.tv_jumlah);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
