package com.kristof.szteam.game;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record GameRequest(
        Integer id,

        @NotNull(message = "Cím megadása kötelező!")
        @Pattern(regexp = ".*\\S.*", message = "Cím nem lehet üres!")
        String title,

        @NotNull(message = "Kiadó megadása kötelező!")
        @Pattern(regexp = ".*\\S.*", message = "Kiadó nem lehet üres!")
        String publisher,

        @NotNull(message = "Leírás megadása kötelező!")
        @Pattern(regexp = ".*\\S.*", message = "Leírás nem lehet üres!")
        String description,

        @NotNull(message = "Dátum megadása kötelező!")
        LocalDate releaseDate,

        boolean shareable
) {}

