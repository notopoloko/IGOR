package br.com.android.unb.igor;

import java.util.Date;

public class Aventura {
    private String nome, mestre, sinopse;
    private Date data;
    private int nImage;

    public Aventura(String nome, Date data, int nImage, String mestre, String pSinopse) {
        this.nome = nome;
        this.data = data;
        this.nImage = nImage;
        this.mestre = mestre;
        sinopse = pSinopse;
    }

    public String getNome() {
        return nome;
    }

    public Date getData() {
        return data;
    }

    public int getnImage() {
        return nImage;
    }

    public String getMestre() {
        return mestre;
    }

    public String getSinopse() {
        return sinopse;
    }
}
