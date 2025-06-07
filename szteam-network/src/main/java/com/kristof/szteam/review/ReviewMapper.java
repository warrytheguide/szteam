package com.kristof.szteam.review;

import com.kristof.szteam.game.Game;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ReviewMapper {
    public Review toReview(ReviewRequest request) {
        return Review.builder()
                .score(request.score())
                .comment(request.comment())
                .game(Game.builder()
                        .id(request.gameId())
                        .archived(false)
                        .shareable(false)
                        .build()
                )
                .build();
    }

    public ReviewResponse toReviewResponse(Review review, Integer id) {
        return ReviewResponse.builder()
                .score(review.getScore())
                .comment(review.getComment())
                .ownReview(Objects.equals(review.getCreatedBy(), id))
                .build();
    }
}
