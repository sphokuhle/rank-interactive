package com.casino.rankinteractive.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayerDto {
    private Long id;
    @NotNull(message = "Username cannot be null")
    private String username;
}
