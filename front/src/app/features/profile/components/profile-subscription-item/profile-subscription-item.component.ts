import { Component, EventEmitter, Input, Output } from '@angular/core';

import { Topic } from 'src/app/interfaces/topic.interface';

@Component({
  selector: 'app-profile-subscription-item',
  standalone: true,
  templateUrl: './profile-subscription-item.component.html',
  styleUrl: './profile-subscription-item.component.css'
})
export class ProfileSubscriptionItemComponent {
  @Input({ required: true }) topic!: Topic;
  @Input() isPending = false;
  @Output() unsubscribeRequested = new EventEmitter<number>();

  onUnsubscribe(): void {
    if (this.isPending) {
      return;
    }

    this.unsubscribeRequested.emit(this.topic.id);
  }
}
