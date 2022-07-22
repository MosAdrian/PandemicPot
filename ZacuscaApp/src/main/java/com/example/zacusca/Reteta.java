package com.example.zacusca;

import android.content.SharedPreferences;

public class Reteta {

    private String nume, descriere, poza;

    public Reteta(String nume, String descriere, String poza){
        this.nume = nume;
        this.descriere = descriere;
        this.poza = poza;

    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }
    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }
    public String getPoza() {
        return poza;
    }

    public void setPoza(String poza) {
        this.poza = poza;
    }
}
