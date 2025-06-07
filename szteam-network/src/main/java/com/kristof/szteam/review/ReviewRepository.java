package com.kristof.szteam.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("""
        SELECT review
        FROM Review review
        WHERE review.game.id = :gameId
""")
    Page<Review> findAllByGameId(Integer gameId, Pageable pageable);
}
