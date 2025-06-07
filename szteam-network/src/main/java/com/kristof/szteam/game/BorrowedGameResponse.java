package com.kristof.szteam.game;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BorrowedGameResponse {

    private Integer id;
    private String title;
    private String description;
    private String publisher;
    private LocalDate releaseDate;
    private double rate;
    private boolean returned;
    private boolean returnApproved;
}
