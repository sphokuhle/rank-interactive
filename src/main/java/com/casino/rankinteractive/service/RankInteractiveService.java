package com.casino.rankinteractive.service;

import com.casino.rankinteractive.dto.PlayerDto;
import com.casino.rankinteractive.dto.PlayerStatisticRequest;
import com.casino.rankinteractive.dto.TransactionDto;
import com.casino.rankinteractive.entity.Player;
import com.casino.rankinteractive.entity.Transaction;
import com.casino.rankinteractive.repository.PlayerRepository;
import com.casino.rankinteractive.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author S'phokuhle on 9/15/2021
 */
@Service
@Slf4j
public class RankInteractiveService {
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Value("${app.security.secret}")
    private String secret;

    //Getting the current balance of a player.
    public TransactionDto getCurrentPlayerBalance(Long playerId) {
        Player player = playerRepository.getOne(playerId);
        if(player != null) {
            log.info("Player found: {}", player.getUsername());
            return new TransactionDto(transactionRepository.findFirstByPlayerOrderByIdDesc(player));
        }
        throw new IllegalArgumentException(String.format("playerId %d was not found", playerId));
    }

    /**
     * Takes the username and password from playerStatisticRequest object and validate the password before returning a list of user's transactions
     * @param playerStatisticRequest
     * @return
     */
    public List<TransactionDto> getLastTenPlayerStatistics(PlayerStatisticRequest playerStatisticRequest) {
        if(playerStatisticRequest == null || playerStatisticRequest.getUsername() == null
                || playerStatisticRequest.getUsername().trim().isEmpty() || playerStatisticRequest.getPassword() == null
                || playerStatisticRequest.getPassword().trim().isEmpty()){
            throw new IllegalArgumentException("Invalid value(s) provided");
        }

        if(!playerStatisticRequest.getPassword().equalsIgnoreCase(secret)) {
            throw new IllegalArgumentException("Unauthorized");
        }

        List<TransactionDto> transactionDtoList = transactionRepository.findTopTenByUsername(playerStatisticRequest.getUsername());
        int size = transactionDtoList.size();
        log.info("Initial list size for user {} is {}", new Object[]{playerStatisticRequest.getUsername(), size});
        if(size > 0) {
            if(size > 10) {
                return transactionDtoList.subList(0, 10);
            }
            return transactionDtoList.subList(0, size);
        }
        return Collections.emptyList();

    }

    /**
     * Deduct the balance provided from the request body, from an existing transaction
     * @param transactionDto
     * @param playerId
     * @return
     */
    public TransactionDto deduct(TransactionDto transactionDto, Long playerId) {
        if(transactionDto.getId() != null || playerId != null) {
            log.info("Transaction ID: {}, Player ID: {}", new Object[]{transactionDto.getId(), playerId});
            Optional<Player> player = playerRepository.findById(playerId);
            if(player.isPresent()) {
                log.info("PLayer Found: {}",player.get().getUsername());
                Optional<Transaction> optionalTransaction = transactionRepository.findFirstByIdAndPlayerOrderByIdDesc(transactionDto.getId(), player.get());
                if (optionalTransaction.isPresent()) {
                    Transaction transaction = optionalTransaction.get();
                    log.info("Transaction Found: {}",transaction.getId());
                    if (transaction.getBalance() != null && transactionDto.getBalance() != null) {
                        transaction.setBalance((transaction.getBalance() - transactionDto.getBalance()) <= 0 ? 0.00: (transaction.getBalance() - transactionDto.getBalance()));
                        transaction.setStatus("wager");
                    }
                    transactionRepository.save(transaction);
                    return new TransactionDto(transaction);
                } else {
                    throw new IllegalArgumentException(String.format("transactionId: %d does not exist", transactionDto.getId()));
                }
            } else {
                throw new IllegalArgumentException(String.format("playerId %d does not exist", playerId));
            }
        }
        throw new IllegalArgumentException("playerId and/or transactionId is not provided");
    }

    /**
     * adding balance if on an existing transaction if it exist for a player or add a new transaction if the transaction id was not found
     * @param transactionDto
     * @param playerId
     * @return
     */
    public TransactionDto deposit(TransactionDto transactionDto, Long playerId) {
        if(playerId != null) {
            log.info("Transaction ID: {}, Player ID: {}", new Object[]{transactionDto.getId(), playerId});
            Player player = playerRepository.getOne(playerId);
            if(player != null) {
                log.info("Player Found: {}", player.getUsername());
                Optional<Transaction> optionalTransaction = transactionRepository.findFirstByIdAndPlayerOrderByIdDesc(transactionDto.getId(), player);
                if (optionalTransaction.isPresent()) { //If a transaction is found, then it is updated to the incoming request body
                    Transaction transaction = optionalTransaction.get();
                    log.info("Transaction Found: {}",transaction.getId());
                    if (transactionDto.getBalance() != null && transactionDto.getBalance() > 0) {
                        transaction.setBalance((transaction.getBalance() != null)? (transaction.getBalance() + transactionDto.getBalance()) : transactionDto.getBalance());
                        transaction.setStatus("win");
                    }
                    transactionRepository.save(transaction);
                    return new TransactionDto(transaction);
                } else { //If there is no existing transaction for this player, then a new transaction will be created.
                    Transaction newTransaction = new Transaction();
                    newTransaction.setPlayer(player);
                    if(transactionDto.getBalance() != null && transactionDto.getBalance() > 0) {
                        newTransaction.setStatus("win");
                        newTransaction.setBalance(transactionDto.getBalance());
                    }
                    transactionRepository.save(newTransaction);
                    return new TransactionDto(newTransaction);
                }
            } else {
                throw new IllegalArgumentException(String.format("playerId %d was not found", playerId));
            }
        }
        log.info("No data found");
        return null;
    }

    public void addPlayer(PlayerDto playerDto) {
        Player player = new Player();
        player.setUsername(playerDto.getUsername());
        playerRepository.save(player);
    }
}
