package com.qanda;

public class Vastaus {
    private int id;
    private int kysymys_id;
    private String vastausteksti;
    private boolean oikein;

    public Vastaus(int id, int kysymys_id, String vastausteksti, boolean oikein) {
        this.id = id;
        this.kysymys_id = kysymys_id;
        this.vastausteksti = vastausteksti;
        this.oikein = oikein;
    }

    public int getId() {
        return id;
    }

    public int getKysymys_id() {
        return kysymys_id;
    }

    public String getVastausteksti() {
        return vastausteksti;
    }

    public boolean isOikein() {
        return oikein;
    }
}
