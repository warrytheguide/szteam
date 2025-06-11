package com.kristof.szteam.review;

import com.kristof.szteam.common.PageResponse;
import com.kristof.szteam.user.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("reviews")
@RequiredArgsConstructor
@Tag(name = "Review")
public class ReviewController {

    private final ReviewService service;

    @PostMapping
    public ResponseEntity<Integer> saveReview(
            @Valid @RequestBody ReviewRequest request,
            Authentication connectedUser
    ){
      return ResponseEntity.ok(service.save(request, connectedUser));
    }

    @GetMapping("/game/{game-id}")
    public ResponseEntity<PageResponse<ReviewResponse>> findAllReviewsByGame(
            @PathVariable("game-id") Integer gameId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        Integer userId = authentication != null ?
                ((User) authentication.getPrincipal()).getId() :
                null;

        return ResponseEntity.ok(
                service.findAllReviewsByGame(gameId, page, size, userId)
        );
    }

}
