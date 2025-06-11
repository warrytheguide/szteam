import {Component, EventEmitter, Input, Output} from '@angular/core';
import {GameResponse} from '../../../../services/models/game-response';
import {NgIf} from '@angular/common';
import {RatingComponent} from '../rating/rating.component';
import {DEFAULT_IMAGE_BASE64} from '../../../../shared/default-image';

@Component({
  selector: 'app-game-card',
  imports: [
    NgIf,
    RatingComponent
  ],
  templateUrl: './game-card.component.html',
  standalone: true,
  styleUrl: './game-card.component.scss'
})
export class GameCardComponent {
  private _game: GameResponse = {};
  private _gameCover: string | undefined;
  private _manage:boolean = false;



  get game(): GameResponse {
    return this._game;
  }

  @Input()
  set game(value: GameResponse) {
    this._game = value;
  }



  get manage(): boolean {
    return this._manage;
  }

  @Input()
  set manage(value: boolean) {
    this._manage = value;
  }


  get gameCover(): string | undefined {
    if(this._game.cover) {
      return 'data:image/jpeg;base64,' + this._game.cover;
    }
    return DEFAULT_IMAGE_BASE64;
  }

  @Output() private share: EventEmitter<GameResponse> = new EventEmitter<GameResponse>();
  @Output() private archive: EventEmitter<GameResponse> = new EventEmitter<GameResponse>();
  @Output() private borrow: EventEmitter<GameResponse> = new EventEmitter<GameResponse>();
  @Output() private edit: EventEmitter<GameResponse> = new EventEmitter<GameResponse>();
  @Output() private details: EventEmitter<GameResponse> = new EventEmitter<GameResponse>();

  onShowDetails() {
    this.details.emit(this._game);
  }

  onBorrow() {
    this.borrow.emit(this._game);
  }

  onEdit() {
    this.edit.emit(this._game);
  }

  onShare() {
    this.share.emit(this._game);
  }

  onArchive() {
    this.archive.emit(this._game);
  }
}
