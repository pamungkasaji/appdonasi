package com.aji.donasi.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aji.donasi.R;
import com.aji.donasi.models.Perkembangan;

import java.util.ArrayList;

public class PerkembanganAdapter extends RecyclerView.Adapter<PerkembanganAdapter.PerkembanganViewHolder> {

    private Context mCtx;
    private ArrayList<Perkembangan> perkembanganList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public PerkembanganAdapter(Context mCtx, ArrayList<Perkembangan> perkembanganList) {
        this.mCtx = mCtx;
        this.perkembanganList = perkembanganList;
    }

//    public KontenAdapter(Context mCtx, List<Konten> kontenList) {
//        this.mCtx = mCtx;
//        this.kontenList = kontenList;
//    }

    @NonNull
    @Override
    public PerkembanganViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_perkembangan, parent, false);
        return new PerkembanganViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PerkembanganViewHolder holder, int position) {
        Perkembangan perkembangan = perkembanganList.get(position);

        holder.tv_judul.setText(perkembangan.getJudul());
        holder.tv_createdat.setText(perkembangan.getTanggal());
        //holder.tv_jumlah.setText(String.valueOf(perkembangan.getJumlah().toString()));

//        String imagePath= "https://lorempixel.com/800/600/"+perkembanganList.get(position).getGambar();
//
//        Glide.with(mCtx)
//                .load(imagePath)
//                .placeholder(R.drawable.loading)
//                .into(holder.movieImage);
    }

    @Override
    public int getItemCount() {
        return perkembanganList.size();
    }

    class PerkembanganViewHolder extends RecyclerView.ViewHolder {

        TextView tv_judul, tv_createdat;

        public PerkembanganViewHolder(View itemView) {
            super(itemView);

            tv_judul = itemView.findViewById(R.id.tv_judul);
            tv_createdat = itemView.findViewById(R.id.tv_createdat);

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
