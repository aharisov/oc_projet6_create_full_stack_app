import { Component, Input } from '@angular/core';

import { Post } from 'src/app/interfaces/post.interface';
import { PostItemComponent } from '../post-item/post-item.component';

@Component({
  selector: 'app-post-list',
  standalone: true,
  imports: [PostItemComponent],
  templateUrl: './post-list.component.html',
  styleUrl: './post-list.component.css'
})
export class PostListComponent {
  @Input({ required: true }) posts: Post[] = [];
}
