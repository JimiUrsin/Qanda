package main.java.com.qanda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VastausDao implements Dao<Vastaus, Integer> {
    private Database database;
    private KysymysDao kysymysDao;

    public VastausDao(Database database) {
        this.database = database;
        this.kysymysDao = new KysymysDao(database);
    }

    @Override
    public Vastaus findOne(Integer key) throws SQLException {
        Connection conn = database.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Vastaus WHERE id = ?");
        stmt.setInt(1, key);

        ResultSet rs = stmt.executeQuery();

        if (!rs.next()) {
            return null;
        }

        Vastaus v = new Vastaus(
                rs.getInt("id"),
                kysymysDao.findOne(rs.getInt("kysymys_id")),
                rs.getString("vastausteksti"),
                rs.getBoolean("oikein"));

        stmt.close();
        rs.close();
        conn.close();

        return v;
    }

    @Override
    public List<Vastaus> findAll() throws SQLException {
        Connection conn = database.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Vastaus");

        ResultSet rs = stmt.executeQuery();

        ArrayList<Vastaus> lista = new ArrayList<>();

        while(rs.next()) {
            lista.add(new Vastaus(
                    rs.getInt("id"),
                    kysymysDao.findOne(rs.getInt("kysymys_id")),
                    rs.getString("aihe"),
                    rs.getBoolean("oikein")));
        }

        stmt.close();
        rs.close();
        conn.close();

        return lista;
    }

    @Override
    public Vastaus saveOrUpdate(Vastaus object) throws SQLException {
        Connection conn = database.getConnection();
        Vastaus v = findByKysymysVastausteksti(object, object.getVastausteksti());
        if (v != null) return v;

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Vastaus(kysymys_id, vastausteksti, oikein) VALUES (?, ?, ?)");
        stmt.setInt(1, object.getKysymys().getId());
        stmt.setString(2, object.getVastausteksti());
        stmt.setBoolean(3, object.isOikein());

        stmt.executeUpdate();
        stmt.close();
        conn.close();

        return findByKysymysVastausteksti(object, object.getVastausteksti());
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection conn = database.getConnection();

        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Vastaus WHERE id = ?");
        stmt.setInt(1, key);

        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    public Vastaus findByKysymysVastausteksti(Vastaus vastaus, String vastausteksti) throws SQLException{
        Connection conn = database.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Vastaus WHERE kysymys_id = ? AND vastausteksti = ?");
        stmt.setInt(1, vastaus.getKysymys().getId());
        stmt.setString(2, vastaus.getVastausteksti());

        ResultSet rs = stmt.executeQuery();

        Vastaus v = null;
        if (rs.next()) {
            v = new Vastaus(rs.getInt("id"), kysymysDao.findOne(rs.getInt("kysymys_id")), rs.getString("vastausteksti"), rs.getBoolean("oikein"));
        }

        stmt.close();
        conn.close();
        rs.close();

        return v;
    }

    public ArrayList<Vastaus> findByKysymysId(int kysymysId) throws SQLException{
        Connection conn = database.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Vastaus WHERE kysymys_id = ?");
        stmt.setInt(1, kysymysId);
        ResultSet rs = stmt.executeQuery();

        ArrayList<Vastaus> lista = new ArrayList<>();

        while(rs.next()) {
            lista.add(new Vastaus(
                    rs.getInt("id"),
                    kysymysDao.findOne(rs.getInt("kysymys_id")),
                    rs.getString("vastausteksti"),
                    rs.getBoolean("oikein")));
        }

        stmt.close();
        rs.close();
        conn.close();

        return lista;

    }

    public void deleteVastausByKysymys(int kysymysId) throws SQLException{
        for(Vastaus v : findByKysymysId(kysymysId)) {
            delete(v.getId());
        }
    }
}
