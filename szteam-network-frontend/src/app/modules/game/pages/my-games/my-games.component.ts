import {Component, OnInit} from '@angular/core';
import {GameCardComponent} from '../../components/game-card/game-card.component';
import {NgForOf, NgIf} from '@angular/common';
import {PageResponseGameResponse} from '../../../../services/models/page-response-game-response';
import {GameService} from '../../../../services/services/game.service';
import {Router, RouterLink} from '@angular/router';
import {GameResponse} from '../../../../services/models/game-response';

@Component({
  selector: 'app-my-games',
  imports: [
    GameCardComponent,
    NgForOf,
    NgIf,
    RouterLink
  ],
  templateUrl: './my-games.component.html',
  standalone: true,
  styleUrl: './my-games.component.scss'
})
export class MyGamesComponent implements OnInit {
  gameResponse: PageResponseGameResponse = {};
  protected page: number = 0;
  private size: number = 5;


  constructor(
    private gameService: GameService,
    private router: Router
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

  archiveGame($event: GameResponse) {

  }

  shareGame($event: GameResponse) {

  }

  editGame($event: GameResponse) {

  }
}
