import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { Subscription, forkJoin } from 'rxjs';

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
export class TopicsPageComponent implements OnInit, OnDestroy {
  private readonly topicsService = inject(TopicsService);
  private readonly subscriptions = new Subscription();

  topics: Topic[] = [];
  subscribedTopicIds = new Set<number>();
  pendingTopicIds = new Set<number>();
  isLoading = false;
  loadError: string | null = null;
  actionError: string | null = null;

  ngOnInit(): void {
    this.loadTopics();
  }

  onSubscribeRequested(topicId: number): void {
    this.actionError = null;
    this.updatePendingTopicIds(topicId, true);
    this.subscriptions.add(
      this.topicsService.subscribe(topicId).subscribe({
        next: () => {
          const nextSubscribedTopicIds = new Set(this.subscribedTopicIds);
          nextSubscribedTopicIds.add(topicId);
          this.subscribedTopicIds = nextSubscribedTopicIds;
          this.updatePendingTopicIds(topicId, false);
        },
        error: (error: HttpErrorResponse) => {
          this.actionError = this.extractApiError(error, "Impossible de s'abonner pour le moment.");
          this.updatePendingTopicIds(topicId, false);
        }
      })
    );
  }

  private loadTopics(): void {
    this.isLoading = true;
    this.loadError = null;

    this.subscriptions.add(
      forkJoin({
        topics: this.topicsService.getTopics(),
        subscribedTopics: this.topicsService.getSubscribedTopics()
      }).subscribe({
        next: ({ topics, subscribedTopics }) => {
          this.topics = topics;
          this.subscribedTopicIds = new Set(subscribedTopics.map((topic) => topic.id));
          this.isLoading = false;
        },
        error: (error: HttpErrorResponse) => {
          this.loadError = this.extractApiError(error, 'Impossible de charger les thèmes.');
          this.isLoading = false;
        }
      })
    );
  }

  private extractApiError(error: HttpErrorResponse, fallback: string): string {
    return error.error?.message || fallback;
  }

  private updatePendingTopicIds(topicId: number, isPending: boolean): void {
    const nextPendingTopicIds = new Set(this.pendingTopicIds);

    if (isPending) {
      nextPendingTopicIds.add(topicId);
    } else {
      nextPendingTopicIds.delete(topicId);
    }

    this.pendingTopicIds = nextPendingTopicIds;
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
