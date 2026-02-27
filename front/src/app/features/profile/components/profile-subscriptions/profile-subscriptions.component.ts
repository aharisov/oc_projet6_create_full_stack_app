import { Component, EventEmitter, Input, Output } from '@angular/core';

import { Topic } from 'src/app/interfaces/topic.interface';
import { ProfileSubscriptionItemComponent } from '../profile-subscription-item/profile-subscription-item.component';

@Component({
  selector: 'app-profile-subscriptions',
  standalone: true,
  imports: [ProfileSubscriptionItemComponent],
  templateUrl: './profile-subscriptions.component.html',
  styleUrl: './profile-subscriptions.component.css'
})
export class ProfileSubscriptionsComponent {
  @Input({ required: true }) topics: Topic[] = [];
  @Input() pendingTopicIds: ReadonlySet<number> = new Set<number>();
  @Output() unsubscribeRequested = new EventEmitter<number>();

  onUnsubscribeRequested(topicId: number): void {
    this.unsubscribeRequested.emit(topicId);
  }
}
