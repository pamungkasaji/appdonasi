package com.aji.donasi;

public class MessageEvent {

    public final int id_konten;
    public final String nomorrekening;

    public MessageEvent(int id_konten, String nomorrekening) {
        this.id_konten = id_konten;
        this.nomorrekening = nomorrekening;
    }
}
