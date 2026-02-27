import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { BtnReturn } from 'src/app/components/btn-return/btn-return';
import { Topic } from 'src/app/interfaces/topic.interface';
import { TopicsService } from 'src/app/features/topics/services/topics.service';
import { PostsService } from '../../services/posts.service';
import { CreatePostRequest } from 'src/app/interfaces/create-post-request.interface';

@Component({
  selector: 'app-post-create-page',
  standalone: true,
  imports: [ReactiveFormsModule, BtnReturn],
  templateUrl: './post-create-page.component.html',
  styleUrl: './post-create-page.component.css'
})
export class PostCreatePageComponent implements OnInit {
  private readonly formBuilder = inject(FormBuilder);
  private readonly topicsService = inject(TopicsService);
  private readonly postsService = inject(PostsService);

  topics: Topic[] = [];
  isTopicsLoading = false;
  topicsLoadError: string | null = null;

  submitted = false;
  isSubmitting = false;
  submitError: string | null = null;
  submitSuccess: string | null = null;

  postForm = this.formBuilder.group({
    topicId: this.formBuilder.control<number | null>(null, [Validators.required]),
    title: ['', [Validators.required, Validators.maxLength(200)]],
    content: ['', [Validators.required]]
  });

  ngOnInit(): void {
    this.loadTopics();
  }

  onSubmit(): void {
    this.submitted = true;
    this.submitError = null;
    this.submitSuccess = null;

    if (this.postForm.invalid) {
      this.postForm.markAllAsTouched();
      return;
    }

    const topicId = this.postForm.controls.topicId.value;
    if (topicId === null) {
      return;
    }

    const payload: CreatePostRequest = {
      topicId,
      title: (this.postForm.controls.title.value || '').trim(),
      content: (this.postForm.controls.content.value || '').trim()
    };

    this.isSubmitting = true;
    this.postsService.createPost(payload).subscribe({
      next: (response) => {
        this.submitSuccess = response.message || 'Article créé avec succès.';
        this.postForm.reset({
          topicId: null,
          title: '',
          content: ''
        });
        this.submitted = false;
        this.isSubmitting = false;
      },
      error: (error: HttpErrorResponse) => {
        this.submitError = error.error?.message || 'Erreur pendant la creation de l\'article.';
        this.isSubmitting = false;
      }
    });
  }

  private loadTopics(): void {
    this.isTopicsLoading = true;
    this.topicsLoadError = null;

    this.topicsService.getTopics().subscribe({
      next: (topics) => {
        this.topics = topics;
        this.isTopicsLoading = false;
      },
      error: (error: HttpErrorResponse) => {
        this.topicsLoadError = error.error?.message || 'Impossible de charger les themes.';
        this.isTopicsLoading = false;
      }
    });
  }
}
