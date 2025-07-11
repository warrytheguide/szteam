import {Component, OnInit} from '@angular/core';
import {CommonModule, NgForOf, NgIf} from '@angular/common';
import {DEFAULT_IMAGE_BASE64} from '../../../../shared/default-image';
import {GameRequest} from '../../../../services/models/game-request';
import {FormsModule} from '@angular/forms';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {GameService} from '../../../../services/services/game.service';

@Component({
  selector: 'app-manage-game',
  imports: [
    NgIf,
    NgForOf,
    FormsModule,
    RouterLink,
    CommonModule,
  ],
  templateUrl: './manage-game.component.html',
  standalone: true,
  styleUrl: './manage-game.component.scss'
})
export class ManageGameComponent implements OnInit {

  gameRequest: GameRequest = {description: '', publisher: '', releaseDate: '', title: ''};
  errorMsg: Array<string> = [];
  protected readonly DEFAULT_IMAGE_BASE64 = DEFAULT_IMAGE_BASE64;
  selectedGameCover: any;
  selectedPicture: string | undefined;

  constructor(
    private gameService: GameService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    const gameId = this.activatedRoute.snapshot.params['gameId'];
    if(gameId){
      this.gameService.findGameById({
        'game-id': gameId
      }).subscribe({
        next: (game) => {
          this.gameRequest = {
            id: game.id,
            title: game.title as string,
            publisher: game.publisher as string,
            releaseDate: game.releaseDate as string,
            description: game.description as string,
            shareable: game.shareable
          }
          if(game.cover){
            this.selectedPicture = 'data:image/jpeg;base64,' + game.cover;
          }
        }
      })
    }
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
    if (!this.selectedGameCover && this.selectedPicture && this.selectedPicture !== this.DEFAULT_IMAGE_BASE64) {
      const base64Data = this.selectedPicture.split(',')[1];
      const byteCharacters = atob(base64Data);
      const byteArrays = [];

      for (let offset = 0; offset < byteCharacters.length; offset += 512) {
        const slice = byteCharacters.slice(offset, offset + 512);
        const byteNumbers = new Array(slice.length);

        for (let i = 0; i < slice.length; i++) {
          byteNumbers[i] = slice.charCodeAt(i);
        }

        const byteArray = new Uint8Array(byteNumbers);
        byteArrays.push(byteArray);
      }

      const blob = new Blob(byteArrays, {type: 'image/jpeg'});
      this.selectedGameCover = new File([blob], 'existing_cover.jpg', {type: 'image/jpeg'});
    }

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
            next: () => {
              this.router.navigate(['/games/my-games']);
            }
          });
        } else {
          this.router.navigate(['/games/my-games']);
        }
      },
      error: (err) => {
        this.errorMsg = err.error.validationErrors;
      }
    });
  }

  removeCover() {
    this.selectedPicture = undefined;
    this.selectedGameCover = undefined;
  }


}
