package com.lopes.jpa_h2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensagem")
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data_programado")
    private LocalDateTime dataProgramado;

    private String assunto;

    private String conteudo;

    @Column(name = "foi_enviado")
    private Boolean foiEnviado;
}
