package com.kristof.szteam.game;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameResponse {

    private Integer id;
    private String title;
    private String description;
    private String publisher;
    private LocalDate releaseDate;
    private String owner;
    private byte[] cover;
    private double rate;
    private boolean archived;
    private boolean shareable;
}
