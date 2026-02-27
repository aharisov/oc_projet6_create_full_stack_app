import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { MessageResponse } from 'src/app/interfaces/message-response.interface';
import { Topic } from 'src/app/interfaces/topic.interface';
import { environment } from 'src/environments/environment';


@Injectable({
  providedIn: 'root'
})
export class TopicsService {
  private readonly http = inject(HttpClient);
  private readonly topicsUrl = `${environment.apiBaseUrl}/topics`;

  getTopics(): Observable<Topic[]> {
    return this.http.get<Topic[]>(this.topicsUrl);
  }

  getSubscribedTopics(): Observable<Topic[]> {
    return this.http.get<Topic[]>(`${this.topicsUrl}/subscribed`);
  }

  subscribe(topicId: number): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(`${this.topicsUrl}/${topicId}/subscribe`, {});
  }

  unsubscribe(topicId: number): Observable<MessageResponse> {
    return this.http.delete<MessageResponse>(`${this.topicsUrl}/${topicId}/unsubscribe`);
  }
}
