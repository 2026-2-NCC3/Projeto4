package com.example.myapplication;

/**
 * Representa um item da agenda do aluno.
 * Os nomes dos campos seguem exatamente as colunas retornadas pelo
 * backend em GET /api/agenda (view my_agenda), para facilitar o mapeamento
 * quando a integração real com a API for feita.
 */
public class AgendaItem {

    public String activityDate;   // ex.: "2026-03-01"
    public String startTime;      // ex.: "19:00:00"
    public String endTime;        // ex.: "21:00:00"
    public String courseTitle;    // ex.: "Introdução à Programação"
    public String title;          // ex.: "Aula 1 — Lógica de programação"
    public String location;       // ex.: "Sala 3"
    public boolean attended;      // true = aluno já compareceu a este encontro

    public AgendaItem(String activityDate, String startTime, String endTime,
                       String courseTitle, String title, String location,
                       boolean attended) {
        this.activityDate = activityDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.courseTitle = courseTitle;
        this.title = title;
        this.location = location;
        this.attended = attended;
    }
}
