import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, switchMap, throwError } from 'rxjs';

import { AuthService } from 'src/app/features/auth/services/auth.service';

const RETRY_HEADER = 'x-auth-retry';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const isAuthRequest = authService.isAuthEndpoint(req.url);
  const accessToken = authService.getAccessToken();

  let request = req;
  if (accessToken && !isAuthRequest) {
    request = req.clone({
      setHeaders: {
        Authorization: `Bearer ${accessToken}`
      }
    });
  }

  return next(request).pipe(
    catchError((error: HttpErrorResponse) => {
      const alreadyRetried = req.headers.has(RETRY_HEADER);
      if (error.status !== 401 || isAuthRequest || alreadyRetried) {
        return throwError(() => error);
      }

      // Refresh once, then replay the original request with the new token.
      return authService.refreshAccessToken().pipe(
        switchMap((newToken) => {
          const retryRequest = req.clone({
            setHeaders: {
              Authorization: `Bearer ${newToken}`,
              [RETRY_HEADER]: '1'
            }
          });

          return next(retryRequest);
        }),
        catchError((refreshError) => {
          authService.clearAuthSession();
          return throwError(() => refreshError);
        })
      );
    })
  );
};
