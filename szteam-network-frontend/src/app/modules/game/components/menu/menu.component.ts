import {Component, OnInit} from '@angular/core';
import {RouterLink} from '@angular/router';
import {NgbCollapse} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-menu',
  imports: [
    RouterLink,
    NgbCollapse
  ],
  templateUrl: './menu.component.html',
  standalone: true,
  styleUrl: './menu.component.scss'
})
export class MenuComponent implements OnInit {
  isMenuCollapsed = true;


  ngOnInit(): void {
      const linkColor = document.querySelectorAll('.nav-link');
      linkColor.forEach(link => {
        if(window.location.href.endsWith(link.getAttribute('href') || "")){
          link.classList.add('active');
        }
        link.addEventListener('click', () => {
          linkColor.forEach(l => l.classList.remove('active'));
          link.classList.add('active');
        });
      });
  }

  logout() {
    localStorage.removeItem('token');
    window.location.reload();
  }
}
