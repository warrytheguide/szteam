import {Component, OnInit} from '@angular/core';
import {GameCardComponent} from '../../components/game-card/game-card.component';
import {DatePipe, NgForOf, NgIf} from '@angular/common';
import {PageResponseGameResponse} from '../../../../services/models/page-response-game-response';
import {GameService} from '../../../../services/services/game.service';
import {Router, RouterLink} from '@angular/router';
import {GameResponse} from '../../../../services/models/game-response';
import {ReviewService} from '../../../../services/services/review.service';
import {ReviewResponse} from '../../../../services/models/review-response';
import {FindAllReviewsByGame$Params} from '../../../../services/fn/review/find-all-reviews-by-game';
import {PageResponseReviewResponse} from '../../../../services/models/page-response-review-response';

@Component({
  selector: 'app-my-games',
  imports: [
    GameCardComponent,
    NgForOf,
    NgIf,
    RouterLink,
    DatePipe
  ],
  templateUrl: './my-games.component.html',
  standalone: true,
  styleUrl: './my-games.component.scss'
})
export class MyGamesComponent implements OnInit {
  gameResponse: PageResponseGameResponse = {};
  protected page: number = 0;
  private size: number = 6;
  selectedGame: GameResponse | null = null;
  reviews: ReviewResponse[] = [];
  currentReviewPage = 0;
  totalReviewPages = 0;


  constructor(
    private gameService: GameService,
    private router: Router,
    private reviewService: ReviewService,
  ) {
  }

  ngOnInit(): void {
    this.findAllGames()
  }

  private findAllGames() {
    this.gameService.findAllGamesByOwner({
      page: this.page,
      size: this.size
    }).subscribe({
      next:(games) => {
        this.gameResponse = games;
      }
    })
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllGames();
  }

  goToPreviousPage() {
    this.page--;
    this.findAllGames();
  }

  goToPage(page: number) {
    this.page = page;
    this.findAllGames();
  }

  goToNextPage() {
    this.page++;
    this.findAllGames();
  }

  goToLastPage() {
    this.page = this.gameResponse.totalPages as number - 1;
    this.findAllGames();
  }

  get isLastPage(): boolean{
    return this.page == this.gameResponse.totalPages as number - 1;
  }

  archiveGame(game: GameResponse) {
    this.gameService.updateArchivedStatus({
      'game-id' : game.id as number
    }).subscribe({
      next:() => {
        game.archived = !game.archived
      }
    })
  }

  shareGame(game: GameResponse) {
    this.gameService.updateSharableStatus({
      'game-id': game.id as number
    }).subscribe({
      next:() =>{
        game.shareable = !game.shareable;
      }
    })
  }

  editGame(game: GameResponse) {
    this.router.navigate(['games', 'manage', game.id]);
  }

  onShowDetails(game: GameResponse) {
    this.selectedGame = game;
    this.currentReviewPage = 0;
    this.loadReviews(game.id as number);
  }

  loadReviews(gameId: number): void {
    const params: FindAllReviewsByGame$Params = {
      'game-id': gameId,
      page: this.currentReviewPage,
      size: 10
    };
    this.reviewService.findAllReviewsByGame(params).subscribe({
      next: (response: PageResponseReviewResponse) => {
        this.reviews = response.content || [];
        this.totalReviewPages = response.totalPages || 0;
      },
      error: (err) => console.error('Error loading reviews:', err)
    });
  }
}
