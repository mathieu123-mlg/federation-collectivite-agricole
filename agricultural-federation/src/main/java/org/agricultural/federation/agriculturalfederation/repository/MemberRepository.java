package org.agricultural.federation.agriculturalfederation.repository;

import org.agricultural.federation.agriculturalfederation.entity.*;
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

    public MemberRepository(Connection connection, RowMapper rowMapper) {/*public Member save(CreateMember cm) {
        String sql = """
                INSERT INTO member
                (id, first_name, last_name, birthdate, gender, address,
                 profession, phone_number, email)
                VALUES (?, ?, ?, ?, ?::gender, ?, ?, ?, ?)
                RETURNING id, first_name, last_name, birthdate, gender,
                          address, profession, phone_number, email
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cm.getFirstName());
            ps.setString(2, cm.getLastName());
            ps.setDate(3, cm.getBirthDate());
            ps.setString(4, cm.getGender().name());
            ps.setString(5, cm.getAddress());
            ps.setString(6, cm.getProfession());
            ps.setString(7, cm.getPhoneNumber());
            ps.setString(8, cm.getEmail());
            ps.setTimestamp(9, Timestamp.from(Instant.now()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rowMapper.mapToMember(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save member", e);
        }
        throw new RuntimeException("Member not saved");
    }*//*public void saveReferee(String candidateId, Integer refereeId,
                            String collectivityId, String relationship) {
        String sql = """
                INSERT INTO member_referrals
                (member_col_id, referrer_col_id, collectivity_id, member_relation)
                VALUES (?, ?, ?, ?)
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, candidateId);
            ps.setInt(2, refereeId);
            ps.setString(3, collectivityId);
            ps.setString(4, relationship);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*//*    public void saveMemberCollectivity(String memberId, String collectivityId) {
        String sql = """
                INSERT INTO member_collectivity
                (id, member_id, collectivity_id)
                VALUES (?, ?)
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, memberId);
            ps.setString(2, collectivityId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*//*public boolean existsById(Integer id) {
        String sql = "SELECT 1 FROM member WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*//*public boolean isSeniorMember(Integer memberId) {
        String sql = """
                SELECT occupation FROM member_collectivity mc
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MemberOccupation role = MemberOccupation.valueOf(rs.getString("role"));
                    if (role != MemberOccupation.JUNIOR) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }*//*public Integer getCollectivityIdOfMember(Integer memberId) {
        String sql = """
                SELECT collectivity_id FROM member_collectivity
                WHERE member_id = ?
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("collectivity_id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }*//*public void savePayment(Integer memberId, CreateMemberPayment cp) {
        String sql = """
                    INSERT INTO payment
                    (id, collectivity_id, member_col_id, amount, account_col_id, payment_mode, created_at)
                    VALUES (?, ?, ?, ?, ?, ?::payment_mode, CURRENT_TIMESTAMP)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            ps.setDouble(2, cp.getAmount());
            ps.setString(3, cp.getPaymentMode().name());
            ps.setInt(4, Integer.parseInt(cp.getMembershipFeeIdentifier()));
            ps.setInt(5, Integer.parseInt(cp.getAccountCreditedIdentifier()));

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*//*    public List<Member> createMembers(List<CreateMember> newMembers) {
        String sql = """
                insert into member (id, first_name, last_name, birthdate, gender, address, profession, phone_number, email)
                values (?, ?, ?, ?::gender, ?, ?, ?, ?)
                on conflict (first_name, last_name) do nothing
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            Integer index = getMemberNextId(connection);
            List<Member> members = new ArrayList<>();
            for (CreateMember newMember : newMembers) {
                String id = "M" + index;
                ps.setString(1, id);
                ps.setString(2, newMember.getFirstName());
                ps.setString(3, newMember.getLastName());
                ps.setDate(4, newMember.getBirthDate());
                ps.setString(5, newMember.getGender().name());
                ps.setString(6, newMember.getAddress());
                ps.setString(7, newMember.getProfession());
                ps.setString(8, newMember.getPhoneNumber());
                ps.setString(9, newMember.getEmail());

                ps.addBatch();
                members.add(new Member(newMember.getFirstName(), newMember.getLastName(), newMember.getBirthDate(),
                        newMember.getGender(), newMember.getAddress(), newMember.getProfession(),
                        newMember.getPhoneNumber(), newMember.getEmail(), newMember.getOccupation(), id, newMember.getReferees()));

                index++;
            }
            ps.executeBatch();
            return members;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<?> saveMembersCollectivity(List<CreateMember> newMembers) {
        String sql = """
                insert into member_collectivity (id, first_name, last_name, birthdate, gender, address, profession,  phone_number, email) values (?, ?, ?, ?, ?::gender, ?, ?, ?, ?) on conflict do nothing
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            List<?> members = new ArrayList<>();
            for (CreateMember newMember : newMembers) {
               ps.setString(1, newMember.getCollectivityIdentifier());
               ps.setString(2, newMember.getCollectivityIdentifier());
               ps.setString(3, newMember.getCollectivityIdentifier());

                ps.addBatch();
                members.add(new Member(newMember.getFirstName(), newMember.getLastName(), newMember.getBirthDate(),
                        newMember.getGender(), newMember.getAddress(), newMember.getProfession(),
                        newMember.getPhoneNumber(), newMember.getEmail(), newMember.getOccupation(), id, newMember.getReferees()));
            }
            ps.executeBatch();
            return members;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Integer getMemberNextId(Connection connection) {
        String sql = "select coalesce(max(cast(substring(id, 2) AS INTEGER)), 0) + 1 as max_number from member";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/
        this.connection = connection;
        this.rowMapper = rowMapper;
    }

    public List<Member> createMembers(Connection connection, List<CreateMember> membersToCreate) {
        String sql = """
                insert into member (id, first_name, last_name, birthdate, gender, address, profession, phone_number, email)
                values (?, ?, ?, ?, ?::gender, ?, ?, ?, ?)""";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            List<Member> members = new ArrayList<>();
            int index = getMemberNextIndex(connection);
            for (CreateMember createMember : membersToCreate) {
                ps.setString(1, "M" + index++);
                ps.setString(2, createMember.getFirstName());
                ps.setString(3, createMember.getLastName());
                ps.setDate(4, createMember.getBirthDate());
                ps.setString(5, createMember.getGender().name());
                ps.setString(6, createMember.getAddress());
                ps.setString(7, createMember.getProfession());
                ps.setString(8, createMember.getPhoneNumber());
                ps.setString(9, createMember.getEmail());
                ps.addBatch();
                members.add(createMember.mapToMember());
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
            attachMemberReferrals(connection, members, collectivityIdentifiers);
            attachMemberOccupation(connection, members, collectivityIdentifiers);
            connection.setAutoCommit(true);
            try {
                return members;
            } catch (RuntimeException e) {
                connection.rollback();
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
                where id ilike %s limit 1;
                """.formatted("C" + reference + "-M%");
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
                values (?, ?, ?, ?)""";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 0; i < membersToCreate.size(); i++) {
                Member member = membersToCreate.get(i);
                if (!member.getReferees().isEmpty()) {
                    for (Referee referee : member.getReferees()) {
                        ps.setString(1, collectivityIdentifiers.get(i));
                        ps.setString(2, member.getId());
                        ps.setString(3, referee.getMemberId());
                        ps.setString(4, referee.getRelationship());
                    }
                }
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<MemberPayment> createMembersPayments(String id, List<CreateMemberPayment> createMembersPayments) {
        String sql = """
                    insert into payment (collectivity_id, member_col_id, amount, account_col_id, payment_mode, created_at)
                    values (?, ?, ?, ?, ?::payment_mode, current_timestamp)
                    returning id, collectivity_id, member_col_id, amount, account_col_id, payment_mode, created_at;
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            createMembersTransaction(connection, id, createMembersPayments);
            List<MemberPayment> memberPayments = new ArrayList<>();
            for (CreateMemberPayment cp : createMembersPayments) {
                ps.setString(1, id);
                ps.setString(2, cp.getMembershipFeeIdentifier());
                ps.setDouble(3, cp.getAmount());
                ps.setString(4, cp.getAccountCreditedIdentifier());
                ps.setString(5, cp.getPaymentMode().name());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    memberPayments.add(new MemberPayment(
                            rs.getString("id"),
                            rs.getDouble("amount"),
                            PaymentMode.valueOf(rs.getString("payment_mode")),
                            findAccountCreditedByAccountCollectivityId(connection, rs.getString("account_col_id")),
                            rs.getTimestamp("created_at").toInstant()
                    ));
                }
                throw new RuntimeException("payment not found");
            }
            connection.setAutoCommit(true);
            try {
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

    private void createMembersTransaction(Connection connection, String id, List<CreateMemberPayment> createMembersPayments) {
        String sql = """
                    insert into transaction (collectivity_id, member_col_id, amount, account_col_id, payment_mode, created_at)
                    values (?, ?, ?, ?, ?::payment_mode, current_timestamp)
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (CreateMemberPayment cp : createMembersPayments) {
                ps.setString(1, id);
                ps.setString(2, cp.getMembershipFeeIdentifier());
                ps.setDouble(3, cp.getAmount());
                ps.setString(4, cp.getAccountCreditedIdentifier());
                ps.setString(5, cp.getPaymentMode().name());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
