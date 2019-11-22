package com.lopes.jpa_h2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "envio")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;

    @ManyToOne
    @JoinColumn(name = "contato_id")
    private Contato contato;

    @ManyToOne
    @JoinColumn(name = "mensagem_id")
    private Mensagem mensagem;

    private Boolean abriu;

    private Boolean clicou;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    public Mensagem getMensagem() {
        return mensagem;
    }

    public void setMensagem(Mensagem mensagem) {
        this.mensagem = mensagem;
    }

    public Boolean getAbriu() {
        return abriu;
    }

    public void setAbriu(Boolean abriu) {
        this.abriu = abriu;
    }

    public Boolean getClicou() {
        return clicou;
    }

    public void setClicou(Boolean clicou) {
        this.clicou = clicou;
    }

}
