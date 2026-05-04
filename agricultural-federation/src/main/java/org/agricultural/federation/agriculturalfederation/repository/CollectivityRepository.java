package org.agricultural.federation.agriculturalfederation.repository;

import org.agricultural.federation.agriculturalfederation.entity.AccountType;
import org.agricultural.federation.agriculturalfederation.entity.Bank;
import org.agricultural.federation.agriculturalfederation.entity.BankAccount;
import org.agricultural.federation.agriculturalfederation.entity.CashAccount;
import org.agricultural.federation.agriculturalfederation.entity.Collectivity;
import org.agricultural.federation.agriculturalfederation.entity.CollectivityIdentifier;
import org.agricultural.federation.agriculturalfederation.entity.CreateCollectivity;
import org.agricultural.federation.agriculturalfederation.entity.CreateMembershipFee;
import org.agricultural.federation.agriculturalfederation.entity.FinancialAccount;
import org.agricultural.federation.agriculturalfederation.entity.Frequency;
import org.agricultural.federation.agriculturalfederation.entity.Member;
import org.agricultural.federation.agriculturalfederation.entity.MembershipFee;
import org.agricultural.federation.agriculturalfederation.entity.MobileBankingAccount;
import org.agricultural.federation.agriculturalfederation.entity.MobileBankingService;
import org.agricultural.federation.agriculturalfederation.entity.PaymentMode;
import org.agricultural.federation.agriculturalfederation.entity.Referee;
import org.agricultural.federation.agriculturalfederation.entity.Status;
import org.agricultural.federation.agriculturalfederation.entity.Transaction;
import org.agricultural.federation.agriculturalfederation.mapper.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CollectivityRepository {
    private final Connection connection;
    private final RowMapper rowMapper;

    public CollectivityRepository(Connection connection, RowMapper rowMapper) {
        this.connection = connection;
        this.rowMapper = rowMapper;
    }

    public Optional<Collectivity> findById(String id) {
        String sql = """
                select col.id, col.number, col.name, col.location, col.speciality, col.updated_at,
                       mc.member_id, first_name, last_name, birthdate, gender, address, profession, phone_number, email, mc.occupation
                from collectivity col
                join member_collectivity mc on mc.collectivity_id = col.id
                join member m on mc.member_id = m.id
                where col.id = ?
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);

            Collectivity collectivity = null;
            List<Member> members = new ArrayList<>();

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (collectivity == null) {
                    collectivity = rowMapper.mapToCollectivity(rs);
                }
                Member member = rowMapper.mapToMember(rs);
                List<Referee> referees = getMemberRefereeById(connection, member.getId(), collectivity.getId());
                member.setReferees(referees);
                members.add(member);
            }
            if (collectivity != null) {
                collectivity.setMembers(members);
                collectivity.setCollectivityStructure(rowMapper.mapToCollectivityStructure(members));
                return Optional.of(collectivity);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Member> getCollectivityMembersById(String collectivity_id) {
        String sql = """
                select mc.member_id, m.first_name, m.last_name, m.birthdate, m.gender, m.address, m.profession,
                       m.phone_number, m.email, mc.occupation
                from member_collectivity mc
                join member m on mc.member_id = m.id
                where mc.collectivity_id = ?;
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, collectivity_id);
            ResultSet rs = ps.executeQuery();
            List<Member> members = new ArrayList<>();
            while (rs.next()) {
                Member member = rowMapper.mapToMember(rs);
                member.setReferees(getMemberRefereeById(connection, member.getId(), collectivity_id));
                members.add(member);
            }
            return members;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Referee> getMemberRefereeById(Connection connection, String member_id, String collectivity_id) {
        String sql = """
                select referrer_col_id, member_relation
                from member_collectivity mc
                join member_referrals mr
                on mr.member_col_id = mc.id
                where member_id = ? and mc.collectivity_id = ?;
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, member_id);
            ps.setString(2, collectivity_id);
            ResultSet rs = ps.executeQuery();
            List<Referee> referees = new ArrayList<>();
            while (rs.next()) {
                referees.add(new Referee(
                        rs.getString("referrer_col_id"),
                        rs.getObject("member_relation") == null ? null : rs.getString("member_relation")
                ));
            }
            return referees;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<MembershipFee> findMembershipFeeById(String id) {
        String sql = """
                select id, label, status, frequency, eligible_from, amount
                from membership_fee
                where collectivity_id = ?;
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(rowMapper.mapToMembershipFee(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Transaction> getCollectivityTransactions(String id, Instant from, Instant to) {
        String sql = """
                select t.id, t.created_at, t.amount, t.payment_mode,
                       ac.id as account_id, ac.account_type, ac.amount as account_amount, ac.titular, ac.account_number,
                       m.id as member_id, m.first_name, m.last_name, m.birthdate, m.gender, m.address, m.profession, m.phone_number, m.email,
                       mc.occupation
                from transaction t
                join account_collectivity ac on ac.id = t.account_col_id
                join member_collectivity mc on t.member_col_id = mc.id
                join member m on mc.member_id = m.id
                where t.collectivity_id = ? and t.created_at between ? and ?;
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setTimestamp(2, Timestamp.from(from));
            ps.setTimestamp(3, Timestamp.from(to));

            ResultSet rs = ps.executeQuery();
            List<Transaction> transactions = new ArrayList<>();
            while (rs.next()) {
                FinancialAccount financialAccount = buildFinancialAccountFromResultSet(rs);
                transactions.add(rowMapper.mapToTransaction(rs, financialAccount));
            }
            return transactions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private FinancialAccount buildFinancialAccountFromResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString("account_id");
        Double amount = rs.getDouble("account_amount");
        String accountType = rs.getString("account_type");
        PaymentMode paymentMode = PaymentMode.valueOf(rs.getString("payment_mode"));
        String titular = rs.getString("titular");
        String accountNumber = rs.getString("account_number");

        if (paymentMode == PaymentMode.CASH) {
            return new CashAccount(id, amount);
        } else if (paymentMode == PaymentMode.MOBILE_MONEY) {
            return new MobileBankingAccount(
                    id, titular,
                    MobileBankingService.valueOf(accountType),
                    accountNumber, amount
            );
        } else {
            List<String> bankAccountNumber = List.of(accountNumber.split(" "));
            return new BankAccount(
                    id, titular, Bank.valueOf(accountType),
                    Integer.parseInt(bankAccountNumber.get(0)),
                    Integer.parseInt(bankAccountNumber.get(1)),
                    bankAccountNumber.get(2),
                    Integer.parseInt(bankAccountNumber.get(3)),
                    amount
            );
        }
    }

    public void updateCollectivityIdentifier(String id, CollectivityIdentifier collectivityIdentifier) {
        String sql = """
                update collectivity set number = ?, name = ?, updated_at = current_timestamp
                where id = ? and updated_at is null
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, collectivityIdentifier.getNumber());
            ps.setString(2, collectivityIdentifier.getName());
            ps.setString(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FinancialAccount> getFinancialAccounts(String id, Date at) {
        String sql = """
                select ac.id, ac.collectivity_id, ac.account_type,
                       (select coalesce(sum(amount), 0)::numeric(20,2) from transaction where created_at = ?) as amount,
                       ac.titular, ac.account_number
                from account_collectivity ac where collectivity_id = ?
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, at);
            ps.setString(2, id);
            ResultSet rs = ps.executeQuery();
            List<FinancialAccount> financialAccounts = new ArrayList<>();
            while (rs.next()) {
                String accountId = rs.getString("id");
                Double amount = rs.getDouble("amount");
                AccountType accountType = AccountType.valueOf(rs.getString("account_type"));
                String titular = rs.getString("titular");
                String accountNumber = rs.getString("account_number");

                if (accountType == AccountType.CASH) {
                    financialAccounts.add(new CashAccount(accountId, amount));
                } else if (accountType == AccountType.ORANGE_MONEY ||
                           accountType == AccountType.MVOLA ||
                           accountType == AccountType.AIRTEL_MONEY) {
                    financialAccounts.add(new MobileBankingAccount(
                            accountId, titular,
                            MobileBankingService.valueOf(rs.getString("account_type")),
                            accountNumber, amount
                    ));
                } else {
                    List<String> bankAccountNumber = List.of(accountNumber.split(" "));
                    financialAccounts.add(new BankAccount(
                            accountId, titular, Bank.valueOf(rs.getString("account_type")),
                            Integer.parseInt(bankAccountNumber.get(0)),
                            Integer.parseInt(bankAccountNumber.get(1)),
                            bankAccountNumber.get(2),
                            Integer.parseInt(bankAccountNumber.get(3)),
                            amount
                    ));
                }
            }
            return financialAccounts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<MembershipFee> createMembershipFee(String id, List<CreateMembershipFee> createMembershipFees) {
        String sql = """
                insert into membership_fee (id, label, status, frequency, eligible_from, amount, collectivity_id)
                values (?, ?, 'ACTIVE', ?::frequency, ?, ?, ?)
                returning eligible_from, frequency, amount, label, id,  status
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            List<MembershipFee> membershipFees = new ArrayList<>();
            for (CreateMembershipFee createMembershipFee : createMembershipFees) {
                ps.setString(1, "cot-" + getNextMembershipFeeId(connection));
                ps.setString(2, createMembershipFee.getLabel());
                ps.setString(3, createMembershipFee.getFrequency().name());
                ps.setDate(4, createMembershipFee.getEligibleFrom());
                ps.setDouble(5, createMembershipFee.getAmount());
                ps.setString(6, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    deactivateMembershipFeeId(connection, id, rs.getString("id"));
                    membershipFees.add(
                            new MembershipFee(
                                    rs.getDate("eligible_from"),
                                    Frequency.valueOf(rs.getString("frequency")),
                                    rs.getDouble("amount"),
                                    rs.getString("label"),
                                    rs.getString("id"),
                                    Status.valueOf(rs.getString("status"))
                            ));
                } else {
                    throw new RuntimeException("MembershipFee not found");
                }
            }
            connection.setAutoCommit(true);
            try {
                return membershipFees;
            } catch (RuntimeException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int getNextMembershipFeeId(Connection connection) {
        String sql = "select coalesce(cast(substr(max(id), 5) as integer), 0) + 1 as id from membership_fee limit 1";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new RuntimeException("MembershipFee not found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deactivateMembershipFeeId(Connection connection, String id, String active_id) {
        String sql = "update membership_fee set status = 'INACTIVE' where collectivity_id = ? and id not ilike ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, active_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Collectivity> createCollectivity(List<CreateCollectivity> newCollectivity) {
        String sql = """
                insert into collectivity (id, number, name, location, speciality)
                values (?, ?, ?, ?, ?)
                returning id, number, name, location, speciality
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            List<Collectivity> collectivityList = new ArrayList<>();
            for (CreateCollectivity createCollectivity : newCollectivity) {
                ps.setString(1, "col-" + getNextCollectivityId(connection));
                ps.setInt(2, 1);
                ps.setString(3, "Aucun");
                ps.setString(4, createCollectivity.getLocation());
                ps.setString(5, createCollectivity.getStructure().getPresident().getProfession());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    collectivityList.add(new Collectivity(
                            rs.getString("id"),
                            rs.getInt("number"),
                            rs.getString("name"),
                            rs.getString("location"),
                            rs.getString("speciality"),
                            null,
                            createCollectivity.getStructure(),
                            null
                    ));
                } else {
                    throw new RuntimeException("Collectivity not found");
                }
            }
            try {
                connection.setAutoCommit(true);
                return collectivityList;
            } catch (RuntimeException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int getNextCollectivityId(Connection connection) {
        String sql = "select coalesce(cast(substr(max(id), 5) as integer), 0) + 1 as id from collectivity";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new RuntimeException("Collectivity not found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}