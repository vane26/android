package com.google.sample.cloudvision.BD;

import java.util.UUID;

/**
 * Created by Vane on 30/08/2016.
 */
public class persona {
    private String run;
    private String apellidos;
    private String nombres;



    public persona(String run, String apellidos, String nombres){
        this.run = UUID.randomUUID().toString();
        this.apellidos = apellidos;
        this.nombres = nombres;

    }


    public String getRun(){
        return run;
    }

    public String getApellidos(){
        return apellidos;
    }

    public String getNombres(){
        return nombres;
    }


    public void setRun(String run) {
        this.run = run;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }


}
