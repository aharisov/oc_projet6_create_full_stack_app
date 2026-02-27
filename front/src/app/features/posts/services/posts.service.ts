import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { CreatePostRequest } from 'src/app/interfaces/create-post-request.interface';
import { MessageResponse } from 'src/app/interfaces/message-response.interface';
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

  createPost(payload: CreatePostRequest): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(this.postsUrl, payload);
  }
}
