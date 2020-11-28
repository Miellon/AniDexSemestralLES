package com.example.projsemestralles;

import com.google.firebase.database.Exclude;

public class Animal {
    private String especie;
    private String peso;
    private String comprimento;
    private String descricao;
    private String local_foto;
    private String mKey;
    public Animal(){

    }
    public Animal(String especie, String peso, String comprimento, String descricao, String local_foto) {
        this.especie = especie;
        this.peso=peso;
        this.comprimento=comprimento;
        this.descricao=descricao;
        this.local_foto=local_foto;
    }
    public Animal(String mKey, String especie){
        this.mKey = mKey;
        this.especie=especie;
    }


    public String getPeso() {
        return peso;
    }

    public String getComprimento() {
        return comprimento;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public void setComprimento(String comprimento) {
        this.comprimento = comprimento;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocal_foto() {
        return local_foto;
    }

    public void setLocal_foto(String local_foto) {
        this.local_foto = local_foto;
    }
    @Exclude
    public String getKey(){
        return mKey;
    }

    public void setKey(String key){
        mKey = key;
    }

    @Override
    public String toString() {
        return especie;
    }
}
