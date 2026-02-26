import { Component, inject } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/features/auth/services/auth.service';
import { LoginRequest } from 'src/app/interfaces/login-request.interface';
import { BtnReturn } from 'src/app/components/btn-return/btn-return';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [ReactiveFormsModule, BtnReturn],
  standalone: true
})
export class LoginComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  submitted = false;
  isSubmitting = false;
  submitError: string | null = null;

  loginForm = this.formBuilder.nonNullable.group({
    identifier: ['', [Validators.required, Validators.maxLength(50)]],
    password: ['', [Validators.required, Validators.minLength(8)]]
  });

  onSubmit(): void {
    this.submitted = true;
    this.submitError = null;

    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    const payload: LoginRequest = {
      identifier: this.loginForm.controls.identifier.value.trim(),
      password: this.loginForm.controls.password.value
    };

    this.isSubmitting = true;
    this.authService.login(payload).subscribe({
      next: () => {
        this.isSubmitting = false;
        void this.router.navigate(['/']);
      },
      error: (error: HttpErrorResponse) => {
        this.submitError = error.error?.message || 'Erreur pendant la connexion.';
        this.isSubmitting = false;
      }
    });
  }
}
