package com.example.myapplication.aba_cursos;

import com.google.gson.annotations.SerializedName;

public class Curso {
    
    @SerializedName("id")
    private int id;

    @SerializedName("title") // Mapeando pro possível nome no banco, ajuste se for diferente
    private String nome;
    
    @SerializedName("carga_horaria") // Exemplo, ajuste se for diferente
    private int cargaHoraria;
    
    private String status = "Disponível"; // Default, já que a listagem geral não traz o status de inscrição

    public Curso(String nome, int cargaHoraria, String status) {
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public int getCargaHoraria() { return cargaHoraria; }
    public void setCargaHoraria(int cargaHoraria) { this.cargaHoraria = cargaHoraria; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}