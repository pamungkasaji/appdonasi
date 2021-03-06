package com.aji.donasi.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aji.donasi.Helper;
import com.aji.donasi.R;
import com.aji.donasi.models.Perkembangan;
import com.bumptech.glide.Glide;
import com.github.vipulasri.timelineview.TimelineView;

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

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

//    public KontenAdapter(Context mCtx, List<Konten> kontenList) {
//        this.mCtx = mCtx;
//        this.kontenList = kontenList;
//    }

    @NonNull
    @Override
    public PerkembanganViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_perkembangan, parent, false);
        return new PerkembanganViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull PerkembanganViewHolder holder, int position) {
        Perkembangan perkembangan = perkembanganList.get(position);

        holder.tv_judul.setText(perkembangan.getJudul());
        holder.tv_deskripsi.setText(perkembangan.getDeskripsi());

        //penggunaan dana
        if (perkembangan.getPenggunaanDana() != null){
            holder.tv_judul.setVisibility(View.GONE);
            holder.tv_penggunaan_dana.setVisibility(View.VISIBLE);
            holder.rincian.setVisibility(View.VISIBLE);
            holder.info_penggunaan_dana.setVisibility(View.VISIBLE);
            holder.tv_penggunaan_dana.setText(Helper.mataUang(perkembangan.getPenggunaanDana()));
        }

        String createdAt = Helper.tanggal(perkembangan.getCreatedAt());
        holder.tv_createdat.setText(createdAt);

        String imagePath= Helper.IMAGE_URL_PERKEMBANGAN +perkembanganList.get(position).getGambar();

        Glide.with(mCtx)
                .load(imagePath)
                .into(holder.gambar);
    }

    @Override
    public int getItemCount() {
        return perkembanganList.size();
    }

    class PerkembanganViewHolder extends RecyclerView.ViewHolder {

        TextView tv_judul, tv_createdat, tv_deskripsi, tv_penggunaan_dana, rincian, info_penggunaan_dana;
        ImageView gambar;

        private PerkembanganViewHolder(View itemView, int viewType) {
            super(itemView);

            TimelineView mTimelineView = itemView.findViewById(R.id.timeline);
            mTimelineView.initLine(viewType);

            gambar = itemView.findViewById(R.id.gambar);

            tv_judul = itemView.findViewById(R.id.tv_judul);
            tv_createdat = itemView.findViewById(R.id.tv_createdat);
            tv_deskripsi = itemView.findViewById(R.id.tv_deskripsi);

            tv_penggunaan_dana = itemView.findViewById(R.id.tv_penggunaan_dana);
            rincian = itemView.findViewById(R.id.rincian);
            info_penggunaan_dana = itemView.findViewById(R.id.info_penggunaan_dana);

        }
    }
}
