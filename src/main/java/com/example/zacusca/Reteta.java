package com.example.zacusca;

public class Reteta {

    private String nume, descriere, poza;
    private String id;

    public Reteta(String nume, String descriere, String poza, String id){
        this.nume = nume;
        this.descriere = descriere;
        this.poza = poza;
        this.id = id;

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
    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

}
