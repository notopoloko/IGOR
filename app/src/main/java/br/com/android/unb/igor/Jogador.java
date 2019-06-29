package br.com.android.unb.igor;

import java.util.Date;

public class Jogador {
    private String id, email, senha, nome, sexo;
    private Date dataNascimento;

    public Jogador(String id, String email, String senha, String nome, String sexo, Date date) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.nome = nome;
        this.sexo = sexo;
        dataNascimento = date;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getNome() {
        return nome;
    }

    public String getSexo() {
        return sexo;
    }
}
