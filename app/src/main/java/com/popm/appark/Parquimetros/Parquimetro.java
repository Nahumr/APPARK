package com.popm.appark.Parquimetros;

import java.util.HashMap;

public class Parquimetro {

    int sensorID;
    String estado;
    Float latitudX, latitudY;

    public int getSensorID() {
        return sensorID;
    }

    public String getEstado() {
        return estado;
    }

    public Float getLatitudX() {
        return latitudX;
    }

    public Float getLatitudY() {
        return latitudY;
    }

    public Parquimetro(int sensorID, String estado, Float latitudX, Float latitudY) {
        this.sensorID = sensorID;
        this.estado = estado;
        this.latitudX = latitudX;
        this.latitudY = latitudY;
    }
}
