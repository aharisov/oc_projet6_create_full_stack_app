import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { map } from 'rxjs';

import { AuthService } from 'src/app/features/auth/services/auth.service';

export const authRequiredGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated()) {
    return true;
  }

  // If a refresh cookie exists, rebuild in-memory session before activating protected routes.
  return authService.tryRestoreSession().pipe(
    map((restored) => (restored ? true : router.createUrlTree(['/login'])))
  );
};
