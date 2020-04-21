package com.aji.donasi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.aji.donasi.Helper;
import com.aji.donasi.R;
import com.aji.donasi.models.Konten;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class KontenAdapter extends RecyclerView.Adapter<KontenAdapter.KontenViewHolder> {

    private Context mCtx;
    private ArrayList<Konten> kontenList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public KontenAdapter(Context mCtx, ArrayList<Konten> kontenList) {
        this.mCtx = mCtx;
        this.kontenList = kontenList;
    }

    @NonNull
    @Override
    public KontenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_konten, parent, false);
        return new KontenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KontenViewHolder holder, int position) {
        Konten konten = kontenList.get(position);

        holder.tvJudul.setText(konten.getJudul());
        holder.tvLama.setText(String.valueOf(konten.getLamaDonasi()));
        holder.progressBar.setProgress(konten.getTerkumpul() / konten.getTarget() * 100 );

        holder.tvTerkumpul.setText(Helper.mataUang(konten.getTerkumpul()));

        double progress = ((double)konten.getTerkumpul()/(double)konten.getTarget())*100;
        holder.progressBar.setProgress((int)progress);

        if (Helper.SELESAI.equals(konten.getStatus())) {
            holder.tvLama.setText(Helper.SELESAI);
            holder.teksLama.setVisibility(View.GONE);
        } else if (Helper.VERIFIKASI.equals(konten.getStatus())){
            holder.tvLama.setText(Helper.TUNGGU);
            holder.teksLama.setText(Helper.VERIFIKASI);
            holder.tvTerkumpul.setVisibility(View.GONE);
            holder.teksTerkumpul.setVisibility(View.GONE);
        } else if (Helper.DITOLAK.equals(konten.getStatus())){
            holder.tvLama.setText(Helper.VERIFIKASI);
            holder.teksLama.setText(Helper.DITOLAK);
            holder.tvTerkumpul.setVisibility(View.GONE);
            holder.teksTerkumpul.setVisibility(View.GONE);
        }

        String imagePath= Helper.IMAGE_URL_KONTEN +kontenList.get(position).getGambar();

        Glide.with(mCtx)
                .load(imagePath)
                .placeholder(R.drawable.loading)
                .dontAnimate()
                .into(holder.gambarkonten);
    }

    @Override
    public int getItemCount() {
        return kontenList.size();
    }

    class KontenViewHolder extends RecyclerView.ViewHolder {

        TextView tvJudul, tvTerkumpul, tvLama, teksLama, teksTerkumpul;
        ImageView gambarkonten;
        ProgressBar progressBar;

        private KontenViewHolder(View itemView) {
            super(itemView);

            gambarkonten = itemView.findViewById(R.id.gambarkonten);
            tvJudul = itemView.findViewById(R.id.tvJudul);
            tvLama = itemView.findViewById(R.id.tvLama);
            teksLama = itemView.findViewById(R.id.teksLama);
            tvTerkumpul = itemView.findViewById(R.id.tvTerkumpul);
            teksTerkumpul = itemView.findViewById(R.id.teksTerkumpul);
            progressBar = itemView.findViewById(R.id.progressBar);

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
