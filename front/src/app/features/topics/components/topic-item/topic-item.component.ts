import { Component, EventEmitter, Input, Output } from '@angular/core';

import { Topic } from 'src/app/interfaces/topic.interface';

@Component({
  selector: 'app-topic-item',
  standalone: true,
  templateUrl: './topic-item.component.html',
  styleUrl: './topic-item.component.css'
})
export class TopicItemComponent {
  @Input({ required: true }) topic!: Topic;
  @Input() isSubscribed = false;
  @Input() isPending = false;
  @Output() subscribeRequested = new EventEmitter<number>();
  @Output() unsubscribeRequested = new EventEmitter<number>();

  onToggleSubscription(): void {
    if (this.isPending) {
      return;
    }

    if (this.isSubscribed) {
      this.unsubscribeRequested.emit(this.topic.id);
      return;
    }

    this.subscribeRequested.emit(this.topic.id);
  }
}
