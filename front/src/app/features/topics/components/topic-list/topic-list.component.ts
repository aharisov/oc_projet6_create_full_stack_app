import { Component, EventEmitter, Input, Output } from '@angular/core';

import { Topic } from 'src/app/interfaces/topic.interface';
import { TopicItemComponent } from '../topic-item/topic-item.component';

@Component({
  selector: 'app-topic-list',
  standalone: true,
  imports: [TopicItemComponent],
  templateUrl: './topic-list.component.html',
  styleUrl: './topic-list.component.css'
})
export class TopicListComponent {
  @Input({ required: true }) topics: Topic[] = [];
  @Input() subscribedTopicIds: ReadonlySet<number> = new Set<number>();
  @Input() pendingTopicIds: ReadonlySet<number> = new Set<number>();
  @Output() subscribeRequested = new EventEmitter<number>();
  @Output() unsubscribeRequested = new EventEmitter<number>();

  onSubscribeRequested(topicId: number): void {
    this.subscribeRequested.emit(topicId);
  }

  onUnsubscribeRequested(topicId: number): void {
    this.unsubscribeRequested.emit(topicId);
  }
}
