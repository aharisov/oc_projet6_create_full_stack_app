import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { BehaviorSubject, Observable, catchError, finalize, map, of, shareReplay, tap, throwError } from 'rxjs';

import { AuthResponse } from 'src/app/interfaces/auth-response.interface';
import { LoginRequest } from 'src/app/interfaces/login-request.interface';
import { MessageResponse } from 'src/app/interfaces/message-response.interface';
import { RegisterRequest } from 'src/app/interfaces/register-request.interface';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly registerUrl = `${environment.apiBaseUrl}/auth/register`;
  private readonly loginUrl = `${environment.apiBaseUrl}/auth/login`;
  private readonly refreshUrl = `${environment.apiBaseUrl}/auth/refresh`;
  private readonly logoutUrl = `${environment.apiBaseUrl}/auth/logout`;

  private accessToken: string | null = null;
  private refreshRequest$: Observable<string> | null = null;
  private readonly isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  readonly isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  register(payload: RegisterRequest): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(this.registerUrl, payload);
  }

  login(payload: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(this.loginUrl, payload, {
      withCredentials: true
    }).pipe(
      tap((response) => {
        this.setAccessToken(response.accessToken);
      })
    );
  }

  /**
   * Called once at app startup to rebuild in-memory auth state from refresh cookie.
   */
  tryRestoreSession(): Observable<boolean> {
    return this.refreshAccessToken().pipe(
      map(() => true),
      catchError(() => of(false))
    );
  }

  /**
   * Shares one refresh request across parallel 401 responses to avoid token refresh storms.
   */
  refreshAccessToken(): Observable<string> {
    if (this.refreshRequest$) {
      return this.refreshRequest$;
    }

    this.refreshRequest$ = this.http.post<AuthResponse>(this.refreshUrl, {}, {
      withCredentials: true
    }).pipe(
      map((response) => response.accessToken),
      tap((token) => {
        this.setAccessToken(token);
      }),
      catchError((error) => {
        this.clearAuthSession();
        return throwError(() => error);
      }),
      finalize(() => {
        this.refreshRequest$ = null;
      }),
      shareReplay(1)
    );

    return this.refreshRequest$;
  }

  logout(): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(this.logoutUrl, {}, {
      withCredentials: true
    }).pipe(
      tap(() => {
        this.clearAuthSession();
      })
    );
  }

  getAccessToken(): string | null {
    return this.accessToken;
  }

  isAuthenticated(): boolean {
    return this.isAuthenticatedSubject.value;
  }

  isAuthEndpoint(url: string): boolean {
    return /\/auth\/(login|register|refresh|logout)(\?|$)/.test(url);
  }

  clearAuthSession(): void {
    this.setAccessToken(null);
    this.refreshRequest$ = null;
  }

  private setAccessToken(token: string | null): void {
    this.accessToken = token;
    this.isAuthenticatedSubject.next(Boolean(token));
  }
}
