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
import tikape.runko.domain.SmoothieRaakaAine;

/**
 *
 * @author petri.nieminen
 */
public class AnnosRaakaAineDao implements Dao<SmoothieRaakaAine, Integer> {

    private Database database;
    String tableName;

    public AnnosRaakaAineDao(Database database, String tableName) {
        this.database = database;
        this.tableName = tableName;
    }

    public List<RaakaAine> listaaRaakaAineetAnnokselle(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT RaakaAine.id As id, RaakaAine.nimi As nimi, AnnosRaakaAine.maara As maara, AnnosRaakaAine.jarjestys as jarjestys FROM RaakaAine \n" +
"INNER JOIN AnnosRaakaAine ON RaakaAine.id = AnnosRaakaAine.raaka_aine_id\n" +
"Where AnnosRaakaAine.annos_id = ?\n"
                + "ORDER BY jarjestys ASC");
        stmt.setInt(1, key);
        ResultSet rs = stmt.executeQuery();
        
        List<RaakaAine> aineet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");
            RaakaAine temp = new RaakaAine(id, nimi);
            temp.setMaara(rs.getString("maara"));

            aineet.add(temp);
        }

        rs.close();
        stmt.close();
        connection.close();
        
        return aineet;
    }

    @Override
    public SmoothieRaakaAine findOne(Integer fkey1) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM AnnosRaakaAine WHERE annos_id = ?");
        stmt.setObject(1, fkey1);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        List<RaakaAine> aineet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            aineet.add(new RaakaAine(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        Integer raaka_id = rs.getInt("raaka_aine_id");
        Integer annos_id = rs.getInt("annos_id");
        Integer jarjestys = rs.getInt("jarjestys");
        String maara = rs.getString("maara");
        String ohje = rs.getString("ohje");

        SmoothieRaakaAine o = new SmoothieRaakaAine(raaka_id, annos_id, jarjestys, maara, ohje);

        rs.close();
        stmt.close();
        connection.close();

        return o;
    }

    @Override
    public List<SmoothieRaakaAine> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public SmoothieRaakaAine createFromRow(ResultSet resultSet) throws SQLException {
        return new SmoothieRaakaAine(resultSet.getInt("raaka_aine_id"), resultSet.getInt("annos_id"), resultSet.getInt("jarjestys"), resultSet.getString("maara"), resultSet.getString("ohje"));
    }

    public SmoothieRaakaAine saveOrUpdate(SmoothieRaakaAine object) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + tableName + " (raaka_aine_id,annos_id,jarjestys,maara,ohje) VALUES (?,?,?,?,?)");
            stmt.setInt(1, object.getRaaka_aine_id());
            stmt.setInt(2, object.getAnnos_id());
            stmt.setInt(3, object.getJarjestys());
            stmt.setString(4, object.getMaara());
            stmt.setString(5, object.getOhje());

            stmt.executeUpdate();

            stmt.close();

            PreparedStatement stmt3 = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE raaka_aine_id = ? AND annos_id = ?");
            stmt3.setInt(1, object.getRaaka_aine_id());
            stmt3.setInt(2, object.getAnnos_id());
            ResultSet rs = stmt3.executeQuery();
            rs.next(); // vain 1 tulos

            return createFromRow(rs);
        }
    }

}
