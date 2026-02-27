import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Subscription } from 'rxjs';

import { HeaderComponent } from './components/header/header.component';
import { AuthService } from './features/auth/services/auth.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css'],
    standalone: true,
    imports: [HeaderComponent, RouterOutlet]
})
export class AppComponent implements OnInit, OnDestroy {
  private readonly authService = inject(AuthService);
  private readonly subscriptions = new Subscription();

  ngOnInit(): void {
    this.subscriptions.add(this.authService.tryRestoreSession().subscribe());
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
