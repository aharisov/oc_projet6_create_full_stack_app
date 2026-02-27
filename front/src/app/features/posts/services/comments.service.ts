import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { Comment } from 'src/app/interfaces/comment.interface';
import { MessageResponse } from 'src/app/interfaces/message-response.interface';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CommentsService {
  private readonly http = inject(HttpClient);
  private readonly postsUrl = `${environment.apiBaseUrl}/posts`;

  getComments(postId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.postsUrl}/${postId}/comments`);
  }

  createComment(postId: number, content: string): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(`${this.postsUrl}/${postId}/comments`, { content });
  }
}
