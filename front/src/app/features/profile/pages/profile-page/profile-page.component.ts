import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

import { UpdateUserRequest } from 'src/app/interfaces/update-user-request.interface';
import { UserProfile } from 'src/app/interfaces/user-profile.interface';
import { ProfileFormComponent } from '../../components/profile-form/profile-form.component';
import { ProfileService } from '../../services/profile.service';

@Component({
  selector: 'app-profile-page',
  standalone: true,
  imports: [ProfileFormComponent],
  templateUrl: './profile-page.component.html',
  styleUrl: './profile-page.component.css'
})
export class ProfilePageComponent implements OnInit {
  private readonly formBuilder = inject(FormBuilder);
  private readonly profileService = inject(ProfileService);
  private readonly passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).+$/;

  private profile: UserProfile | null = null;

  isLoading = false;
  loadError: string | null = null;
  submitted = false;
  isSubmitting = false;
  submitError: string | null = null;
  submitSuccess: string | null = null;

  profileForm = this.formBuilder.nonNullable.group({
    username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]],
    email: ['', [Validators.required, Validators.email, Validators.maxLength(50)]],
    password: ['', [Validators.minLength(8), Validators.pattern(this.passwordPattern)]]
  });

  ngOnInit(): void {
    this.loadProfile();
  }

  onSubmit(): void {
    this.submitted = true;
    this.submitError = null;
    this.submitSuccess = null;

    if (this.profileForm.invalid || !this.profile) {
      this.profileForm.markAllAsTouched();
      return;
    }

    const nextUsername = this.profileForm.controls.username.value.trim();
    const nextEmail = this.profileForm.controls.email.value.trim().toLowerCase();
    const nextPassword = this.profileForm.controls.password.value;

    const payload: UpdateUserRequest = {};
    if (nextUsername !== this.profile.username) {
      payload.username = nextUsername;
    }
    if (nextEmail !== this.profile.email) {
      payload.email = nextEmail;
    }
    if (nextPassword.trim().length > 0) {
      payload.password = nextPassword;
    }

    if (Object.keys(payload).length === 0) {
      this.submitError = 'Aucune modification détectée.';
      return;
    }

    this.isSubmitting = true;
    this.profileService.updateProfile(payload).subscribe({
      next: (response) => {
        this.submitSuccess = response.message || 'Profil mis à jour.';
        this.isSubmitting = false;
        this.loadProfile();
      },
      error: (error: HttpErrorResponse) => {
        this.submitError = error.error?.message || 'Erreur pendant la mise à jour du profil.';
        this.isSubmitting = false;
      }
    });
  }

  private loadProfile(): void {
    this.isLoading = true;
    this.loadError = null;

    this.profileService.getProfile().subscribe({
      next: (profile) => {
        this.profile = profile;
        this.profileForm.reset({
          username: profile.username,
          email: profile.email,
          password: ''
        });
        this.submitted = false;
        this.isLoading = false;
      },
      error: (error: HttpErrorResponse) => {
        this.loadError = error.error?.message || 'Impossible de charger votre profil.';
        this.isLoading = false;
      }
    });
  }
}
