package edu.bits.txn;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface TransactionRepository extends JpaRepository<TransactionRecord, Long> {
    List<TransactionRecord> findByAccountNumberOrderByCreatedAtDesc(String accountNumber);

    @Query("select coalesce(sum(t.amount), 0) from TransactionRecord t where t.txnType='DEBIT' and t.accountNumber = :accountNumber and t.createdAt >= :from and t.createdAt < :to")
    BigDecimal dailyDebitSum(String accountNumber, OffsetDateTime from, OffsetDateTime to);
}

interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey, String> {
    Optional<IdempotencyKey> findByKeyValue(String keyValue);
}
