package com.qanda;

import java.sql.SQLException;
import java.util.List;

public class VastausDao implements Dao<Vastaus, Integer> {
    private Database database;

    public VastausDao(Database database) {
        this.database = database;
    }

    @Override
    public Vastaus findOne(Integer key) throws SQLException {
        return null;
    }

    @Override
    public List<Vastaus> findAll() throws SQLException {
        return null;
    }

    @Override
    public Vastaus saveOrUpdate(Vastaus object) throws SQLException {
        return null;
    }

    @Override
    public void delete(Integer key) throws SQLException {

    }
}
