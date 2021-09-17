package com.casino.rankinteractive.controller;

import com.casino.rankinteractive.dto.PlayerDto;
import com.casino.rankinteractive.dto.PlayerStatisticRequest;
import com.casino.rankinteractive.dto.TransactionDto;
import com.casino.rankinteractive.service.RankInteractiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author S'phokuhle on 9/15/2021
 */
@RestController
@RequestMapping("/v1/player")
@Slf4j
public class RankInteractiveController {

    @Autowired
    private RankInteractiveService service;

    @GetMapping(path = "/heath/test")
    public ResponseEntity<String> healthTest() {
        return ResponseEntity.ok().body(RankInteractiveController.class.getCanonicalName());
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDto> getPlayerCurrentBalance(@NotNull @PathVariable("id") Long id) {
        return ResponseEntity.ok().body(service.getCurrentPlayerBalance(id));
    }

    @PostMapping(value = "/{playerId}/win", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDto> deposit(@Valid @RequestBody TransactionDto transactionDto, @PathVariable("playerId") Long playerId) {
        return ResponseEntity.ok().body(service.deposit(transactionDto, playerId));
    }

    @PostMapping(value = "/{playerId}/wager", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDto> deduct(@Valid @NotNull @RequestBody TransactionDto transactionDto, @PathVariable("playerId") Long playerId) {
        return ResponseEntity.ok().body(service.deduct(transactionDto, playerId));
    }

    @GetMapping(path = "/statistics", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TransactionDto>> getPlayerStatistics(@Valid @NotNull @RequestBody PlayerStatisticRequest playerStatisticRequest) {
        return ResponseEntity.ok().body(service.getLastTenPlayerStatistics(playerStatisticRequest));
    }

    //For testing purposes
    @PutMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addPlayer(@Valid @RequestBody PlayerDto playerDto) {
        service.addPlayer(playerDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
