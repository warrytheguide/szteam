import { Component } from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {DEFAULT_IMAGE_BASE64} from '../../../../shared/default-image';
import {GameRequest} from '../../../../services/models/game-request';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-manage-game',
  imports: [
    NgIf,
    NgForOf,
    FormsModule
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


  onFileSelected(event: any) {
    this.selectedGameCover = event.target.files[0];
    console.log(this.selectedGameCover);
    if(this.selectedGameCover) {
      const reader = new FileReader();
      reader.onload = () =>{
        this.selectedGameCover = reader.result as string;
      }
      reader.readAsDataURL(this.selectedGameCover);
    }
  }
}
