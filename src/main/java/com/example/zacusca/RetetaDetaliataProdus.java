package com.example.zacusca;

public class RetetaDetaliataProdus {

    private String descriere;
    private String link;
    private int nr_pas;


    public RetetaDetaliataProdus(String descriere, String link, int nr_pas){
        this.descriere = descriere;
        this.link = link;
        this.nr_pas = nr_pas;
    }
    public String getDescriere(){
        return descriere;
    }
    public void setDescriere(String descriere){
        this.descriere = descriere;
    }
    public String getLink(){
        return link;
    }
    public void setLink(String link){
        this.link = link;
    }
    public int getNr_pas(){
        return nr_pas;
    }
    public void setNr_pas(int nr_pas){
        this.nr_pas = nr_pas;
    }
}