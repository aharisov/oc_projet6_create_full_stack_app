import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';

type CommentForm = FormGroup<{
  content: FormControl<string>;
}>;

@Component({
  selector: 'app-comment-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './comment-form.component.html',
  styleUrl: './comment-form.component.css'
})
export class CommentFormComponent {
  @Input({ required: true }) commentForm!: CommentForm;
  @Input() isSubmitting = false;
  @Input() submitError: string | null = null;
  @Output() formSubmitted = new EventEmitter<void>();

  onFormSubmit(): void {
    this.formSubmitted.emit();
  }
}
