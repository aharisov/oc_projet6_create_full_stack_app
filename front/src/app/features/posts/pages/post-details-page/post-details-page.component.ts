import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { BtnReturn } from 'src/app/components/btn-return/btn-return';
import { Post } from 'src/app/interfaces/post.interface';
import { PostsService } from '../../services/posts.service';

@Component({
  selector: 'app-post-details-page',
  standalone: true,
  imports: [BtnReturn, DatePipe],
  templateUrl: './post-details-page.component.html',
  styleUrl: './post-details-page.component.css'
})
export class PostDetailsPageComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly postsService = inject(PostsService);

  post: Post | null = null;
  isLoading = false;
  loadError: string | null = null;

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    const postId = Number(idParam);

    if (!idParam || Number.isNaN(postId) || postId <= 0) {
      this.loadError = 'Article introuvable.';
      return;
    }

    this.loadPost(postId);
  }

  private loadPost(postId: number): void {
    this.isLoading = true;
    this.loadError = null;

    this.postsService.getPost(postId).subscribe({
      next: (post) => {
        this.post = post;
        this.isLoading = false;
      },
      error: (error: HttpErrorResponse) => {
        this.loadError = error.error?.message || 'Impossible de charger cet article.';
        this.isLoading = false;
      }
    });
  }
}
