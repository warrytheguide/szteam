import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {PageResponseBorrowedGameResponse} from '../../../../services/models/page-response-borrowed-game-response';
import {ReviewRequest} from '../../../../services/models/review-request';
import {BorrowedGameResponse} from '../../../../services/models/borrowed-game-response';
import {GameService} from '../../../../services/services/game.service';
import {ReviewService} from '../../../../services/services/review.service';
import {approveReturnBorrowGame} from '../../../../services/fn/game/approve-return-borrow-game';

@Component({
  selector: 'app-returned-games',
  imports: [
    NgForOf,
    NgIf
  ],
  templateUrl: './returned-games.component.html',
  standalone: true,
  styleUrl: './returned-games.component.scss'
})
export class ReturnedGamesComponent implements OnInit {
  returnedGames: PageResponseBorrowedGameResponse = {};
  page = 0;
  size = 15;
  message = '';
  level = 'success';

  constructor(
    private gameService: GameService,
  ) {
  }
  ngOnInit(): void {
    this.findAllReturnedGames();
  }


  private findAllReturnedGames() {
    this.gameService.findAllReturnedGames({
      page: this.page,
      size: this.size
    }).subscribe({
        next: (resp) =>{
          this.returnedGames = resp;
        }
      }
    )
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllReturnedGames();
  }

  goToPreviousPage() {
    this.page--;
    this.findAllReturnedGames();
  }

  goToPage(page: number) {
    this.page = page;
    this.findAllReturnedGames();
  }

  goToNextPage() {
    this.page++;
    this.findAllReturnedGames();
  }

  goToLastPage() {
    this.page = this.returnedGames.totalPages as number - 1;
    this.findAllReturnedGames();
  }

  get isLastPage(): boolean{
    return this.page == this.returnedGames.totalPages as number - 1;
  }


  approveGameReturn(game: BorrowedGameResponse) {
    if(!game.returned){
      this.level = 'error';
      this.message = 'A játék még nem lett visszajuttatva!'
      return;
    }
    this.gameService.approveReturnBorrowGame({
      'game-id': game.id as number
    }).subscribe({
      next: () => {
        this.level = 'success';
        this.message = 'Visszajuttatás jóváhagyva!';
        this.findAllReturnedGames();
      }
    })
  }
}
