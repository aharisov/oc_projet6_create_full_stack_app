import { Component, OnInit, inject } from '@angular/core';
import { AuthService } from './features/auth/services/auth.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css'],
    standalone: false
})
export class AppComponent implements OnInit {
  private readonly authService = inject(AuthService);

  ngOnInit(): void {
    this.authService.tryRestoreSession().subscribe();
  }
}