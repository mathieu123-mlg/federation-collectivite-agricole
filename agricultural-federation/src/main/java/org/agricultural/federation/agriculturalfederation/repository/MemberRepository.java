package org.agricultural.federation.agriculturalfederation.repository;

import org.agricultural.federation.agriculturalfederation.entity.*;
import org.agricultural.federation.agriculturalfederation.exception.NotFoundException;
import org.agricultural.federation.agriculturalfederation.mapper.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MemberRepository {
    private final Connection connection;
    private final RowMapper rowMapper;

    public MemberRepository(Connection connection, RowMapper rowMapper) {
        this.connection = connection;
        this.rowMapper = rowMapper;
    }

    public List<Member> createMembers(Connection connection, List<CreateMember> membersToCreate) {
        String sql = """
                insert into member (id, first_name, last_name, birthdate, gender, address, profession, phone_number, email)
                values (?, ?, ?, ?, ?::gender, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            List<Member> members = new ArrayList<>();
            int index = getMemberNextIndex(connection);
            for (CreateMember createMember : membersToCreate) {
                String memberId = "M" + index;
                ps.setString(1, memberId);
                ps.setString(2, createMember.getFirstName());
                ps.setString(3, createMember.getLastName());
                ps.setDate(4, createMember.getBirthDate());
                ps.setString(5, createMember.getGender().name());
                ps.setString(6, createMember.getAddress());
                ps.setString(7, createMember.getProfession());
                ps.setString(8, createMember.getPhoneNumber());
                ps.setString(9, createMember.getEmail());
                ps.addBatch();
                Member member = createMember.mapToMember(memberId);
                members.add(member);
                index++;
            }
            ps.executeBatch();
            return members;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int getMemberNextIndex(Connection connection) {
        String sql = """
                select coalesce(max(cast(substring(id, 2) AS INTEGER)), 0) + 1 as id from member limit 1;
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new RuntimeException("table member is not found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Member> saveMembers(List<CreateMember> membersToCreate) {
        try {
            connection.setAutoCommit(false);
            List<Member> members = createMembers(connection, membersToCreate);
            List<String> collectivityIdentifiers = membersToCreate.stream().map(CreateMember::getCollectivityIdentifier).toList();
            attachMemberOccupation(connection, members, collectivityIdentifiers);
            attachMemberReferrals(connection, members, collectivityIdentifiers);
            try {
                connection.setAutoCommit(true);
                return members;
            } catch (RuntimeException e) {
                connection.rollback();
                connection.close();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void attachMemberOccupation(Connection connection, List<Member> membersToCreate, List<String> collectivityIdentifiers) {
        String sql = """
                insert into member_collectivity (id, collectivity_id, member_id, occupation)
                values (?, ?, ?, ?::occupation)""";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            Map<String, Integer> map = new HashMap<>();
            for (int i = 0; i < membersToCreate.size(); i++) {
                Member member = membersToCreate.get(i);
                String reference = collectivityIdentifiers.get(i).replace("col-", "");
                String id_ref = "C" + reference + "-M";
                int member_collectivity_index = getMemberCollectivityNextIndex(connection, reference);
                if (!map.containsKey(id_ref)) {
                    map.put(id_ref, member_collectivity_index);
                }
                String id = id_ref + map.get(id_ref);

                ps.setString(1, id);
                ps.setString(2, collectivityIdentifiers.get(i));
                ps.setString(3, member.getId());
                ps.setString(4, member.getOccupation().name());
                ps.addBatch();
                member_collectivity_index++;
                map.put(id_ref, member_collectivity_index);
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int getMemberCollectivityNextIndex(Connection connection, String reference) {
        String sql = """
                select coalesce(max(cast(substring(id, 5) AS INTEGER)), 0) + 1 as id
                from member_collectivity
                where id ilike '%s' limit 1;
                """;
        sql = sql.formatted("C" + reference + "-M%");
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new RuntimeException("table member_collectivity is not found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void attachMemberReferrals(Connection connection, List<Member> membersToCreate, List<String> collectivityIdentifiers) {
        String sql = """
                insert into member_referrals (collectivity_id, member_col_id, referrer_col_id, member_relation)
                values (?, ?, ?, ?)
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 0; i < membersToCreate.size(); i++) {
                Member member = membersToCreate.get(i);
                if (!member.getReferees().isEmpty()) {
                    for (Referee referee : member.getReferees()) {
                        ps.setString(1, collectivityIdentifiers.get(i));
                        ps.setString(2, getMemberCollectivityIdByMemberId(connection, member.getId(), collectivityIdentifiers.get(i)));
                        ps.setString(3, referee.getMemberId());
                        ps.setString(4, referee.getRelationship());
                        ps.addBatch();
                    }
                }
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getMemberCollectivityIdByMemberId(Connection connection, String memberId, String collectivityId) {
        String sql = """
                select id from member_collectivity
                where member_id = ? and collectivity_id = ?;
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, memberId);
            ps.setString(2, collectivityId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("id");
            }
            throw new NotFoundException("member_collectivity.id is not found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<MemberPayment> createMembersPayments(String id, List<CreateMemberPayment> createMembersPayments) {
        String sql = """
                    insert into payment (id, collectivity_id, member_col_id, amount, account_col_id, payment_mode, created_at)
                    values (?, ?, ?, ?, ?, ?::payment_mode, current_timestamp)
                    returning id, amount, payment_mode, account_col_id, created_at;
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            int index = getNextTransactionId(connection);
            createMembersTransaction(index, connection, id, createMembersPayments);
            List<MemberPayment> memberPayments = new ArrayList<>();
            for (CreateMemberPayment cp : createMembersPayments) {
                ps.setInt(1, index++);
                ps.setString(2, id);
                ps.setString(3, cp.getMembershipFeeIdentifier());
                ps.setDouble(4, cp.getAmount());
                ps.setString(5, cp.getAccountCreditedIdentifier());
                ps.setString(6, cp.getPaymentMode().name());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    memberPayments.add(new MemberPayment(
                            rs.getString("id"),
                            rs.getDouble("amount"),
                            PaymentMode.valueOf(rs.getString("payment_mode")),
                            findAccountCreditedByAccountCollectivityId(connection, rs.getString("account_col_id")),
                            rs.getTimestamp("created_at").toInstant()
                    ));
                } else {
                    throw new RuntimeException("Payment not found");
                }
            }
            try {
                connection.setAutoCommit(true);
                return memberPayments;
            } catch (RuntimeException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private FinancialAccount findAccountCreditedByAccountCollectivityId(Connection connection, String account_id) {
        String sql = """
                select ac.id, ac.collectivity_id, ac.account_type,
                       (select coalesce(sum(amount), 0)::numeric(20,2) from transaction) as amount,
                       ac.titular, ac.account_number
                from account_collectivity ac
                where id like ?
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, account_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String accountId = rs.getString("id");
                Double amount = rs.getDouble("amount");
                AccountType accountType = AccountType.valueOf(rs.getString("account_type"));
                String titular = rs.getString("titular");
                String accountNumber = rs.getString("account_number");

                if (accountType == AccountType.CASH) {
                    return new CashAccount(accountId, amount);
                } else if (accountType == AccountType.ORANGE_MONEY ||
                           accountType == AccountType.MVOLA ||
                           accountType == AccountType.AIRTEL_MONEY) {
                    return new MobileBankingAccount(
                            accountId, titular,
                            MobileBankingService.valueOf(rs.getString("account_type")),
                            accountNumber, amount
                    );
                }
                List<String> bankAccountNumber = List.of(accountNumber.split(" "));
                return new BankAccount(
                        accountId, titular, Bank.valueOf(rs.getString("account_type")),
                        Integer.parseInt(bankAccountNumber.get(0)),
                        Integer.parseInt(bankAccountNumber.get(1)),
                        bankAccountNumber.get(2),
                        Integer.parseInt(bankAccountNumber.get(3)),
                        amount
                );
            }
            throw new RuntimeException("Collectivity.account not found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createMembersTransaction(int index, Connection connection, String
            id, List<CreateMemberPayment> createMembersPayments) {
        String sql = """
                    insert into transaction (id, collectivity_id, member_col_id, amount, account_col_id, payment_mode, created_at)
                    values (?, ?, ?, ?, ?, ?::payment_mode, current_timestamp)
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (CreateMemberPayment cp : createMembersPayments) {
                ps.setInt(1, index++);
                ps.setString(2, id);
                ps.setString(3, cp.getMembershipFeeIdentifier());
                ps.setDouble(4, cp.getAmount());
                ps.setString(5, cp.getAccountCreditedIdentifier());
                ps.setString(6, cp.getPaymentMode().name());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int getNextTransactionId(Connection connection) {
        String sql = "select coalesce(max(id), 0) + 1 as id from transaction";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new RuntimeException("Transaction or payment not found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existingId(String memberId) {
        try (PreparedStatement ps = connection.prepareStatement("select id from member_collectivity where id = ?")) {
            ps.setString(1, memberId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
