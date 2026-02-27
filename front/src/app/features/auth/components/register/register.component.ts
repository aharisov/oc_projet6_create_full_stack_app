import { Component, OnDestroy, inject } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { BtnReturn } from 'src/app/components/btn-return/btn-return.component';
import { AuthService } from 'src/app/features/auth/services/auth.service';
import { RegisterRequest } from 'src/app/interfaces/register-request.interface';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  imports: [ReactiveFormsModule, BtnReturn],
  standalone: true
})
export class RegisterComponent implements OnDestroy {
  private readonly formBuilder = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private redirectTimeoutId: ReturnType<typeof setTimeout> | null = null;
  private readonly subscriptions = new Subscription();
  private readonly passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).+$/;

  submitted = false;
  isSubmitting = false;
  submitError: string | null = null;
  submitSuccess: string | null = null;

  registerForm = this.formBuilder.nonNullable.group({
    username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]],
    email: ['', [Validators.required, Validators.email, Validators.maxLength(50)]],
    password: ['', [Validators.required, Validators.minLength(8), Validators.pattern(this.passwordPattern)]]
  });

  onSubmit(): void {
    this.submitted = true;
    this.submitError = null;
    this.submitSuccess = null;

    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    const payload: RegisterRequest = {
      username: this.registerForm.controls.username.value.trim(),
      email: this.registerForm.controls.email.value.trim().toLowerCase(),
      password: this.registerForm.controls.password.value
    };

    this.isSubmitting = true;
    this.subscriptions.add(
      this.authService.register(payload).subscribe({
        next: (response) => {
          this.submitSuccess = response.message || 'Inscription reussie.';
          this.registerForm.reset({
            username: '',
            email: '',
            password: ''
          });
          this.submitted = false;
          this.isSubmitting = false;
          if (this.redirectTimeoutId) {
            clearTimeout(this.redirectTimeoutId);
          }
          this.redirectTimeoutId = setTimeout(() => {
            void this.router.navigate(['/login']);
          }, 2000);
        },
        error: (error: HttpErrorResponse) => {
          this.submitError = error.error?.message || "Erreur pendant l'inscription.";
          this.isSubmitting = false;
        }
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();

    if (this.redirectTimeoutId) {
      clearTimeout(this.redirectTimeoutId);
      this.redirectTimeoutId = null;
    }
  }
}
