package com.qanda;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KysymysDao implements Dao<Kysymys, Integer>{
    private Database database;

    public KysymysDao(Database database) {
        this.database = database;
    }

    @Override
    public Kysymys findOne(Integer key) throws SQLException {
        Connection conn = database.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kysymys WHERE id = ?");
        stmt.setInt(1, key);

        ResultSet rs = stmt.executeQuery();

        if (!rs.next()) {
            return null;
        }

        Kysymys k = new Kysymys(
                rs.getInt("id"),
                rs.getString("kurssi"),
                rs.getString("aihe"),
                rs.getString("kysymysteksti"));

        stmt.close();
        rs.close();
        conn.close();

        return k;
    }

    @Override
    public List<Kysymys> findAll() throws SQLException {
        Connection conn = database.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kysymys");

        ResultSet rs = stmt.executeQuery();

        ArrayList<Kysymys> lista = new ArrayList<>();

        while(rs.next()) {
            lista.add(new Kysymys(
                    rs.getInt("id"),
                    rs.getString("kurssi"),
                    rs.getString("aihe"),
                    rs.getString("kysymysteksti")));
        }

        stmt.close();
        rs.close();
        conn.close();

        return lista;
    }

    public List<String> findDistinctKurssi() throws SQLException {
        Connection conn = database.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT kurssi FROM Kysymys ");

        ResultSet rs = stmt.executeQuery();

        ArrayList<String> lista = new ArrayList<>();

        while(rs.next()) {
            lista.add(rs.getString("kurssi"));
        }

        stmt.close();
        rs.close();
        conn.close();

        return lista;
    }

    public List<String> findDistinctAiheByKurssi(String kurssi) throws SQLException {
        Connection conn = database.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT aihe FROM Kysymys WHERE kurssi = ?");
        stmt.setString(1, kurssi);

        ResultSet rs = stmt.executeQuery();

        ArrayList<String> lista = new ArrayList<>();

        while(rs.next()) {
            lista.add(rs.getString("aihe"));
        }

        stmt.close();
        rs.close();
        conn.close();

        return lista;
    }

    public List<String> findDistinctKysymystekstiByKurssiAndAihe(String kurssi, String aihe) throws SQLException {
        Connection conn = database.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT kysymysteksti FROM Kysymys WHERE kurssi = ? AND aihe = ?");
        stmt.setString(1, kurssi);
        stmt.setString(2, aihe);

        ResultSet rs = stmt.executeQuery();

        ArrayList<String> lista = new ArrayList<>();

        while(rs.next()) {
            lista.add(rs.getString("kysymysteksti"));
        }

        stmt.close();
        rs.close();
        conn.close();

        return lista;
    }

    @Override
    public Kysymys saveOrUpdate(Kysymys object) throws SQLException {
        Connection conn = database.getConnection();
        Kysymys k = findByKurssiAiheKysymysTeksti(object.getKurssi(), object.getAihe(), object.getKysymysteksti());
        if (k != null) return k;

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kysymys(kurssi, aihe, kysymysteksti) VALUES (?, ?, ?)");
        stmt.setString(1, object.getKurssi());
        stmt.setString(2, object.getAihe());
        stmt.setString(3, object.getKysymysteksti());

        stmt.executeUpdate();
        stmt.close();
        conn.close();

        return findByKurssiAiheKysymysTeksti(object.getKurssi(), object.getAihe(), object.getKysymysteksti());

    }

    @Override
    public void delete(Integer key) throws SQLException {
        new VastausDao(database).deleteVastausByKysymys(key);
        Connection conn = database.getConnection();

        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Kysymys WHERE id = ?");
        stmt.setInt(1, key);

        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    public Kysymys findByKurssiAiheKysymysTeksti(String kurssi, String aihe, String kysymysteksti) throws SQLException {
        Connection conn = database.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kysymys WHERE kurssi = ? AND aihe = ? AND kysymysteksti = ?");
        stmt.setString(1, kurssi);
        stmt.setString(2, aihe);
        stmt.setString(3, kysymysteksti);

        ResultSet rs = stmt.executeQuery();
        Kysymys k = null;
        if (rs.next()) {
            k = new Kysymys(rs.getInt("id"), rs.getString("kurssi"), rs.getString("aihe"), rs.getString("kysymysteksti"));
        }

        stmt.close();
        conn.close();
        rs.close();

        return k;
    }

    // Juu...
    public HashMap<String, HashMap<String, List<Kysymys>>> createFilteredMap() throws SQLException{
        ArrayList<String> kurssit = (ArrayList<String>) findDistinctKurssi();
        HashMap<String, HashMap<String, List<Kysymys>>> palautus = new HashMap<>();

        for(int i = 0; i < kurssit.size(); i++) {
            ArrayList<String> aiheet = (ArrayList<String>) findDistinctAiheByKurssi(kurssit.get(i));

            HashMap<String, List<Kysymys>> tmp = new HashMap<>();
            for(int j = 0; j < aiheet.size(); j++) {
                ArrayList<String> kysymykset = (ArrayList<String>) findDistinctKysymystekstiByKurssiAndAihe(kurssit.get(i), aiheet.get(j));
                ArrayList<Kysymys> kysymysOliot = new ArrayList<>();
                for(String s : kysymykset) {
                    Kysymys k = findByKurssiAiheKysymysTeksti(kurssit.get(i), aiheet.get(j), s);
                    if (k!= null) kysymysOliot.add(k);
                }
                tmp.put(aiheet.get(j), kysymysOliot);
            }
            palautus.put(kurssit.get(i), tmp);
        }

        return palautus;
    }
}
