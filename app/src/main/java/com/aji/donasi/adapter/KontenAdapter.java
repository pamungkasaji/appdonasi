package com.aji.donasi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.aji.donasi.R;
import com.aji.donasi.models.Konten;
import androidx.annotation.NonNull;

import java.util.List;

public class KontenAdapter extends RecyclerView.Adapter<KontenAdapter.KontenViewHolder> {

    private Context mCtx;
    private List<Konten> kontenList;

    public KontenAdapter(Context mCtx, List<Konten> kontenList) {
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
        holder.tvTarget.setText(konten.getDeskripsi());
        holder.tvTerkumpul.setText(konten.getNomorrekening());
    }

    @Override
    public int getItemCount() {
        return kontenList.size();
    }

    class KontenViewHolder extends RecyclerView.ViewHolder {

        TextView tvJudul, tvTarget, tvTerkumpul;

        public KontenViewHolder(View itemView) {
            super(itemView);

            tvJudul = itemView.findViewById(R.id.tvJudul);
            tvTarget = itemView.findViewById(R.id.tvTarget);
            tvTerkumpul = itemView.findViewById(R.id.tvTerkumpul);
        }
    }
}
