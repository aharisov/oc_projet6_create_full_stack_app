import { AsyncPipe } from '@angular/common';
import { Component, HostListener, inject } from '@angular/core';
import { RouterLink } from '@angular/router';

import { AuthService } from 'src/app/features/auth/services/auth.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, AsyncPipe],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class HeaderComponent {
  private readonly authService = inject(AuthService);
  readonly isAuthenticated$ = this.authService.isAuthenticated$;
  isMobileMenuOpen = false;

  toggleMobileMenu(): void {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }

  closeMobileMenu(): void {
    this.isMobileMenuOpen = false;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    if (!this.isMobileMenuOpen) {
      return;
    }

    const target = event.target as HTMLElement | null;
    const clickedInsideMenu = !!target?.closest('.user-menu');
    const clickedBurger = !!target?.closest('.mobile-burger');

    if (!clickedInsideMenu && !clickedBurger) {
      this.closeMobileMenu();
    }
  }

  @HostListener('window:keydown.escape')
  onEscapeKey(): void {
    this.closeMobileMenu();
  }

  @HostListener('window:resize')
  onResize(): void {
    if (window.innerWidth > 639) {
      this.closeMobileMenu();
    }
  }
}
