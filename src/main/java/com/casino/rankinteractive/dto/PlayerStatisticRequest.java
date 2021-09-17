package com.casino.rankinteractive.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author S'phokuhle on 9/16/2021
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlayerStatisticRequest {
    private String username;
    private String password;
}
