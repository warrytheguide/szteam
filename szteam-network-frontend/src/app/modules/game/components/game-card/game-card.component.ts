import {Component, Input} from '@angular/core';
import {GameResponse} from '../../../../services/models/game-response';

@Component({
  selector: 'app-game-card',
  imports: [],
  templateUrl: './game-card.component.html',
  standalone: true,
  styleUrl: './game-card.component.scss'
})
export class GameCardComponent {
  private _game: GameResponse = {};

  get game(): GameResponse {
    return this._game;
  }

  @Input()
  set game(value: GameResponse) {
    this._game = value;
  }

  private _gameCover: string | undefined;

  get gameCover(): string | undefined {
    if(this._game.cover) {
      return '';
    }
    return this._gameCover;
  }

}
