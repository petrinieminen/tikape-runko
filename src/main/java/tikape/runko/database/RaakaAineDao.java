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

public class RaakaAineDao implements Dao<RaakaAine, Integer> {

    private Database database;
    String tableName;

    public RaakaAineDao(Database database, String tableName) {
        this.database = database;
        this.tableName = tableName;
    }

    @Override
    public RaakaAine findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        RaakaAine o = new RaakaAine(id, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return o;
    }

    @Override
    public List<RaakaAine> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine");

        ResultSet rs = stmt.executeQuery();
        List<RaakaAine> aineet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            aineet.add(new RaakaAine(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return aineet;
    }

    public RaakaAine saveOrUpdate(RaakaAine object) throws SQLException {
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

    public RaakaAine createFromRow(ResultSet resultSet) throws SQLException {
        return new RaakaAine(resultSet.getInt("id"), resultSet.getString("nimi"));
    }

    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + tableName + " WHERE id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
            stmt.close();

        }
    }

}
