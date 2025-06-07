package com.kristof.szteam.game;

import com.kristof.szteam.common.BaseEntity;
import com.kristof.szteam.history.GameTransactionHistory;
import com.kristof.szteam.review.Review;
import com.kristof.szteam.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Game extends BaseEntity {

    private String title;
    private String publisher;
    private String description;
    private LocalDate releaseDate;
    private String gameCover;
    private boolean archived;
    private boolean shareable;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "game")
    private List<Review> reviews;

    @OneToMany(mappedBy = "game")
    private List<GameTransactionHistory> histories;

    @Transient
    public double getRate(){
        if(reviews == null || reviews.isEmpty()){
            return 0.0;
        }
        var rate = this.reviews.stream()
                .mapToDouble(Review::getScore)
                .average()
                .orElse(0.0);
        double roundedRate = Math.round(rate * 10.0) / 10.0;
        return roundedRate;
    }
}
