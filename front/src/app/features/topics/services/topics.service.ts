import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

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
}
