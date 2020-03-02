package com.aji.donasi;

public class MessageEvent {

    public final int id_konten;
    public final String nomorrekening;
    public final String gambar;

    public MessageEvent(int id_konten, String nomorrekening, String gambar) {
        this.id_konten = id_konten;
        this.nomorrekening = nomorrekening;
        this.gambar = gambar;
    }
}
