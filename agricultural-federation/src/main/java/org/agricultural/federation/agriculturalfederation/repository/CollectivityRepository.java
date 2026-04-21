package org.agricultural.federation.agriculturalfederation.repository;

import org.agricultural.federation.agriculturalfederation.entity.Collectivity;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CollectivityRepository {
    private final Connection connection;

    public CollectivityRepository(Connection connection) {
        this.connection = connection;
    }

    public List<Collectivity> getAllCollectivity() {
        String sql = "SELECT id, name, location, specialty, creation_datetime, federation_approval FROM collectivity";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Collectivity> list = new ArrayList<>();
            while (rs.next()) {
                list.add(
                        new Collectivity(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getString("location"),
                                rs.getTimestamp("creation_datetime").toInstant(),
                                rs.getBoolean("federation_approval")
                        )
                );
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
