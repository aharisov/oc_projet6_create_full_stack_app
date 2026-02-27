import { Component, Input } from '@angular/core';

import { Comment } from 'src/app/interfaces/comment.interface';

@Component({
  selector: 'app-comments-list',
  standalone: true,
  templateUrl: './comments-list.component.html',
  styleUrl: './comments-list.component.css'
})
export class CommentsListComponent {
  @Input({ required: true }) comments: Comment[] = [];
}
