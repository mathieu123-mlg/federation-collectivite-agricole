package org.agricultural.federation.agriculturalfederation.mapper;

import org.agricultural.federation.agriculturalfederation.entity.*;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class RowMapper {

    public Collectivity mapToCollectivity(ResultSet rs) throws SQLException {
        return new Collectivity(
                rs.getString("id"),
                rs.getInt("number"),
                rs.getString("name"),
                rs.getString("location"),
                rs.getString("speciality"),
                null,
                null,
                (rs.getObject("updated_at") == null ? null
                        : rs.getTimestamp("updated_at").toInstant())
        );
//        c.setFederationApproval(rs.getBoolean("federation_approval"));
    }

    public Member mapToMember(ResultSet rs) throws SQLException {
        return new Member(
                rs.getString("member_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getDate("birthdate"),
                Gender.valueOf(rs.getString("gender")),
                rs.getString("address"),
                rs.getString("profession"),
                rs.getString("phone_number"),
                rs.getString("email"),
                MemberOccupation.valueOf(rs.getString("occupation"))
        );
    }

    public PaymentMode mapPaymentMode(String dbValue) {
        return switch (dbValue) {
            case "CASH" -> PaymentMode.CASH;
            case "BANK_TRANSFER" -> PaymentMode.BANK_TRANSFER;
            case "MOBILE_MONEY" -> PaymentMode.MOBILE_MONEY;
            default -> throw new IllegalArgumentException("Unknown payment method: " + dbValue);
        };
    }

    public MembershipFee mapToMembershipFee(ResultSet rs) throws SQLException {
        MembershipFee membershipFee = new MembershipFee();
        membershipFee.setEligibleFrom(rs.getDate("eligible_from"));
        membershipFee.setFrequency(Frequency.valueOf(rs.getString("frequency")));
        membershipFee.setAmount(rs.getDouble("amount"));
        membershipFee.setLabel(rs.getString("label"));
        membershipFee.setId(rs.getString("id"));
        membershipFee.setStatus(Status.valueOf(rs.getString("status")));
        return membershipFee;
    }

    public Transaction mapToTransaction(ResultSet rs, FinancialAccount financialAccount) throws SQLException {
        Transaction t = new Transaction();
        t.setId(rs.getInt("id"));
        t.setCreationDate(rs.getTimestamp("created_at").toInstant());
        t.setAmount(rs.getDouble("amount"));
        t.setPaymentMode(PaymentMode.valueOf(rs.getString("payment_mode")));
        t.setAccountCredited(financialAccount);
        t.setMemberDebited(mapToMember(rs));
        return t;
    }

    public CollectivityStructure mapToCollectivityStructure(List<Member> members) {
        return new CollectivityStructure(
                members.stream().filter(x -> x.getOccupation() == MemberOccupation.PRESIDENT).findFirst().orElse(null),
                members.stream().filter(x -> x.getOccupation() == MemberOccupation.VICE_PRESIDENT).findFirst().orElse(null),
                members.stream().filter(x -> x.getOccupation() == MemberOccupation.TREASURER).findFirst().orElse(null),
                members.stream().filter(x -> x.getOccupation() == MemberOccupation.SECRETARY).findFirst().orElse(null)
        );
    }
}
