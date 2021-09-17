package com.casino.rankinteractive.repository;

import com.casino.rankinteractive.dto.TransactionDto;
import com.casino.rankinteractive.entity.Player;
import com.casino.rankinteractive.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author S'phokuhle on 9/16/2021
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    Transaction findFirstByPlayerOrderByIdDesc(Player player);

    Optional<Transaction> findFirstByIdAndPlayerOrderByIdDesc(Long transactionId, Player player);

    @Query(value = "Select new com.casino.rankinteractive.dto.TransactionDto(tr) From Transaction tr where tr.status is not null and tr.player.username = :username order by tr.id desc")
    List<TransactionDto> findTopTenByUsername(String username);
}
