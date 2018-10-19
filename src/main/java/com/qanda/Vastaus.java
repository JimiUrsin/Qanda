package main.java.com.qanda;

public class Vastaus {
    private int id;
    private Kysymys kysymys;
    private String vastausteksti;
    private boolean oikein;

    public Vastaus(int id, Kysymys kysymys, String vastausteksti, boolean oikein) {
        this.id = id;
        this.kysymys = kysymys;
        this.vastausteksti = vastausteksti;
        this.oikein = oikein;
    }

    public int getId() {
        return id;
    }

    public Kysymys getKysymys() {
        return kysymys;
    }

    public String getVastausteksti() {
        return vastausteksti;
    }

    public boolean isOikein() {
        return oikein;
    }
}
