package br.com.grupodois.cadastroturmas.model;

import java.time.LocalDate;

public class Pessoa {
    private int id;
    private String nome;
    private String email;
    private String cpf;
    private LocalDate dataNascimento;
    private String trabalho;

    public Pessoa(int id, String nome, String email, String cpf, LocalDate dataNascimento, String trabalho) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.trabalho = trabalho;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getTrabalho() {
        return trabalho;
    }
}
