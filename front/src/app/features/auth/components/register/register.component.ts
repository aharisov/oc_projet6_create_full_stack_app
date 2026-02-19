import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { BtnReturn } from 'src/app/components/btn-return/btn-return';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  imports: [ReactiveFormsModule, BtnReturn],
  standalone: true
})
export class RegisterComponent {
  private readonly formBuilder = inject(FormBuilder);

  submitted = false;

  registerForm = this.formBuilder.nonNullable.group({
    username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]],
    email: ['', [Validators.required, Validators.email, Validators.maxLength(50)]],
    password: ['', [Validators.required, Validators.minLength(8)]]
  });

  onSubmit(): void {
    this.submitted = true;
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    const payload = {
      username: this.registerForm.controls.username.value.trim(),
      email: this.registerForm.controls.email.value.trim().toLowerCase(),
      password: this.registerForm.controls.password.value
    };

    // TODO: replace with backend call.
    console.log('Register payload', payload);
  }
}
