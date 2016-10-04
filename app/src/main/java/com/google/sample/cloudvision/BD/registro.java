package com.google.sample.cloudvision.BD;

/**
 * Created by Vane on 31/08/2016.
 */
public class registro {
    private String indice;
    private String texto;
    private String calidad;

    public registro(String indice, String texto, String calidad) {
        this.indice = indice;
        this.texto = texto;
        this.calidad = calidad;


    }

    public registro() {

    }


    public registro(String texto) {
        this.texto = texto;
    }


    public registro(String indice, String calidad){
        this.indice = indice;
        this.calidad = calidad;
    }

    public String getIndice() {
        return indice;
    }

    public String getTexto() {
        return texto;
    }

    public String getCalidad() {
        return calidad;
    }


    public void setIndice(String indice) {
        this.indice = indice;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setCalidad(String calidad) {
        this.calidad = calidad;
    }



}


