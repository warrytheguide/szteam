package com.kristof.szteam.review;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {

    private Double score;
    private String comment;
    private boolean ownReview;
    private String author;
    private LocalDateTime createdAt;
}
