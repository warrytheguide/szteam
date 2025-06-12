package com.kristof.szteam.review;

import com.kristof.szteam.game.Game;
import com.kristof.szteam.user.User;
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

    public ReviewResponse toReviewResponse(Object[] result, Integer userId) {
        Review review = (Review) result[0];
        User user = (User) result[1];

        return ReviewResponse.builder()
                .score(review.getScore())
                .comment(review.getComment())
                .author(user.getRealUsername())
                .createdAt(review.getCreatedAt())
                .ownReview(review.getCreatedBy() != null && review.getCreatedBy().equals(userId))
                .build();
    }
}
