import { Component } from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {DEFAULT_IMAGE_BASE64} from '../../../../shared/default-image';
import {GameRequest} from '../../../../services/models/game-request';
import {FormsModule} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import {GameService} from '../../../../services/services/game.service';

@Component({
  selector: 'app-manage-game',
  imports: [
    NgIf,
    NgForOf,
    FormsModule,
    RouterLink
  ],
  templateUrl: './manage-game.component.html',
  standalone: true,
  styleUrl: './manage-game.component.scss'
})
export class ManageGameComponent {

  gameRequest: GameRequest = {description: '', publisher: '', releaseDate: '', title: ''};
  errorMsg: Array<string> = [];
  protected readonly DEFAULT_IMAGE_BASE64 = DEFAULT_IMAGE_BASE64;
  selectedGameCover: any;
  selectedPicture: string | undefined;

  constructor(
    private gameService: GameService,
    private router: Router
  ) {
  }

  onFileSelected(event: any) {
    this.selectedGameCover = event.target.files[0];
    console.log(this.selectedGameCover);
    if(this.selectedGameCover) {
      const reader = new FileReader();
      reader.onload = () =>{
        this.selectedPicture = reader.result as string;
      }
      reader.readAsDataURL(this.selectedGameCover);
    }
  }

  saveGame() {
    this.gameService.saveGame({
      body: this.gameRequest
    }).subscribe({
      next: (gameId) => {
        if (this.selectedGameCover) {
          this.gameService.uploadGameCoverPicture({
            'game-id': gameId,
            body: {
              file: this.selectedGameCover
            }
          }).subscribe({
            next:() => {
              this.router.navigate(['/games/my-games']);
            }
          });
        } else {
          // No file selected, just navigate away or display success
          this.router.navigate(['/games/my-games']);
        }
      },
      error: (err) => {
        this.errorMsg = err.error.validationErrors;
      }
    });
  }
}
