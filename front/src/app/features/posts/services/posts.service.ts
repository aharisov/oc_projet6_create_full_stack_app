import { HttpClient, HttpParams } from '@angular/common/http';
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

  getFeed(sortOrder: 'asc' | 'desc' = 'desc'): Observable<Post[]> {
    const params = new HttpParams().set('sort', sortOrder);

    return this.http.get<Post[]>(this.postsUrl, { params });
  }

  getPost(postId: number): Observable<Post> {
    return this.http.get<Post>(`${this.postsUrl}/${postId}`);
  }
}
