package com.kristof.szteam.game;

import com.kristof.szteam.file.FileUtils;
import com.kristof.szteam.history.GameTransactionHistory;
import org.springframework.stereotype.Service;

@Service
public class GameMapper {

    public Game toGame(GameRequest request) {
        return Game.builder()
                .id(request.id())
                .title(request.title())
                .publisher(request.publisher())
                .description(request.description())
                .releaseDate(request.releaseDate())
                .archived(false)
                .shareable(request.shareable())
                .build();
    }

    public GameResponse toGameResponse(Game game) {
        return GameResponse.builder()
                .id(game.getId())
                .title(game.getTitle())
                .publisher(game.getPublisher())
                .description(game.getDescription())
                .releaseDate(game.getReleaseDate())
                .rate(game.getRate())
                .archived(game.isArchived())
                .shareable(game.isShareable())
                .owner(game.getOwner().getRealUsername())
                .cover(FileUtils.readFileFromLocation(game.getGameCover()))
                .build();
    }

    public BorrowedGameResponse toBorrowedGameResponse(GameTransactionHistory history) {
        return BorrowedGameResponse.builder()
                .id(history.getGame().getId())
                .title(history.getGame().getTitle())
                .publisher(history.getGame().getPublisher())
                .description(history.getGame().getDescription())
                .releaseDate(history.getGame().getReleaseDate())
                .rate(history.getGame().getRate())
                .returned(history.isReturned())
                .returnApproved(history.isReturnApproved())
                .build();
    }
}
