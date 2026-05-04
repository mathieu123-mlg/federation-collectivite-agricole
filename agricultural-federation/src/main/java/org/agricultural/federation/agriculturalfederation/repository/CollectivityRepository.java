package org.agricultural.federation.agriculturalfederation.repository;

import org.agricultural.federation.agriculturalfederation.entity.*;
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
    }/*

        public Optional<Collectivity> save(Collectivity collectivity) {
            String sql = """
                    INSERT INTO collectivity (number, name, location, speciality, created_at, federation_approval)
                    VALUES ((select coalesce(max(number) + 1, 1) from collectivity),
                            ?, ?, ?, ?, ?) RETURNING id;
                    """;
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, collectivity.getLocation());
                ps.setString(2, collectivity.getSpeciality());
                ps.setTimestamp(3, Timestamp.from(collectivity.getCreationDate()));
                ps.setBoolean(4, collectivity.isFederationApproval());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        collectivity.setId(rs.getInt("id"));
                        return Optional.of(collectivity);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to save collectivity", e);
            }
            return Optional.empty();
        }

        public void saveMemberCollectivity(Integer collectivityId, Integer memberId) {
            String sql = "INSERT INTO member_collectivity (member_id, collectivity_id, join_date) VALUES (?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, memberId);
                ps.setInt(2, collectivityId);
                ps.setTimestamp(3, Timestamp.from(Instant.now()));
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to link member to collectivity", e);
            }
        }

        public Integer saveMandate(Integer collectivityId, Instant startDate, Instant endDate) {
            String sql = "INSERT INTO mandate (collectivity_id, start_date, end_date) VALUES (?, ?, ?) RETURNING id";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, collectivityId);
                ps.setTimestamp(2, Timestamp.from(startDate));
                ps.setTimestamp(3, Timestamp.from(endDate));
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? rs.getInt("id") : null;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to save mandate", e);
            }
        }

        public void saveMemberRole(Integer memberId, Integer mandateId, String role) {
            String sql = "INSERT INTO member_role (member_id, mandate_id, role) VALUES (?, ?, ?::role_enum)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, memberId);
                ps.setInt(2, mandateId);
                ps.setString(3, role);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to save member role", e);
            }
        }

        public boolean existsByName(String name) {
            String sql = "SELECT 1 FROM collectivity WHERE name = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, name);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to check collectivity name", e);
            }
        }

        public boolean existsMemberById(Integer memberId) {
            String sql = "SELECT 1 FROM member WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, memberId);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to check member existence", e);
            }
        }

        public Optional<LocalDate> findMemberAdhesionDate(Integer memberId) {
            String sql = "SELECT adhesion_date FROM member WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, memberId);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? Optional.of(rs.getDate("adhesion_date").toLocalDate()) : Optional.empty();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to fetch member adhesion date", e);
            }
        }

        public Optional<Collectivity> findByIdWithDetails(Integer id) {
            String sql = """
                    SELECT id, name, location, speciality, created_at, federation_approval
                    FROM collectivity WHERE id = ?""";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Collectivity c = rowMapper.mapToCollectivity(rs);
                        c.setMembers(findMembersByCollectivityId(id));
                        c.setStructure(findStructureByCollectivityId(id));
                        return Optional.of(c);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to fetch collectivity", e);
            }
            return Optional.empty();
        }

        private List<Member> findMembersByCollectivityId(Integer collectivityId) {
            String sql = """
                        SELECT m.id, m.first_name, m.last_name, m.birth_date, m.gender::text,
                               m.address, m.profession, m.phone_number, m.email, m.adhesion_date
                        FROM member m JOIN member_collectivity mc ON m.id = mc.member_id
                        WHERE mc.collectivity_id = ? AND mc.leave_date IS NULL
                    """;
            List<Member> members = new ArrayList<>();
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, collectivityId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) members.add(rowMapper.mapToMember(rs));
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to fetch members", e);
            }
            return members;
        }

        private CollectivityStructure findStructureByCollectivityId(Integer collectivityId) {
            String sql = """
                        SELECT mr.role, m.id, m.first_name, m.last_name, m.birth_date, m.gender::text,
                               m.address, m.profession, m.phone_number, m.email, m.adhesion_date
                        FROM mandate ma JOIN member_role mr ON ma.id = mr.mandate_id
                        JOIN member m ON mr.member_id = m.id
                        WHERE ma.collectivity_id = ? AND ma.end_date >= CURRENT_DATE
                    """;
            CollectivityStructure collectivityStructure = new CollectivityStructure();
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, collectivityId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Member member = rowMapper.mapToMember(rs);
                        switch (rs.getString("role")) {
                            case "PRESIDENT" -> collectivityStructure.setPresident(member.getId());
                            case "VICE_PRESIDENT" -> collectivityStructure.setVicePresident(member.getId());
                            case "TREASURER" -> collectivityStructure.setTreasurer(member.getId());
                            case "SECRETARY" -> collectivityStructure.setSecretary(member.getId());
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to fetch structure", e);
            }
            return collectivityStructure;
        }

        public Optional<Collectivity> findCollectivityById(Integer id) {
            String sql = """
                        SELECT id, number, name, location, speciality, created_at, federation_approval
                        FROM collectivity
                        WHERE id = ?
                    """;

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Collectivity collectivity = rowMapper.mapToCollectivity(rs);
                        return Optional.of(collectivity);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to fetch collectivity by id", e);
            }

            return Optional.empty();
        }

        public void assignCollectivityIdentifier(Integer collectivityId, Integer number, String name) {
            String sql = """
                    update collectivity set number = ?, name = ?, updated_at = current_timestamp
                    where id = ? and updated_at is null""";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, number);
                ps.setString(2, name);
                ps.setInt(3, collectivityId);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public boolean existsById(Integer id) {
            String sql = "SELECT 1 FROM membership_fee WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public List<CollectivityTransaction> getTransaction(Integer id, Instant from, Instant to) {
            String sql = """
                        SELECT p.id, p.payment_date, p.amount, p.payment_method,
                               m.id AS member_id, m.first_name, m.last_name
                        FROM payment p
                        JOIN member m ON m.id = p.member_id
                        JOIN member_collectivity mc ON mc.member_id = m.id
                        WHERE mc.collectivity_id = ?
                          AND p.payment_date BETWEEN ? AND ?
                          AND (mc.leave_date IS NULL OR mc.leave_date >= p.payment_date)
                        ORDER BY p.payment_date DESC
                    """;

            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1, id);
                ps.setTimestamp(2, Timestamp.from(from));
                ps.setTimestamp(3, Timestamp.from(to));

                ResultSet rs = ps.executeQuery();
                List<CollectivityTransaction> transactions = new ArrayList<>();
                while (rs.next()) {
                    CollectivityTransaction tx = new CollectivityTransaction();

                    tx.setId(rs.getInt("id"));
                    tx.setCreationDate(rs.getTimestamp("payment_date").toInstant());
                    tx.setAmount(rs.getDouble("amount"));

                    tx.setPaymentMode(rowMapper.mapPaymentMode(rs.getString("payment_method")));

                    Member m = new Member();
                    m.setId(rs.getInt("member_id"));
                    m.setFirstName(rs.getString("first_name"));
                    m.setLastName(rs.getString("last_name"));

                    tx.setMemberDebited(m);

                    // ⚠️ AccountCredited (pas dans schema payment)
                    // fallback simple (sinon table account relation manquante)
                    tx.setAccountCredited(null);

                    transactions.add(tx);
                }
                return transactions;
            } catch (SQLException e) {
                throw new RuntimeException("Failed to fetch collectivity transactions", e);
            }
        }

    */

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
                members.add(rowMapper.mapToMember(rs));
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

    public List<Member> getCollectivityMembersById(String id) {
        String sql = """
                select mc.id, mc.collectivity_id, mc.member_id, mc.occupation,
                       m.first_name, m.last_name, m.birthdate, m.gender, m.address, m.profession, m.phone_number, m.email
                from member_collectivity mc
                join member m on mc.member_id = m.id
                where collectivity_id = ?;
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            List<Member> members = new ArrayList<>();
            while (rs.next()) {
                members.add(rowMapper.mapToMember(rs));
            }
            return members;
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
                ps.setString(1, null);
                ps.setString(2, createMembershipFee.getLabel());
                ps.setString(3, createMembershipFee.getFrequency());
                ps.setDate(4, createMembershipFee.getEligibleFrom());
                ps.setDouble(5, createMembershipFee.getAmount());
                ps.setString(6, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    deactivateMembershipFeeId(connection, id);
                    membershipFees.add(
                            new MembershipFee(
                                    rs.getDate("eligible_from"),
                                    Frequency.valueOf(rs.getString("frequency")),
                                    rs.getDouble("amount"),
                                    rs.getString("label"),
                                    rs.getString("id"),
                                    Status.valueOf(rs.getString("status"))
                            ));
                }
                throw new RuntimeException("MembershipFee not found");
            }
            ps.executeBatch();
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

    private void deactivateMembershipFeeId(Connection connection, String id) {
        String sql = "update membership_fee set status = 'INACTIVE' where collectivity_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
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
            int index = getNextCollectivityId(connection);
            List<Collectivity> collectivityList = new ArrayList<>();
            for (CreateCollectivity createCollectivity : newCollectivity) {
                ps.setString(1, "col-" + index++);
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
                }
                throw new RuntimeException("Collectivity not found");
            }
            try {
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