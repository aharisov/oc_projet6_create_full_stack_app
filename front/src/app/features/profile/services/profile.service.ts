import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { MessageResponse } from 'src/app/interfaces/message-response.interface';
import { UpdateUserRequest } from 'src/app/interfaces/update-user-request.interface';
import { UserProfile } from 'src/app/interfaces/user-profile.interface';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private readonly http = inject(HttpClient);
  private readonly profileUrl = `${environment.apiBaseUrl}/users/me`;

  getProfile(): Observable<UserProfile> {
    return this.http.get<UserProfile>(this.profileUrl);
  }

  updateProfile(payload: UpdateUserRequest): Observable<MessageResponse> {
    return this.http.put<MessageResponse>(this.profileUrl, payload);
  }
}
