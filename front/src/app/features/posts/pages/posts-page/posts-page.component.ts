import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';

import { Post } from 'src/app/interfaces/post.interface';
import { PostsService } from '../../services/posts.service';
import { PostListComponent } from '../../components/post-list/post-list.component';

@Component({
  selector: 'app-posts-page',
  standalone: true,
  imports: [PostListComponent, RouterLink],
  templateUrl: './posts-page.component.html',
  styleUrl: './posts-page.component.css'
})
export class PostsPageComponent implements OnInit, OnDestroy {
  private readonly postsService = inject(PostsService);
  private readonly subscriptions = new Subscription();

  posts: Post[] = [];
  sortOrder: 'asc' | 'desc' = 'desc';
  isLoading = false;
  loadError: string | null = null;

  ngOnInit(): void {
    this.loadPosts();
  }

  onToggleSort(): void {
    this.sortOrder = this.sortOrder === 'desc' ? 'asc' : 'desc';
    this.loadPosts();
  }

  private loadPosts(): void {
    this.isLoading = true;
    this.loadError = null;

    this.subscriptions.add(
      this.postsService.getFeed(this.sortOrder).subscribe({
        next: (posts) => {
          this.posts = posts;
          this.isLoading = false;
        },
        error: (error: HttpErrorResponse) => {
          this.loadError = error.error?.message || 'Impossible de charger les articles.';
          this.isLoading = false;
        }
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
