import { Component, Input } from '@angular/core';

import { Topic } from 'src/app/interfaces/topic.interface';

@Component({
  selector: 'app-topic-item',
  standalone: true,
  templateUrl: './topic-item.component.html',
  styleUrl: './topic-item.component.css'
})
export class TopicItemComponent {
  @Input({ required: true }) topic!: Topic;
  @Input() isPending = false;
}
