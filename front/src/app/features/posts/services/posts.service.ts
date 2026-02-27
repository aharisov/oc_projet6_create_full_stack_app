import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { Post } from 'src/app/interfaces/post.interface';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PostsService {
  private readonly http = inject(HttpClient);
  private readonly postsUrl = `${environment.apiBaseUrl}/posts`;

  getFeed(): Observable<Post[]> {
    return this.http.get<Post[]>(this.postsUrl);
  }
}
