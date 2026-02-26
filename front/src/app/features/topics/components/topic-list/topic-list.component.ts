import { Component, Input } from '@angular/core';

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
  @Input() pendingTopicIds: ReadonlySet<number> = new Set<number>();
}
