package com.example.kalkulator;

import androidx.annotation.NonNull;

public class History {
    private String riwayat;
    private String id;

    public String getRiwayat() {
        return riwayat;
    }

    public void setRiwayat(String riwayat) {
        this.riwayat = riwayat;
    }

    public String getId() {
        return id;
    }

    public History(String id, String riwayat) {
        this.id = id;
        this.riwayat = riwayat;
    }

    @NonNull
    @Override
    public String toString() {
        return riwayat;
    }
}