package com.qanda;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

        ArrayList<Kysymys> lista = new ArrayList<Kysymys>();

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

    @Override
    public Kysymys saveOrUpdate(Kysymys object) throws SQLException {
        Connection conn = database.getConnection();

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kysymys(kurssi, aihe, kysymysteksti) VALUES (?, ?, ?)");
        stmt.setString(1, object.getKurssi());
        stmt.setString(2, object.getAihe());
        stmt.setString(3, object.getKysymysteksti());

        stmt.executeUpdate();
        stmt.close();
        conn.close();

        return null;

    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection conn = database.getConnection();

        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Kysymys WHERE id = ?");
        stmt.setInt(1, key);

        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }
}
