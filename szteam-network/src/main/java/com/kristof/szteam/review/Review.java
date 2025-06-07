package com.kristof.szteam.review;


import com.kristof.szteam.common.BaseEntity;
import com.kristof.szteam.game.Game;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Review extends BaseEntity {

    private Double score;
    private String comment;


    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;
}
