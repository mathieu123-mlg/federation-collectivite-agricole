package org.agricultural.federation.agriculturalfederation.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.agricultural.federation.agriculturalfederation.entity.CreateMembershipFee;
import org.agricultural.federation.agriculturalfederation.entity.MembershipFee;
import org.springframework.stereotype.Repository;

@Repository
public class MembershipFeeRepository {

    private final Connection connection;

    public MembershipFeeRepository(Connection connection) {
        this.connection = connection;
    }

    public List<MembershipFee> findByCollectivityId(Integer collectivityId) {
        String sql = """
            SELECT id, eligible_from, frequency, amount, label, status
            FROM membership_fee WHERE collectivity_id = ?
            """;
        List<MembershipFee> fees = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, collectivityId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MembershipFee fee = new MembershipFee();
                    fee.setId(rs.getInt("id"));
                    fee.setEligibleFrom(rs.getDate("eligible_from").toLocalDate());
                    fee.setFrequency(rs.getString("frequency"));
                    fee.setAmount(rs.getDouble("amount"));
                    fee.setLabel(rs.getString("label"));
                    fee.setStatus(rs.getString("status"));
                    fees.add(fee);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return fees;
    }

    public MembershipFee save(Integer collectivityId, CreateMembershipFee cm) {
        String sql = """
            INSERT INTO membership_fee
            (collectivity_id, eligible_from, frequency, amount, label)
            VALUES (?, ?, ?::frequency_enum, ?, ?)
            RETURNING id, eligible_from, frequency, amount, label, status
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, collectivityId);
            ps.setDate(2, java.sql.Date.valueOf(cm.getEligibleFrom()));
            ps.setString(3, cm.getFrequency());
            ps.setDouble(4, cm.getAmount());
            ps.setString(5, cm.getLabel());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MembershipFee fee = new MembershipFee();
                    fee.setId(rs.getInt("id"));
                    fee.setEligibleFrom(rs.getDate("eligible_from").toLocalDate());
                    fee.setFrequency(rs.getString("frequency"));
                    fee.setAmount(rs.getDouble("amount"));
                    fee.setLabel(rs.getString("label"));
                    fee.setStatus(rs.getString("status"));
                    return fee;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Failed to save membership fee");
    }
}
