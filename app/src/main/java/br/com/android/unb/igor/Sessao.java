package br.com.android.unb.igor;

import java.util.Date;

public class Sessao {
    private String sinopseSessao, nome;
    private Date dataSessao;

    public Sessao(String sinopseSessao, String nome, Date dataSessao) {
        this.sinopseSessao = sinopseSessao;
        this.nome = nome;
        this.dataSessao = dataSessao;
    }

    public String getSinopseSessao() {
        return sinopseSessao;
    }

    public String getNome() {
        return nome;
    }

    public Date getDataSessao() {
        return dataSessao;
    }
}
