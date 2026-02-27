import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';

import { Post } from 'src/app/interfaces/post.interface';
import { PostsService } from '../../services/posts.service';
import { PostListComponent } from '../../components/post-list/post-list.component';

@Component({
  selector: 'app-posts-page',
  standalone: true,
  imports: [PostListComponent],
  templateUrl: './posts-page.component.html',
  styleUrl: './posts-page.component.css'
})
export class PostsPageComponent implements OnInit {
  private readonly postsService = inject(PostsService);

  posts: Post[] = [];
  isLoading = false;
  loadError: string | null = null;

  ngOnInit(): void {
    this.loadPosts();
  }

  private loadPosts(): void {
    this.isLoading = true;
    this.loadError = null;

    this.postsService.getFeed().subscribe({
      next: (posts) => {
        this.posts = posts;
        this.isLoading = false;
      },
      error: (error: HttpErrorResponse) => {
        this.loadError = error.error?.message || 'Impossible de charger les articles.';
        this.isLoading = false;
      }
    });
  }
}
