import {Component, OnInit} from '@angular/core';
import {BorrowedGameResponse} from '../../../../services/models/borrowed-game-response';
import {PageResponseBorrowedGameResponse} from '../../../../services/models/page-response-borrowed-game-response';
import {NgForOf, NgIf} from '@angular/common';
import {GameService} from '../../../../services/services/game.service';
import {ReviewRequest} from '../../../../services/models/review-request';
import {FormsModule} from '@angular/forms';
import {RatingComponent} from '../../components/rating/rating.component';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-borrowed-game-list',
  imports: [
    NgIf,
    NgForOf,
    FormsModule,
    RatingComponent,
    RouterLink
  ],
  templateUrl: './borrowed-game-list.component.html',
  standalone: true,
  styleUrl: './borrowed-game-list.component.scss'
})
export class BorrowedGameListComponent implements OnInit {

  borrowedGames: PageResponseBorrowedGameResponse = {};
  reviewRequest: ReviewRequest = {comment: "", gameId: 0, score: 0};
  page = 0;
  size = 5;
  selectedGame: BorrowedGameResponse | undefined = undefined;

  constructor(
    private gameService: GameService
  ) {
  }
  ngOnInit(): void {
      this.findAllBorrowedGames();
  }

  returnBorrowGame(game: BorrowedGameResponse) {
    this.selectedGame = game;

  }

  private findAllBorrowedGames() {
    this.gameService.findAllBorrowedGames({
      page: this.page,
      size: this.size
    }).subscribe({
        next: (resp) =>{
          this.borrowedGames = resp;
        }
      }
    )
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllBorrowedGames();
  }

  goToPreviousPage() {
    this.page--;
    this.findAllBorrowedGames();
  }

  goToPage(page: number) {
    this.page = page;
    this.findAllBorrowedGames();
  }

  goToNextPage() {
    this.page++;
    this.findAllBorrowedGames();
  }

  goToLastPage() {
    this.page = this.borrowedGames.totalPages as number - 1;
    this.findAllBorrowedGames();
  }

  get isLastPage(): boolean{
    return this.page == this.borrowedGames.totalPages as number - 1;
  }

  returnGame(withReview: boolean) {

  }
}
