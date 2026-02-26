import { Component, inject } from '@angular/core';
import { AuthService } from 'src/app/features/auth/services/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  standalone: false
})
export class HomeComponent {
  private readonly authService = inject(AuthService);
  readonly isAuthenticated$ = this.authService.isAuthenticated$;
}
