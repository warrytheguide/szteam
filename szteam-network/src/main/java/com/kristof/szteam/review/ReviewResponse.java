package com.kristof.szteam.review;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {

    private Double score;
    private String comment;
    private boolean ownReview;
}
