package com.kristof.szteam.review;

import com.kristof.szteam.game.Game;
import com.kristof.szteam.user.User;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ReviewMapper {
    public Review toReview(ReviewRequest request, User user) {
        return Review.builder()
                .score(request.score())
                .comment(request.comment())
                .game(Game.builder()
                        .id(request.gameId())
                        .archived(false)
                        .shareable(false)
                        .build()
                )
                .user(user)
                .build();
    }

    public ReviewResponse toReviewResponse(Review review, Integer userId) {
        return ReviewResponse.builder()
                .score(review.getScore())
                .comment(review.getComment())
                .author(review.getUser() != null ? review.getUser().getRealUsername() : null)
                .createdAt(review.getCreatedAt())
                .ownReview(
                        review.getCreatedBy() != null && review.getCreatedBy().equals(userId)
                )
                .build();
    }
}
