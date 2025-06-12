package com.kristof.szteam.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("""
        SELECT review, user
        FROM Review review
        JOIN User user ON review.createdBy = user.id
        WHERE review.game.id = :gameId
""")
    Page<Object[]> findAllReviewsWithUserByGameId(Integer gameId, Pageable pageable);
}
