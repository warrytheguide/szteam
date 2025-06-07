package com.kristof.szteam.game;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record GameRequest(
        Integer id,
        @NotNull(message = "100")
        @NotEmpty(message = "100")
        String title,
        @NotNull(message = "101")
        @NotEmpty(message = "101")
        String publisher,
        @NotNull(message = "102")
        @NotEmpty(message = "102")
        String description,
        @NotNull(message = "103")
        LocalDate releaseDate,
        boolean shareable
) {
}
