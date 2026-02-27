import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';

type ProfileForm = FormGroup<{
  username: FormControl<string>;
  email: FormControl<string>;
  password: FormControl<string>;
}>;

@Component({
  selector: 'app-profile-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './profile-form.component.html',
  styleUrl: './profile-form.component.css'
})
export class ProfileFormComponent {
  @Input({ required: true }) profileForm!: ProfileForm;
  @Input() submitted = false;
  @Input() isSubmitting = false;
  @Input() submitError: string | null = null;
  @Input() submitSuccess: string | null = null;
  @Output() formSubmitted = new EventEmitter<void>();

  onSubmit(): void {
    this.formSubmitted.emit();
  }
}
