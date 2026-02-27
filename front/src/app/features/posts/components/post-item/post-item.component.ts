import { DatePipe } from '@angular/common';
import { Component, Input } from '@angular/core';

import { Post } from 'src/app/interfaces/post.interface';

@Component({
  selector: 'app-post-item',
  standalone: true,
  imports: [DatePipe],
  templateUrl: './post-item.component.html',
  styleUrl: './post-item.component.css'
})
export class PostItemComponent {
  @Input({ required: true }) post!: Post;
}
