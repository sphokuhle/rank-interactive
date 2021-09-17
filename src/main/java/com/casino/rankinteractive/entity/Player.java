package com.casino.rankinteractive.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author S'phokuhle on 9/15/2021
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "PLAYER")
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, insertable = false)
    private Long id;

    @NotNull(message = "Username cannot be null")
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    @OrderBy("id DESC")
    private List<Transaction> transactions;

}
