package com.aji.donasi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.aji.donasi.R;
import com.aji.donasi.models.Konten;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class KontenAdapter extends RecyclerView.Adapter<KontenAdapter.KontenViewHolder> {

    private Context mCtx;
    //private List<Konten> kontenList;
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

//    public KontenAdapter(Context mCtx, List<Konten> kontenList) {
//        this.mCtx = mCtx;
//        this.kontenList = kontenList;
//    }

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
        holder.tvTarget.setText(String.valueOf(konten.getTerkumpul()));
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
