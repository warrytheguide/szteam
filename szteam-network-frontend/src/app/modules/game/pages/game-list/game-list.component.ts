import {Component, OnInit} from '@angular/core';
import {GameService} from '../../../../services/services/game.service';
import {Router} from '@angular/router';
import {PageResponseGameResponse} from '../../../../services/models/page-response-game-response';
import {NgForOf} from '@angular/common';
import {GameCardComponent} from '../../components/game-card/game-card.component';

@Component({
  selector: 'app-game-list',
  imports: [
    NgForOf,
    GameCardComponent
  ],
  templateUrl: './game-list.component.html',
  standalone: true,
  styleUrl: './game-list.component.scss'
})
export class GameListComponent implements OnInit {
  gameResponse: PageResponseGameResponse = {};
  private page: number = 0;
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
    this.gameService.findAllGames({
      page: this.page,
      size: this.size
    }).subscribe({
      next:(games) => {
        this.gameResponse = games;
      }
    })
  }
}
