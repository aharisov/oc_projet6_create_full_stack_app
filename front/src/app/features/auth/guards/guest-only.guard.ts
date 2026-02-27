import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { map } from 'rxjs';

import { AuthService } from 'src/app/features/auth/services/auth.service';

export const guestOnlyGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated()) {
    return router.createUrlTree(['/posts']);
  }

  // If a refresh cookie exists, rebuild in-memory session and redirect guests-only routes.
  return authService.tryRestoreSession().pipe(
    map((restored) => (restored ? router.createUrlTree(['/posts']) : true))
  );
};
