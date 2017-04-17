package models;

import java.util.Calendar;

public class Npa {
    private int idNpa;

    private String numeroNpa;

    private Calendar derniereMiseAJour;

    public Npa() {

    }

    public Npa(int idNpa, String numeroNpa, Calendar derniereMiseAJour) {
        this.idNpa = idNpa;
        this.numeroNpa = numeroNpa;
        this.derniereMiseAJour = derniereMiseAJour;
    }

    public int getIdNpa() {
        return idNpa;
    }

    public void setIdNpa(int idNpa) {
        this.idNpa = idNpa;
    }

    public String getNumeroNpa() {
        return numeroNpa;
    }

    public void setNumeroNpa(String numeroNpa) {
        this.numeroNpa = numeroNpa;
    }

    public Calendar getDerniereMiseAJour() {
        return derniereMiseAJour;
    }

    public void setDerniereMiseAJour(Calendar derniereMiseAJour) {
        this.derniereMiseAJour = derniereMiseAJour;
    }

    @Override
    public String toString() {
        return "Npa{" +
                "idNpa=" + idNpa +
                ", numeroNpa='" + numeroNpa + '\'' +
                ", derniereMiseAJour=" + derniereMiseAJour.getTime() +
                '}';
    }
}
