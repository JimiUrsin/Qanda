package main.java.com.qanda;

public class Kysymys {
    private int id;
    private String kurssi;
    private String aihe;
    private String kysymysteksti;

    public Kysymys(int id, String kurssi, String aihe, String kysymysteksti) {
        this.id = id;
        this.kurssi = kurssi;
        this.aihe = aihe;
        this.kysymysteksti = kysymysteksti;
    }

    public int getId() {
        return id;
    }

    public String getKurssi() {
        return kurssi;
    }

    public String getAihe() {
        return aihe;
    }

    public String getKysymysteksti() {
        return kysymysteksti;
    }
}
