package com.casino.rankinteractive.repository;

import com.casino.rankinteractive.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author S'phokuhle on 9/16/2021
 */
public interface PlayerRepository extends JpaRepository<Player, Long>, JpaSpecificationExecutor<Player> {

}
