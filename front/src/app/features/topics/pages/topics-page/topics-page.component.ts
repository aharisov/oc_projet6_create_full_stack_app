import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { forkJoin } from 'rxjs';

import { TopicsService } from '../../services/topics.service';
import { TopicListComponent } from '../../components/topic-list/topic-list.component';
import { Topic } from 'src/app/interfaces/topic.interface';

@Component({
  selector: 'app-topics-page',
  standalone: true,
  imports: [TopicListComponent],
  templateUrl: './topics-page.component.html',
  styleUrl: './topics-page.component.css'
})
export class TopicsPageComponent implements OnInit {
  private readonly topicsService = inject(TopicsService);

  topics: Topic[] = [];
  pendingTopicIds = new Set<number>();
  isLoading = false;
  loadError: string | null = null;
  actionError: string | null = null;

  ngOnInit(): void {
    this.loadTopics();
  }

  private loadTopics(): void {
    this.isLoading = true;
    this.loadError = null;

    forkJoin({
      topics: this.topicsService.getTopics()
    }).subscribe({
      next: ({ topics }) => {
        this.topics = topics;
        this.isLoading = false;
      },
      error: (error: HttpErrorResponse) => {
        this.loadError = this.extractApiError(error, 'Impossible de charger les thèmes.');
        this.isLoading = false;
      }
    });
  }

  private extractApiError(error: HttpErrorResponse, fallback: string): string {
    return error.error?.message || fallback;
  }
}
