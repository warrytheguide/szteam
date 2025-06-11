package com.kristof.szteam.review;

import com.kristof.szteam.common.PageResponse;
import com.kristof.szteam.exception.OperationNotPermittedException;
import com.kristof.szteam.game.Game;
import com.kristof.szteam.game.GameRepository;
import com.kristof.szteam.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final GameRepository gameRepository;
    private final ReviewMapper reviewMapper;
    private final ReviewRepository reviewRepository;

    public Integer save(ReviewRequest request, Authentication connectedUser) {
        Game game = gameRepository.findById(request.gameId())
                .orElseThrow(() -> new EntityNotFoundException("Nincs ilyen ID konyv:: " + request.gameId()));
        if(game.isArchived() || !game.isShareable()){
            throw new OperationNotPermittedException("Ezt a jatekot nem tudod ertekelni, mert archivalt vagy nem megoszthato");
        }
        User user = ((User) connectedUser.getPrincipal());
        if(Objects.equals(game.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("A sajat jatekod nem tudod ertekelni");
        }
        Review review = reviewMapper.toReview(request, user);
        return reviewRepository.save(review).getId();
    }

    public PageResponse<ReviewResponse> findAllReviewsByGame(Integer gameId, int page, int size, Integer userId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepository.findAllByGameId(gameId, pageable);

        List<ReviewResponse> reviewResponses = reviews.stream()
                .map(review -> reviewMapper.toReviewResponse(review, userId))
                .toList();

        return new PageResponse<>(
                reviewResponses,
                reviews.getNumber(),
                reviews.getSize(),
                reviews.getTotalElements(),
                reviews.getTotalPages(),
                reviews.isFirst(),
                reviews.isLast()
        );
    }
}
