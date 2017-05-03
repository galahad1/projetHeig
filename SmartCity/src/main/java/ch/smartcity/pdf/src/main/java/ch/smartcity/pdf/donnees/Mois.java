package ch.smartcity.pdf.donnees;

public enum Mois {
    JANVIER,
    FEVRIER,
    MARS,
    AVRIL,
    MAI,
    JUIN,
    JUILLET,
    AOUT,
    SEPTEMBRE,
    OCTOBRE,
    NOVEMBRE,
    DECEMBRE;

    public String toString() {
        return name().toLowerCase();
    }
}

