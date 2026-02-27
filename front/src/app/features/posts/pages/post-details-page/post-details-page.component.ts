import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';

import { BtnReturn } from 'src/app/components/btn-return/btn-return.component';
import { Comment } from 'src/app/interfaces/comment.interface';
import { Post } from 'src/app/interfaces/post.interface';
import { CommentFormComponent } from '../../components/comment-form/comment-form.component';
import { CommentsListComponent } from '../../components/comments-list/comments-list.component';
import { CommentsService } from '../../services/comments.service';
import { PostsService } from '../../services/posts.service';

@Component({
  selector: 'app-post-details-page',
  standalone: true,
  imports: [BtnReturn, DatePipe, CommentsListComponent, CommentFormComponent],
  templateUrl: './post-details-page.component.html',
  styleUrl: './post-details-page.component.css'
})
export class PostDetailsPageComponent implements OnInit, OnDestroy {
  private readonly formBuilder = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly postsService = inject(PostsService);
  private readonly commentsService = inject(CommentsService);
  private readonly subscriptions = new Subscription();

  private postId: number | null = null;
  post: Post | null = null;
  comments: Comment[] = [];
  isLoading = false;
  areCommentsLoading = false;
  isSubmittingComment = false;
  loadError: string | null = null;
  commentsError: string | null = null;
  commentSubmitError: string | null = null;

  commentForm = this.formBuilder.nonNullable.group({
    content: ['', [Validators.required, Validators.maxLength(2000)]]
  });

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    const postId = Number(idParam);

    if (!idParam || Number.isNaN(postId) || postId <= 0) {
      this.loadError = 'Article introuvable.';
      return;
    }

    this.postId = postId;
    this.loadPost(postId);
    this.loadComments(postId);
  }

  onSubmitComment(): void {
    this.commentSubmitError = null;

    if (!this.postId) {
      this.commentSubmitError = 'Article introuvable.';
      return;
    }

    if (this.commentForm.invalid) {
      this.commentForm.markAllAsTouched();
      return;
    }

    const content = this.commentForm.controls.content.value.trim();
    this.isSubmittingComment = true;

    this.subscriptions.add(
      this.commentsService.createComment(this.postId, content).subscribe({
        next: () => {
          this.commentForm.reset({ content: '' });
          this.isSubmittingComment = false;
          this.loadComments(this.postId as number);
        },
        error: (error: HttpErrorResponse) => {
          this.commentSubmitError = error.error?.message || 'Impossible d\'envoyer le commentaire.';
          this.isSubmittingComment = false;
        }
      })
    );
  }

  private loadPost(postId: number): void {
    this.isLoading = true;
    this.loadError = null;

    this.subscriptions.add(
      this.postsService.getPost(postId).subscribe({
        next: (post) => {
          this.post = post;
          this.isLoading = false;
        },
        error: (error: HttpErrorResponse) => {
          this.loadError = error.error?.message || 'Impossible de charger cet article.';
          this.isLoading = false;
        }
      })
    );
  }

  private loadComments(postId: number): void {
    this.areCommentsLoading = true;
    this.commentsError = null;

    this.subscriptions.add(
      this.commentsService.getComments(postId).subscribe({
        next: (comments) => {
          this.comments = comments;
          this.areCommentsLoading = false;
        },
        error: (error: HttpErrorResponse) => {
          this.commentsError = error.error?.message || 'Impossible de charger les commentaires.';
          this.areCommentsLoading = false;
        }
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
