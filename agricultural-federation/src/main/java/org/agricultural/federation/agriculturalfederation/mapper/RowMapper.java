package org.agricultural.federation.agriculturalfederation.mapper;

import org.agricultural.federation.agriculturalfederation.entity.Collectivity;
import org.agricultural.federation.agriculturalfederation.entity.Gender;
import org.agricultural.federation.agriculturalfederation.entity.Member;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RowMapper {

    public Collectivity mapToCollectivity(ResultSet rs) throws SQLException {
        Collectivity c = new Collectivity();
        c.setId(rs.getInt("id"));
        c.setNumber(rs.getInt("number"));
        c.setName(rs.getString("name"));
        c.setLocation(rs.getString("location"));
        c.setSpeciality(rs.getString("speciality"));
        c.setCreationDate(rs.getTimestamp("created_at").toInstant());
        c.setFederationApproval(rs.getBoolean("federation_approval"));
        return c;
    }

    public Member mapToMember(ResultSet rs) throws SQLException {
        return new Member(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getDate("birth_date").toLocalDate().atStartOfDay().toInstant(java.time.ZoneOffset.UTC),
                Gender.valueOf(rs.getString("gender")),
                rs.getString("address"),
                rs.getString("profession"),
                rs.getString("phone_number"),
                rs.getString("email"),
                rs.getDate("adhesion_date").toLocalDate().atStartOfDay().toInstant(java.time.ZoneOffset.UTC)
        );
    }
}
