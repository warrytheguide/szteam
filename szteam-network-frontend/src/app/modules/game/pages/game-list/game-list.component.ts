import {Component, OnInit} from '@angular/core';
import {GameService} from '../../../../services/services/game.service';
import {Router} from '@angular/router';
import {PageResponseGameResponse} from '../../../../services/models/page-response-game-response';
import {DatePipe, NgForOf, NgIf} from '@angular/common';
import {GameCardComponent} from '../../components/game-card/game-card.component';
import {GameResponse} from '../../../../services/models/game-response';
import {ReviewService} from '../../../../services/services/review.service';
import {ReviewResponse} from '../../../../services/models/review-response';
import {PageResponseReviewResponse} from '../../../../services/models/page-response-review-response';
import {FindAllReviewsByGame$Params} from '../../../../services/fn/review/find-all-reviews-by-game';

@Component({
  selector: 'app-game-list',
  imports: [
    NgForOf,
    GameCardComponent,
    NgIf,
    DatePipe
  ],
  templateUrl: './game-list.component.html',
  standalone: true,
  styleUrl: './game-list.component.scss'
})
export class GameListComponent implements OnInit {
  gameResponse: PageResponseGameResponse = {};
  protected page: number = 0;
  private size: number = 6;
  message: string = '';
  level: string = 'success';
  selectedGame: GameResponse | null = null;
  reviews: ReviewResponse[] = [];
  currentPage = 0;
  totalPages = 0;

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
    this.gameService.findAllGames({
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

  borrowGame(game: GameResponse) {
    this.message = '';
    this.gameService.borrowGame({
      'game-id': game.id as number,
    }).subscribe({
      next:() =>{
        this.level = 'success';
        this.message = 'A játék sikeresen kikölcsönözve!';
      },
      error: (err) => {
        console.log(err);
        this.level = 'error';
        this.message = err.error.error;
      }
    })

  }

  loadReviews(gameId: number): void {
    const params: FindAllReviewsByGame$Params = {
      'game-id': gameId,
      page: this.currentPage,
      size: 10
    };

    this.reviewService.findAllReviewsByGame(params).subscribe({
      next: (response: PageResponseReviewResponse) => {
        this.reviews = response.content || [];
        this.totalPages = response.totalPages || 0;
      },
      error: (err) => console.error('Error loading reviews:', err)
    });
  }

  onShowDetails(game: GameResponse) {
    this.selectedGame = game;
    this.currentPage = 0;
    this.loadReviews(game.id as number);
  }
}

