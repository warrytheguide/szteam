package com.kristof.szteam.game;

import com.kristof.szteam.common.PageResponse;
import com.kristof.szteam.exception.OperationNotPermittedException;
import com.kristof.szteam.file.FileStorageService;
import com.kristof.szteam.history.GameTransactionHistory;
import com.kristof.szteam.history.GameTransactionHistoryRepository;
import com.kristof.szteam.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static com.kristof.szteam.game.GameSpecification.withOwnerId;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final GameTransactionHistoryRepository transactionHistoryRepository;
    private final FileStorageService fileStorageService;

    public Integer save(GameRequest request, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Game game = gameMapper.toGame(request);
        game.setOwner(user);
        return gameRepository.save(game).getId();
    }

    public GameResponse findById(Integer gameId) {
        return gameRepository.findById(gameId)
                .map(gameMapper::toGameResponse)
                .orElseThrow(() -> new EntityNotFoundException("Nincs ilyen ID konyv:: " + gameId));
    }

    public PageResponse<GameResponse> findAllGames(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Game> games = gameRepository.findAllDisplayableGames(pageable, user.getId());
        List<GameResponse> gameResponse = games.stream()
                .map(gameMapper::toGameResponse)
                .toList();
        return new PageResponse<>(
                gameResponse,
                games.getNumber(),
                games.getSize(),
                games.getTotalElements(),
                games.getTotalPages(),
                games.isFirst(),
                games.isLast()
        );
    }

    public PageResponse<GameResponse> findAllGamesByOwner(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Game> games = gameRepository.findAll(withOwnerId(user.getId()), pageable);

        List<GameResponse> gameResponse = games.stream()
                .map(gameMapper::toGameResponse)
                .toList();
        return new PageResponse<>(
                gameResponse,
                games.getNumber(),
                games.getSize(),
                games.getTotalElements(),
                games.getTotalPages(),
                games.isFirst(),
                games.isLast()
        );
    }

    public PageResponse<BorrowedGameResponse> findAllBorrowedGames(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<GameTransactionHistory> allBorrowedGames = transactionHistoryRepository.findAllBorrowedGames(pageable, user.getId());
        List<BorrowedGameResponse> gameResponse = allBorrowedGames.stream()
                .map(gameMapper::toBorrowedGameResponse)
                .toList();
        return new PageResponse<>(
                gameResponse,
                allBorrowedGames.getNumber(),
                allBorrowedGames.getSize(),
                allBorrowedGames.getTotalElements(),
                allBorrowedGames.getTotalPages(),
                allBorrowedGames.isFirst(),
                allBorrowedGames.isLast()
        );
    }

    public PageResponse<BorrowedGameResponse> findAllReturnedGames(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<GameTransactionHistory> allBorrowedGames = transactionHistoryRepository.findAllReturnedGames(pageable, user.getId());
        List<BorrowedGameResponse> gameResponse = allBorrowedGames.stream()
                .map(gameMapper::toBorrowedGameResponse)
                .toList();
        return new PageResponse<>(
                gameResponse,
                allBorrowedGames.getNumber(),
                allBorrowedGames.getSize(),
                allBorrowedGames.getTotalElements(),
                allBorrowedGames.getTotalPages(),
                allBorrowedGames.isFirst(),
                allBorrowedGames.isLast()
        );
    }

    public Integer updateSharableStatus(Integer gameId, Authentication connectedUser) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Nincs ilyen ID konyv:: " + gameId));
        User user = ((User) connectedUser.getPrincipal());
        if(!Objects.equals(game.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("Nem valtoztathatod meg a jatek oszthatosag statuszat, mert nem a tied");

        }
        game.setShareable(!game.isShareable());
        gameRepository.save(game);
        return gameId;
    }

    public Integer updateArchivedStatus(Integer gameId, Authentication connectedUser) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Nincs ilyen ID konyv:: " + gameId));
        User user = ((User) connectedUser.getPrincipal());
        if(!Objects.equals(game.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("Nem valtoztathatod meg a jatek archivalt statuszat, mert nem a tied");
        }
        game.setArchived(!game.isArchived());
        gameRepository.save(game);
        return gameId;
    }

    public Integer borrowGame(Integer gameId, Authentication connectedUser) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Nincs ilyen ID konyv:: " + gameId));
        if(game.isArchived() || !game.isShareable()){
            throw new OperationNotPermittedException("Ezt a jatekot nem tudod kolcsonozni mert nem megoszthato vagy archivalva van");
        }
        User user = ((User) connectedUser.getPrincipal());
        if(Objects.equals(game.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("A sajat jatekodat nem kolcsonozheted");
        }
        final boolean isAlreadyBorrowed = transactionHistoryRepository.isAlreadyBorrowedByUser(gameId, user.getId());
        if(isAlreadyBorrowed) {
            throw new OperationNotPermittedException("A jatek mar ki van kolcsonozve");
        }
        GameTransactionHistory gameTransactionHistory = GameTransactionHistory.builder()
                .user(user)
                .game(game)
                .returned(false)
                .returnApproved(false)
                .build();
        return transactionHistoryRepository.save(gameTransactionHistory).getId();
    }

    public Integer returnBorrowedGame(Integer gameId, Authentication connectedUser) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Nincs ilyen ID konyv:: " + gameId));
        if(game.isArchived() || !game.isShareable()){
            throw new OperationNotPermittedException("Ezt a jatekot nem tudod kolcsonozni mert nem megoszthato vagy archivalva van");
        }
        User user = ((User) connectedUser.getPrincipal());
        if(Objects.equals(game.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("A sajat jatekodat nem visszaadni");
        }
        GameTransactionHistory gameTransactionHistory = transactionHistoryRepository.findByGameIdAndUserId(gameId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("Nem te kolcsonozted ezt a jatekot"));
        gameTransactionHistory.setReturned(true);
        return transactionHistoryRepository.save(gameTransactionHistory).getId();
    }

    public Integer approveReturnBorrowedGame(Integer gameId, Authentication connectedUser) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Nincs ilyen ID konyv:: " + gameId));
        if(game.isArchived() || !game.isShareable()){
            throw new OperationNotPermittedException("Ezt a jatekot nem tudod kolcsonozni mert nem megoszthato vagy archivalva van");
        }
        User user = ((User) connectedUser.getPrincipal());
        if(Objects.equals(game.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("A sajat jatekodat nem visszaadni");
        }
        GameTransactionHistory gameTransactionHistory = transactionHistoryRepository.findByGameIdAndOwnerId(gameId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("A jatek nincs visszaadva meg, szoval nem tudsz beleegyezni"));
        gameTransactionHistory.setReturnApproved(true);
        return transactionHistoryRepository.save(gameTransactionHistory).getId();
    }

    public void uploadGameCoverPicture(MultipartFile file, Authentication connectedUser, Integer gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Nincs ilyen ID konyv:: " + gameId));
        User user = ((User) connectedUser.getPrincipal());
        var gameCover = fileStorageService.saveFile(file, user.getId());
        game.setGameCover(gameCover);
        gameRepository.save(game);
    }
}
