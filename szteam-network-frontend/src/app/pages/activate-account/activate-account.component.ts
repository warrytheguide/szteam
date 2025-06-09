import { Component } from '@angular/core';
import {AuthenticationService} from '../../services/services/authentication.service';
import {Router} from '@angular/router';
import {NgIf} from '@angular/common';
import {CodeInputModule} from 'angular-code-input';

@Component({
  selector: 'app-activate-account',
  imports: [
    NgIf,
    CodeInputModule
  ],
  templateUrl: './activate-account.component.html',
  standalone: true,
  styleUrl: './activate-account.component.scss'
})
export class ActivateAccountComponent {

  message: string = '';
  isOkay: boolean = true;
  submitted: boolean = false;

  constructor(
    private router: Router,
    private authService: AuthenticationService
  ) {}

  onCodeCompleted(token: string) {
    this.confirmAccount(token);
  }

  redirectToLogin() {
    this.router.navigate(['login']);
  }

  private confirmAccount(token: string) {
    this.authService.confirm({
      token
    }).subscribe({
      next:() => {
        this.message = 'A fiók sikeresen aktiválva lett.\nMostmár be tud jelentkezni a fiókjába.';
        this.submitted = true;
        this.isOkay = true;
        },
      error:() => {
        this.message = 'A kód hibás, vagy lejárt.'
        this.submitted = true;
        this.isOkay = false;
      }
    });
  }
}
