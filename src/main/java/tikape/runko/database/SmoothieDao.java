/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.RaakaAine;
import tikape.runko.domain.Smoothie;

/**
 *
 * @author petri.nieminen
 */
public class SmoothieDao implements Dao<Smoothie, Integer> {

    private Database database;
    String tableName;

    public SmoothieDao(Database database, String tableName) {
        this.database = database;
        this.tableName = tableName;
    }

    @Override
    public Smoothie findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Annos WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        Smoothie o = new Smoothie(id, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return o;

    }

    @Override
    public List<Smoothie> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Annos");

        ResultSet rs = stmt.executeQuery();
        List<Smoothie> annokset = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            annokset.add(new Smoothie(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return annokset;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + tableName + " WHERE id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
            stmt.close();

        }
    }

    public Smoothie saveOrUpdate(Smoothie object) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + tableName + " (nimi) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, object.getNimi());
            stmt.executeUpdate();
            //haetaan äsken insertatun incremental ID
            ResultSet rsKey = stmt.getGeneratedKeys();
            int latestId = 0;
            if (rsKey.next()) {
                latestId = rsKey.getInt(1);
            }

            stmt.close();
            PreparedStatement stmt3 = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE id = ?");
            stmt3.setInt(1, latestId);
            ResultSet rs = stmt3.executeQuery();
            rs.next(); // vain 1 tulos

            return createFromRow(rs);
        }
    }

    public Smoothie createFromRow(ResultSet resultSet) throws SQLException {
        return new Smoothie(resultSet.getInt("id"), resultSet.getString("nimi"));
    }

}
