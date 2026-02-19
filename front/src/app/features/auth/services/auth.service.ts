import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { MessageResponse } from 'src/app/interfaces/message-response.interface';
import { RegisterRequest } from 'src/app/interfaces/register-request.interface';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly registerUrl = `${environment.apiBaseUrl}/auth/register`;

  register(payload: RegisterRequest): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(this.registerUrl, payload);
  }
}
