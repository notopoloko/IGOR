package br.com.android.unb.igor;

import java.util.Date;

public class Aventura {
    private String nome;
    private Date data;
    private int nImage;

    public Aventura(String nome, Date data, int nImage) {
        this.nome = nome;
        this.data = data;
        this.nImage = nImage;
    }

    public Aventura(String nome, Date data, Long nImage) {
        this.nome = nome;
        this.data = data;
        this.nImage = nImage.intValue();
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
}
