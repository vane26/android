package com.google.sample.cloudvision.BD;

/**
 * Created by Vane on 31/08/2016.
 */
public class Registro {
    private int id;
    private String indice;
    private String texto;
    private String calidad;
    private String imagen;

    public Registro(String indice, String texto, String calidad, String imagen) {
        this.indice = indice;
        this.texto = texto;
        this.calidad = calidad;
        this.imagen = imagen;


    }

    public Registro() {

    }


    public Registro(String indice, String calidad){
        this.indice = indice;
        this.calidad = calidad;
    }




    public int getId(){
        return id;
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

    public String getImagen(){
        return imagen;
    }

    public void  setId(int id){
        this.id = id;
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

    public void setImagen(String imagen){
        this.imagen = imagen;
    }
}


